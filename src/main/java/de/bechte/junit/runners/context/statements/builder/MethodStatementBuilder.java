package de.bechte.junit.runners.context.statements.builder;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * The {@link MethodStatementBuilder} interfaces provides a simple Builder mechanism, used to create {@link Statement}s.
 * The interface allows that all builders can be processed in a common manner.
 */
public interface MethodStatementBuilder {
    /**
     * Creates a {@link Statement} for the given attributes.
     *
     * @param testClass the {@link TestClass} to create the {@link Statement} for
     * @param method the {@link FrameworkMethod} to create the {@link Statement} for
     * @param next the next {@link Statement} to evaluate after the new {@link Statement} has been processed
     * @param description the {@link Description} that should be used with the {@link RunNotifier}
     * @param notifier the {@link RunNotifier} to be used to notify about events
     * @return the created {@link Statement} (must not be {@code null}!)
     */
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier);
}