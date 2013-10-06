package de.bechte.junit.runners.context.statements;

import org.junit.Test;
import org.junit.runners.model.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RunAllTest {
    @Test
    public void verifyAllStatementsAreEvaluated() throws Throwable {
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);

        RunAll runAll = new RunAll(statement1, statement2, statement3);

        runAll.evaluate();

        verify(statement1).evaluate();
        verify(statement2).evaluate();
        verify(statement3).evaluate();
    }
}
