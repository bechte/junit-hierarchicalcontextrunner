package de.bechte.junit.runners.context.statements;

import org.junit.runners.model.Statement;

/**
 * The {@link RunAll} statement takes several statements and executes them all in the given order. It is used to group
 * on ore more statements together. Example:
 *
 * Given three statements A, B, and C.
 *
 * <code>
 *   new RunAll(A, B, C).evaluate();
 * </code>
 *
 * This will evaluate all statements A, B, and C in sequence.
 */
public class RunAll extends Statement {
    private final Statement[] statements;

    public RunAll(Statement... statements) {
        this.statements = (statements == null) ? new Statement[0] : statements;
    }

    @Override
    public void evaluate() throws Throwable {
        for (Statement statement : statements)
            statement.evaluate();
    }
}
