package de.bechte.junit.runners.context.statements.builder;

import de.bechte.junit.runners.model.TestClassPool;
import org.junit.Rule;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.LinkedList;
import java.util.List;

import static de.bechte.junit.runners.util.ReflectionUtil.getEnclosingInstance;

/**
 * The {@link HierarchicalRunRulesStatementBuilder} creates {@link RunRules} statements for all {@code @Rule} annotated
 * fields and methods along the context hierarchy. If no rules exist, the builder returns the provided next
 * {@link Statement}.
 */
public class HierarchicalRunRulesStatementBuilder implements MethodStatementBuilder {
    public Statement createStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                     final Statement next, final Description description, final RunNotifier notifier) {
        try {
            final List<TestRule> testRules = new LinkedList<TestRule>();
            final List<MethodRule> methodRules = new LinkedList<MethodRule>();

            for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
                final TestClass instanceTestClass = TestClassPool.forClass(instance.getClass());
                testRules.addAll(instanceTestClass.getAnnotatedFieldValues(instance, Rule.class, TestRule.class));
                methodRules.addAll(instanceTestClass.getAnnotatedFieldValues(instance, Rule.class, MethodRule.class));
            }

            Statement statement = next;
            for (MethodRule methodRule : methodRules)
                if (!testRules.contains(methodRule))
                    statement = methodRule.apply(statement, method, target);
            if (!testRules.isEmpty())
                statement = new RunRules(statement, testRules, description);
            return statement;
        } catch (final IllegalAccessException e) {
            return new Fail(e);
        }
    }
}