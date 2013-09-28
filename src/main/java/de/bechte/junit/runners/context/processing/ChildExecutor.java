package de.bechte.junit.runners.context.processing;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;

/**
 * A {@link ChildExecutor} is responsible for executing all tests for the given object. Typically, the
 * {@link ChildExecutor} is used together with the {@link ChildResolver}.
 *
 * @param <T> The type of object that tests get executed by the {@link ChildExecutor}.
 */
public interface ChildExecutor<T extends Object> {
    /**
     * Runs the given object of type T and informs the {@link RunNotifier} about all notifications.
     *
     * @param testClass the corresponding {@link TestClass}
     * @param object the object of type T
     * @param notifier the {@link RunNotifier} to be notified
     */
    public abstract void run(final TestClass testClass, final T object, final RunNotifier notifier);
}
