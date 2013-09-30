package de.bechte.junit.runners.scheduler;

import org.junit.runners.model.RunnerScheduler;

/**
 * The {@link DirectInvokeScheduler} is a simple {@link RunnerScheduler} implementation that runs all incoming requests
 * on demand. No scheduling is performed and all operations are immediately executed.
 *
 * Note: This feature might be go away in future release of JUnit!
 */
public class DirectInvokeScheduler implements RunnerScheduler {
    @Override
    public void schedule(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void finished() {
    }
}