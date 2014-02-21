package de.bechte.junit.runners.context.statements.builder;

import org.junit.AfterClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link AfterClassStatementBuilder} creates a {@link RunAfters} statement that evaluates all {@code @AfterClass}
 * annotated methods. If no such method exist, the builder will simply return the provided next {@link Statement}.
 */
public class AfterClassStatementBuilder implements ClassStatementBuilder {
    public Statement createStatement(final TestClass testClass, final Statement next,
                                     final Description description, final RunNotifier notifier) {
        final List<FrameworkMethod> afters = testClass.getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? next : new RunAfters(next, afters, null);
    }
}