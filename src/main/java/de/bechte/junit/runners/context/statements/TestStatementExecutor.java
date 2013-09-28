package de.bechte.junit.runners.context.statements;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;

/**
 * The {@link TestStatementExecutor} extends the {@link StatementExecutor}, providing additional notifications when
 * the test is started and finished.
 *
 * @see StatementExecutor
 */
public class TestStatementExecutor extends StatementExecutor {
    @Override
    protected void beforeExecution(EachTestNotifier notifier, Description description) {
        notifier.fireTestStarted();
    }

    @Override
    protected void afterExecution(EachTestNotifier notifier, Description description) {
        notifier.fireTestFinished();
    }
}