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
 * {@link #beforeExecution(EachTestNotifier)}
 * {@link #afterExecution(EachTestNotifier)}
 *
 * @see MethodStatementExecutor
 */
public class StatementExecutor {
    public void execute(final Statement statement, final RunNotifier notifier, final Description description) {
        final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        try {
            beforeExecution(eachNotifier);
            statement.evaluate();
        } catch (final InitializationError e) {
            whenInitializationErrorIsRaised(eachNotifier, e);
        } catch (final AssumptionViolatedException e) {
            whenAssumptionViolatedExceptionIsRaised(eachNotifier, e);
        } catch (final Throwable e) {
            whenThrowableIsRaised(eachNotifier, e);
        } finally {
            afterExecution(eachNotifier);
        }
    }

    /**
     * Clients may override this method to add additional behavior prior to the execution of the statement.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     */
    protected void beforeExecution(final EachTestNotifier notifier) {
    }

    /**
     * Clients may override this method to add additional behavior when a {@link InitializationError} is raised.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     * @param e the error
     */
    protected void whenInitializationErrorIsRaised(final EachTestNotifier notifier, final InitializationError e) {
        notifier.addFailure(new MultipleFailureException(e.getCauses()));
    }

    /**
     * Clients may override this method to add additional behavior when a {@link AssumptionViolatedException} is raised.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     * @param e the error
     */
    protected void whenAssumptionViolatedExceptionIsRaised(final EachTestNotifier notifier, final AssumptionViolatedException e) {
        notifier.addFailedAssumption(e);
    }

    /**
     * Clients may override this method to add additional behavior when a {@link Throwable} is raised.
     *
     * @param notifier the notifier
     * @param e the error
     */
    protected void whenThrowableIsRaised(final EachTestNotifier notifier, final Throwable e) {
        notifier.addFailure(e);
    }

    /**
     * Clients may override this method to add additional behavior after the execution of the statement.
     * The call of this method is guaranteed.
     *
     * @param notifier the notifier
     */
    protected void afterExecution(final EachTestNotifier notifier) {
    }
}
