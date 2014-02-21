package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.bechte.junit.runners.context.description.Describer;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;

/**
 * The {@link ContextExecutor} is responsible for executing a sub-contexts, represented by the given {@link Class}.
 *
 * This implementation will create and run a new {@link HierarchicalContextRunner} for the given {@link Class}.
 */
public class ContextExecutor implements ChildExecutor<Class<?>> {
    private final Describer<Class<?>> describer;
    private StatementExecutor statementExecutor;

    public ContextExecutor(final Describer<Class<?>> describer) {
        this.describer = describer;
        this.statementExecutor = new StatementExecutor();
    }

    public void run(final TestClass testClass, final Class<?> clazz, final RunNotifier notifier) {
        try {
            new HierarchicalContextRunner(clazz).run(notifier);
        } catch (final Throwable t) {
            statementExecutor.execute(new Fail(t), notifier, describer.describe(clazz));
        }
    }
}