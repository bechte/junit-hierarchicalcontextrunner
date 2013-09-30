package de.bechte.junit.runners.context.statements.builder;

import org.junit.BeforeClass;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link BeforeClassStatementBuilder} creates a {@link RunBefores} statement that evaluates all
 * {@code @BeforeClass} annotated methods. If no such method exist, the builder will simply return the provided next
 * {@link Statement}.
 */
public class BeforeClassStatementBuilder implements ClassStatementBuilder {
    @Override
    public Statement createStatement(final TestClass testClass, final Statement next,
                                     final Description description, final RunNotifier notifier) {
        final List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? next : new RunBefores(next, befores, null);
    }
}