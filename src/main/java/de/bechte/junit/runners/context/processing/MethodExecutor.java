package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.context.description.Describer;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import de.bechte.junit.runners.context.statements.TestStatementExecutor;
import de.bechte.junit.runners.model.TestClassPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.MethodRule;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The {@link MethodExecutor} is responsible for executing a test for the given {@link FrameworkMethod}.
 *
 * This implementation creates a new test instance along the context hierarchy, i.e. it starts by creating an instance
 * of the outer class and then creates instances of the inner classes until the actual test class to which the current
 * {@link FrameworkMethod} belongs to is created.
 *
 * Currently, the following annotations are supported by this executor:
 * {@link Before}, {@link After}, {@link ExpectException}, {@link FailOnTimeout}, {@link Rule}
 *
 * Note: All annotations are evaluated for the entire context hierarchy of the test instance. Example:
 *
 * <code>
 *   public class A {
 *     @Before public void setUpA() {...}
 *     @After public void tearDownA() {...}
 *
 *     @Test public void test1() {...}
 *
 *     public class B {
 *       @Before public void setUpB() {...}
 *       @After public void tearDownB() {...}
 *
 *       @Test public void test2() {...}
 *     }
 *   }
 * </code>
 *
 * When test2 is executed, the following methods are called in sequence:
 * setUpA(), setUpB(), test2(), tearDownB(), tearDownA()
 */
public class MethodExecutor implements ChildExecutor<FrameworkMethod> {
    private Describer<FrameworkMethod> describer;
    private StatementExecutor statementExecutor;

    public MethodExecutor(final Describer<FrameworkMethod> describer) {
        this.describer = describer;
        this.statementExecutor = new TestStatementExecutor();
    }

    @Override
    public void run(final TestClass testClass, final FrameworkMethod method, final RunNotifier notifier) {
        final Description description = describer.describe(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            try {
                final Stack<Class<?>> classHierarchy = getClassHierarchy(testClass);
                final Object test = createTestInstance(classHierarchy);
                Statement statement = buildStatement(method, test);
                statementExecutor.execute(statement, notifier, description);
            } catch (Throwable t) {
                statementExecutor.execute(new Fail(t), notifier, description);
            }
        }
    }

    private Stack<Class<?>> getClassHierarchy(final TestClass testClass) {
        final Stack<Class<?>> classHierarchy = new Stack<Class<?>>();
        for (Class<?> currentClass = testClass.getJavaClass();
             currentClass != null;
             currentClass = currentClass.getEnclosingClass())
            classHierarchy.push(currentClass);
        return classHierarchy;
    }

    private Object createTestInstance(final Stack<Class<?>> classHierarchy) throws Throwable {
        try {
            // Top level class has empty constructor
            Class<?> outerClass = classHierarchy.pop();
            Object test = outerClass.newInstance();

            // Inner class constructors require the enclosing instance
            while (!classHierarchy.empty()) {
                final Class<?> innerClass = classHierarchy.pop();
                final Constructor<?> innerConstructor = innerClass.getConstructor(outerClass);
                test = innerConstructor.newInstance(test);
                outerClass = innerClass;
            }
            return test;
        } catch (final ReflectiveOperationException e) {
            if (e instanceof InvocationTargetException)
                throw ((InvocationTargetException) e).getTargetException();
            else
                throw e;
        }
    }

    protected Statement buildStatement(final FrameworkMethod method, final Object test) {
        Statement statement = new InvokeMethod(method, test);
        statement = withExpectingExceptions(method, statement);
        statement = withPotentialTimeout(method, statement);

        try {
            final List<TestRule> testRules = new ArrayList<TestRule>();
            final List<MethodRule> methodRules = new ArrayList<MethodRule>();

            for (Object instance = test; instance != null; instance = getEnclosingInstance(instance)) {
                final TestClass testClass = TestClassPool.forClass(instance.getClass());
                statement = withBefores(method, testClass, instance, statement);
                statement = withAfters(method, testClass, instance, statement);

                testRules.addAll(testClass.getAnnotatedMethodValues(instance, Rule.class, TestRule.class));
                testRules.addAll(testClass.getAnnotatedFieldValues(instance, Rule.class, TestRule.class));
                methodRules.addAll(testClass.getAnnotatedFieldValues(instance, Rule.class, MethodRule.class));
            }

            statement = withRules(method, test, statement, testRules, methodRules);
        } catch (final IllegalAccessException e) {
            statement = new Fail(e);
        }

        return statement;
    }

    protected Statement withExpectingExceptions(final FrameworkMethod method, final Statement next) {
        final Test annotation = method.getAnnotation(Test.class);
        return annotation.expected() == Test.None.class ? next : new ExpectException(next, annotation.expected());
    }

    protected Statement withPotentialTimeout(final FrameworkMethod method, final Statement next) {
        final Test annotation = method.getAnnotation(Test.class);
        return annotation.timeout() <= 0 ? next : new FailOnTimeout(next, annotation.timeout());
    }

    protected Statement withBefores(final FrameworkMethod method, final TestClass testClass,
                                    final Object target, final Statement next) {
        final List<FrameworkMethod> befores = testClass.getAnnotatedMethods(Before.class);
        return befores.isEmpty() ? next : new RunBefores(next, befores, target);
    }

    protected Statement withAfters(final FrameworkMethod method, final TestClass testClass,
                                   final Object target, final Statement next) {
        final List<FrameworkMethod> afters = testClass.getAnnotatedMethods(After.class);
        return afters.isEmpty() ? next : new RunAfters(next, afters, target);
    }

    protected Statement withRules(final FrameworkMethod method, final Object target, final Statement next,
                                  final List<TestRule> testRules, final List<MethodRule> methodRules) {
        Statement statement = next;

        for (MethodRule methodRule : methodRules)
            if (!testRules.contains(methodRule))
                statement = methodRule.apply(statement, method, target);

        if (!testRules.isEmpty())
            statement = new RunRules(statement, testRules, describer.describe(method));

        return statement;
    }

    protected static Object getEnclosingInstance(final Object target) throws IllegalAccessException {
        final Class<?> targetClass = target.getClass();
        if (!targetClass.isMemberClass())
            return null;

        final Class<?> enclosingClass = targetClass.getEnclosingClass();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getType().equals(enclosingClass)) {
                field.setAccessible(true);
                return field.get(target);
            }
        }
        return null;
    }
}
