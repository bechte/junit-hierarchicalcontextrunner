package de.bechte.junit.runners.context.statements;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatementExecutorTest {
    @Mock
    private Statement statement;

    @Mock
    private RunNotifier notifier;

    @Mock
    private Description description;

    private StatementExecutor statementExecutor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        statementExecutor = new StatementExecutor();
    }

    @Test
    public void verifyThatExecutorEvaluatesStatementWithoutNotifications() throws Throwable {
        statementExecutor.execute(statement, notifier, description);

        verify(statement).evaluate();
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenEvaluationFailsWithAnInitializationError_FailureIsReportedWithTheNotifier() throws Throwable {
        doThrow(new InitializationError("")).when(statement).evaluate();

        statementExecutor.execute(statement, notifier, description);

        verify(notifier).fireTestFailure(any(Failure.class));
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenEvaluationFailsWithAssumptionViolatedException_AssumptionFailureIsReportedWithTheNotifier() throws Throwable {
        doThrow(new AssumptionViolatedException("")).when(statement).evaluate();

        statementExecutor.execute(statement, notifier, description);

        verify(notifier).fireTestAssumptionFailed(any(Failure.class));
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenEvaluationFailsWithThrowable_FailureIsReportedWithTheNotifier() throws Throwable {
        doThrow(new Throwable("")).when(statement).evaluate();

        statementExecutor.execute(statement, notifier, description);

        verify(notifier).fireTestFailure(any(Failure.class));
        verifyNoMoreInteractions(notifier);
    }
}