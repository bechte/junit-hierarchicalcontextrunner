package de.bechte.junit.runners.context.statements.builder;

import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * The {@link ExpectExceptionStatementBuilder} creates a {@link ExpectException} statement that verifies that the
 * expected {@link Exception} within the {@code @Test} annotation has been thrown. If no {@link Exception} is expected,
 * the builder returns the provided next {@link Statement}.
 *
 * @deprecated The {@code expected} field of the {@code @Test} annotation is deprecated. Therefore, this builder is
 * also marked as deprecated. Please make use of the {@code @Rule} annotation.
 */
@Deprecated
public class ExpectExceptionStatementBuilder implements MethodStatementBuilder {
    @Override
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier) {
        final Test annotation = method.getAnnotation(Test.class);
        return annotation.expected() == Test.None.class ? next : new ExpectException(next, annotation.expected());
    }
}