package de.bechte.junit.runners.context.statements;

import de.bechte.junit.runners.context.statements.MethodStatementExecutor;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import de.bechte.junit.runners.context.statements.StatementExecutorFactory;

/**
 * The {@link StatementExecutorFactory} resolves the {@link StatementExecutor}s for classes and methods. These two
 * types might differ, as they require different reporting.
 *
 * The default implementation returns:
 * the {@link StatementExecutor} for classes,
 * the {@link MethodStatementExecutor} for methods.
 */
public class DefaultStatementExecutorFactory extends StatementExecutorFactory {
    private final StatementExecutor statementExecutor;
    private final MethodStatementExecutor methodStatementExecutor;

    public DefaultStatementExecutorFactory() {
        super();
        statementExecutor = new StatementExecutor();
        methodStatementExecutor = new MethodStatementExecutor();
    }

    @Override
    public StatementExecutor getExecutorForClasses() {
        return statementExecutor;
    }

    @Override
    public StatementExecutor getExecutorForMethods() {
        return methodStatementExecutor;
    }
}
