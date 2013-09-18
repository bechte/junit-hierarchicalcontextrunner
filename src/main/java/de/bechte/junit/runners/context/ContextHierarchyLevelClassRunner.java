package de.bechte.junit.runners.context;

import org.junit.After;
import org.junit.Before;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * This runner is used to execute the tests within a level of the context
 * hierarchy. For each test within the member classes, all @Before and @After
 * methods of the entire context hierarchy are executed. Also, @Rule
 * annotations are evaluated.
 *
 * Please refer to the wiki for a sample and more information:
 * https://github.com/bechte/junit-hierarchicalcontextrunner/wiki
 */
class ContextHierarchyLevelClassRunner extends BlockJUnit4ClassRunner {
    private LinkedList<Object> instances = null;

    public ContextHierarchyLevelClassRunner(final Class<?> tClass) throws InitializationError {
        super(tClass);
    }

    @Override
    protected String getName() {
        return getTestClass().getJavaClass().getSimpleName();
    }

    @Override
    protected void validateNoNonStaticInnerClass(final List<Throwable> errors) {
        // This contradicts the concept of the hierarchical context runner!
    }

    @Override
    protected void validateConstructor(final List<Throwable> errors) {
        if (!getTestClass().getJavaClass().isMemberClass()) {
            super.validateConstructor(errors);
        } else {
            validateContextConstructor(errors);
        }
    }

    private void validateContextConstructor(final List<Throwable> errors) {
        final Class<?> enclosingClass = getTestClass().getJavaClass().getEnclosingClass();
        final Class<?>[] parameterTypes = getTestClass().getOnlyConstructor().getParameterTypes();

        if (parameterTypes.length != 1) {
            String gripe = "Test context should have exactly one public one-argument constructor!";
            errors.add(new Exception(gripe));
        } else if (enclosingClass != null && !enclosingClass.isAssignableFrom(parameterTypes[0])) {
            String gripe = "Context constructor must take an instance of the enclosing class!";
            errors.add(new Exception(gripe));
        }
    }

    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        // Top level class has already been processed by the
        // HierarchicalContextRunner, so just invoke children!
        return childrenInvoker(notifier);
    }

    @Override
    protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement next) {
        Statement statement = next;
        for (int i = instances.size() - 1; i >= 0; i--) {
            final Object instance = instances.get(i);
            final TestClass tClass = new TestClass(instance.getClass());
            final List<FrameworkMethod> befores = tClass.getAnnotatedMethods(Before.class);
            statement = befores.isEmpty() ? statement : new RunBefores(statement, befores, instance);
        }
        return statement;
    }

    @Override
    protected Statement withAfters(final FrameworkMethod method, final Object target, final Statement next) {
        Statement statement = next;
        for (int i = instances.size() - 1; i >= 0; i--) {
            final Object instance = instances.get(i);
            final TestClass tClass = new TestClass(instance.getClass());
            final List<FrameworkMethod> afters = tClass.getAnnotatedMethods(After.class);
            statement = afters.isEmpty() ? statement : new RunAfters(statement, afters, instance);
        }
        return statement;
    }

    @Override
    protected Object createTest() throws Exception {
        ensureHierarchicalFixturesAreValid();
        return instances.getLast();
    }

    /**
     * Returns new fixtures for the entire class hierarchy for running a test.
     * Default implementation executes the test class's no-argument constructor
     * and the inner class default constructor taking the outer class' instance
     * (validation should have ensured one exists).
     */
    protected synchronized void ensureHierarchicalFixturesAreValid() throws Exception {
        if (instances == null) {
            final Stack<Class<?>> classHierarchy = getClassHierarchy();
            instances = new LinkedList<Object>();

            // Top level class has empty constructor
            instances.add(classHierarchy.pop().newInstance());

            // Inner class constructors require the enclosing instance
            while (!classHierarchy.empty()) {
                final Object enclosingInstance = instances.getLast();
                final Class<?> innerClass = classHierarchy.pop();
                instances.add(createInnerInstance(enclosingInstance, innerClass));
            }
        }
    }

    private Stack<Class<?>> getClassHierarchy() {
        final Stack<Class<?>> classHierarchy = new Stack<Class<?>>();
        for (Class<?> currentClass = getTestClass().getJavaClass();
                currentClass != null;
                currentClass = currentClass.getEnclosingClass())
            classHierarchy.push(currentClass);
        return classHierarchy;
    }

    private Object createInnerInstance(Object outerInstance, Class<?> innerClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Class<?> outerClass = outerInstance.getClass();
        final Constructor<?> innerConstructor = innerClass.getConstructor(outerClass);
        return innerConstructor.newInstance(outerInstance);
    }
}