package de.bechte.junit.runners.context.statements.builder;

import de.bechte.junit.runners.model.TestClassPool;
import org.junit.Before;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

import static de.bechte.junit.runners.util.ReflectionUtil.getEnclosingInstance;

/**
 * The {@link HierarchicalRunBeforeStatementBuilder} creates a {@link RunBefores} statement for each context hierarchy
 * level that evaluates all {@code @Before} annotated methods on that level. If no such method exist one (or even all)
 * hierarchy levels is skipped and the builder returns the current (or provided) next {@link Statement}.
 */
public class HierarchicalRunBeforeStatementBuilder implements MethodStatementBuilder {
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier) {
        Statement statement = next;
        try {
            for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
                final TestClass instanceTestClass = TestClassPool.forClass(instance.getClass());
                final List<FrameworkMethod> beforeMethods = instanceTestClass.getAnnotatedMethods(Before.class);
                statement = beforeMethods.isEmpty() ? statement : new RunBefores(statement, beforeMethods, instance);
            }
        } catch (final IllegalAccessException e) {
            statement = new Fail(e);
        }
        return statement;
    }
}