package de.bechte.junit.runners.context.statements.builder;

import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * The {@link FailOnTimeoutStatementBuilder} creates a {@link FailOnTimeout} statement for the timeout given in the
 * {@code @Test} annotation. If no timeout is specified, the builder returns the provided next {@link Statement}.

 * @deprecated The {@code timeout} field of the {@code @Test} annotation is deprecated. Therefore, this builder is
 * also marked as deprecated. Please make use of the {@code @Rule} annotation.
 */
@Deprecated
public class FailOnTimeoutStatementBuilder implements MethodStatementBuilder {
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier) {
        final Test annotation = method.getAnnotation(Test.class);
        return annotation.timeout() <= 0 ? next : new FailOnTimeout(next, annotation.timeout());
    }
}