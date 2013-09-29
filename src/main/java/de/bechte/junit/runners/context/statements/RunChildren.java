package de.bechte.junit.runners.context.statements;

import de.bechte.junit.runners.context.processing.ChildExecutor;
import de.bechte.junit.runners.context.processing.ChildResolver;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link RunChildren} statement takes four arguments:
 * {@link TestClass}, {@link ChildExecutor}, {@link List} of children, {@link RunNotifier}
 *
 * When evaluated, the statement calls {@link ChildExecutor#run(TestClass, Object, RunNotifier)} on each child.
 *
 * Note: The type of the {@link ChildExecutor} instance must match the type of the {@code List} of children.
 *
 * @param <T> the type to run
 */
public class RunChildren<T extends Object> extends Statement {
    private final TestClass testClass;
    private final ChildExecutor<T> childExecutor;
    private final ChildResolver<T> childResolver;
    private final RunNotifier notifier;

    public RunChildren(final TestClass testClass, final ChildExecutor<T> childExecutor,
                       final ChildResolver<T> childResolver, final RunNotifier notifier) {
        this.testClass = testClass;
        this.childExecutor = childExecutor;
        this.childResolver = childResolver;
        this.notifier = notifier;
    }

    @Override
    public void evaluate() throws Throwable {
        for (final T child : childResolver.getChildren(testClass))
            childExecutor.run(testClass, child, notifier);
    }
}