package de.bechte.junit.runners.context.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * The {@link StatementExecutor} evaluates the given {@link Statement} and notifies the {@link RunNotifier} about all
 * events that occur during execution. It also handles the errors that might be thrown during execution.
 *
 * One can easily provide additional notifications by overriding the {@link StatementExecutor} and providing an
 * implementation for the template methods:
 *
 * {@link #beforeExecution(EachTestNotifier, Description)}
 * {@link #afterExecution(EachTestNotifier, Description)}
 *
 * @see MethodStatementExecutor
 */
public class StatementExecutor {
    public void execute(final Statement statement, final RunNotifier notifier, final Description description) {
        final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        try {
            beforeExecution(eachNotifier, description);
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (final InitializationError e) {
            eachNotifier.addFailure(new MultipleFailureException(e.getCauses()));
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            afterExecution(eachNotifier, description);
        }
    }

    /**
     * Clients may override this method to add additional behavior prior to the execution of the statement.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     * @param description the description
     */
    protected void beforeExecution(final EachTestNotifier notifier, final Description description) {
    }

    /**
     * Clients may override this method to add additional behavior after the execution of the statement.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     * @param description the description
     */
    protected void afterExecution(final EachTestNotifier notifier, final Description description) {
    }
}
