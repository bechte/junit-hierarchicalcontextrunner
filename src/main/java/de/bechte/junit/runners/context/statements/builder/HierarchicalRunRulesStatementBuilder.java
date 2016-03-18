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

import java.util.ArrayList;
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
            final TestRuleDefinitions testRules = new TestRuleDefinitions();
            final List<MethodRule> methodRules = new LinkedList<MethodRule>();

            List<Object> hierarchyInstancesInAscendingOrder = hierarchyInstancesInAscendingOrder(target);

            for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
                final TestClass instanceTestClass = TestClassPool.forClass(instance.getClass());
                testRules.addAll(instanceTestClass.getAnnotatedMethodValues(instance, Rule.class, TestRule.class), instance);
                testRules.addAll(instanceTestClass.getAnnotatedFieldValues(instance, Rule.class, TestRule.class), instance);
                methodRules.addAll(instanceTestClass.getAnnotatedFieldValues(instance, Rule.class, MethodRule.class));
            }

            Statement statement = next;
            for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
                for (MethodRule methodRule : methodRules)
                    if (!testRules.contains(methodRule)) {
                        statement = methodRule.apply(statement, method, instance);
                    }
                if (testRules.hasSome())
                    statement = new RunRules(statement, testRules.getTestRulesDefinedForThisHieraryLevel(hierarchyInstancesInAscendingOrder, instance), description);
            }
            return statement;
        } catch (final IllegalAccessException e) {
            return new Fail(e);
        }
    }

    private List<Object> hierarchyInstancesInAscendingOrder(Object target) throws IllegalAccessException {
        List<Object> result = new ArrayList<Object>();
        for (Object instance = target; instance != null; instance = getEnclosingInstance(instance)) {
            result.add(instance);
        }
        return result;
    }

    private class TestRuleWithInstance {
        private TestRule testRule;
        private Object instance;

        public TestRuleWithInstance(TestRule testRule, Object instance) {
            this.testRule = testRule;
            this.instance = instance;
        }
    }

    private class TestRuleDefinitions {
        private List<TestRuleWithInstance> testRuleWithInstances = new ArrayList<TestRuleWithInstance>();

        public boolean contains(MethodRule methodRule) {
            for (TestRuleWithInstance t : testRuleWithInstances) {
                if (t.testRule.equals(methodRule))
                    return true;
            }
            return false;
        }

        public void addAll(List<TestRule> testRules, Object instance) {
            for (TestRule testRule : testRules)
                testRuleWithInstances.add(new TestRuleWithInstance(testRule, instance));
        }

        public boolean hasSome() {
            return !testRuleWithInstances.isEmpty();
        }

        public Iterable<TestRule> getTestRulesDefinedForThisHieraryLevel(List<Object> hierarchyInstancesInAscendingOrder, Object instance) {
            List<TestRule> result = new ArrayList<TestRule>();
            for (TestRuleWithInstance t : testRuleWithInstances)
                if (hierarchyInstancesInAscendingOrder.indexOf(t.instance) >= hierarchyInstancesInAscendingOrder.indexOf(instance))
                    result.add(t.testRule);
            return result;
        }
    }
}
