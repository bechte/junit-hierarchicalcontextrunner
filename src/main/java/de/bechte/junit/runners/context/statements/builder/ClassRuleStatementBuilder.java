package de.bechte.junit.runners.context.statements.builder;

import org.junit.ClassRule;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ClassRuleStatementBuilder} creates a {@link RunRules} statement that evaluates all {@code @ClassRule}
 * annotated members. If no such members exist, the builder will simply return the provided next {@link Statement}.
 */
public class ClassRuleStatementBuilder implements ClassStatementBuilder {
    public Statement createStatement(final TestClass testClass, final Statement next,
                                     final Description description, final RunNotifier notifier) {
        final List<TestRule> classRules = new ArrayList<TestRule>();
        classRules.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return classRules.isEmpty() ? next : new RunRules(next, classRules, description);
    }
}
