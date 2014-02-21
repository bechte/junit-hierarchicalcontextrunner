package de.bechte.junit.runners.context.statements.builder;

import de.bechte.junit.runners.model.TestClassPool;
import org.junit.After;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

import static de.bechte.junit.runners.util.ReflectionUtil.getEnclosingInstance;

/**
 * The {@link HierarchicalRunAfterStatementBuilder} creates a {@link RunAfters} statement for each context hierarchy
 * level that evaluates all {@code @After} annotated methods on that level. If no such method exist one (or even all)
 * hierarchy levels is skipped and the builder returns the current (or provided) next {@link Statement}.
 */
public class HierarchicalRunAfterStatementBuilder implements MethodStatementBuilder {
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier) {
        Statement statement = next;
        try {
            for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
                final TestClass instanceTestClass = TestClassPool.forClass(instance.getClass());
                final List<FrameworkMethod> afterMethods = instanceTestClass.getAnnotatedMethods(After.class);
                statement = afterMethods.isEmpty() ? statement : new RunAfters(statement, afterMethods, instance);
            }
        } catch (final IllegalAccessException e) {
            statement = new Fail(e);
        }
        return statement;
    }
}