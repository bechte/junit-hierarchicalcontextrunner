package de.bechte.junit.runners.context.statements;

import org.junit.internal.runners.model.EachTestNotifier;

/**
 * The {@link MethodStatementExecutor} extends the {@link StatementExecutor}, providing additional notifications when
 * the test is started and finished.
 *
 * @see StatementExecutor
 */
public class MethodStatementExecutor extends StatementExecutor {
    @Override
    protected void beforeExecution(final EachTestNotifier notifier) {
        notifier.fireTestStarted();
    }

    @Override
    protected void afterExecution(final EachTestNotifier notifier) {
        notifier.fireTestFinished();
    }
}