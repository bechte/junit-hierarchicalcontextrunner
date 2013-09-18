package de.bechte.junit.runners.context;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class HierarchicalContextRunner extends ParentRunner<Class<?>> {
    public HierarchicalContextRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<Class<?>> getChildren() {
        final Class<?>[] declaredClasses = getTestClass().getJavaClass().getDeclaredClasses();
        final List<Class<?>> children = new ArrayList<Class<?>>(declaredClasses.length);

        for (final Class<?> child : declaredClasses)
            if (!Modifier.isStatic(child.getModifiers()))
                children.add(child);

        return children;
    }

    @Override
    protected Description describeChild(final Class<?> tClass) {
        try {
            // Retrieve the description from the runner
            return new HierarchicalContextRunner(tClass).getDescription();
        } catch (InitializationError e) {
            return Description.createTestDescription(tClass, tClass.getName(), tClass.getAnnotations());
        }
    }

    @Override
    protected void runChild(final Class<?> tClass, final RunNotifier runNotifier) {
        try {
            new HierarchicalContextRunner(tClass).run(runNotifier);
        } catch (InitializationError e) {
            runNotifier.fireTestFailure(new Failure(getDescription(), e));
        }
    }

    @Override
    protected String getName() {
        return getTestClass().getJavaClass().getSimpleName();
    }

    @Override
    public Description getDescription() {
        Runner runner;
        try {
            runner = getRunnerForContextHierarchyLevel(getTestClass());
        } catch (InitializationError e) {
            runner = null;
        }

        final Description description = (runner != null)
                ? runner.getDescription()
                : Description.createSuiteDescription(getName(), getRunnerAnnotations());
        for (Class<?> child : getChildren())
            description.addChild(describeChild(child));
        return description;
    }

    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        // Wrap children statement with tests for current hierarchy
        final Statement next = super.childrenInvoker(notifier);
        return withTestsForCurrentContextHierarchyLevel(next, notifier);
    }

    private Statement withTestsForCurrentContextHierarchyLevel(final Statement next, final RunNotifier notifier) {
        try {
            final Runner runner = getRunnerForContextHierarchyLevel(getTestClass());
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    if (runner != null)
                        runner.run(notifier);
                    next.evaluate();
                }
            };
        } catch (InitializationError e) {
            return new Fail(e);
        }
    }

    /**
     * Returns a {@link Runner} for evaluating the tests of the given context
     * class within the hierarchy level. The current implementation uses the
     * {@link ContextHierarchyLevelClassRunner}. This method may return null,
     * if there are no tests to run within the context hierarchy level.
     *
     * @param tClass the {@link TestClass} under test
     * @return a {@link Runner} instance or null
     * @throws InitializationError if the runner cannot be initialized
     */
    protected Runner getRunnerForContextHierarchyLevel(final TestClass tClass) throws InitializationError {
        if (!tClass.getAnnotatedMethods(Test.class).isEmpty())
            return new ContextHierarchyLevelClassRunner(tClass.getJavaClass());
        else
            return null;
    }
}
