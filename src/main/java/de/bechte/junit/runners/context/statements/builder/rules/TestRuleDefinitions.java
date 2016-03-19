package de.bechte.junit.runners.context.statements.builder.rules;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.List;

public class TestRuleDefinitions {
    private List<TestRuleInTestHierarchy> testRulePositionInTestHierarchies = new ArrayList<TestRuleInTestHierarchy>();
    private List<Object> hierarchyOfTestsFromLowestToHighest;

    public TestRuleDefinitions(List<Object> hierarchyOfTestsFromLowestToHighest) {
        this.hierarchyOfTestsFromLowestToHighest = hierarchyOfTestsFromLowestToHighest;
    }

    public boolean contains(MethodRule methodRule) {
        for (TestRuleInTestHierarchy t : testRulePositionInTestHierarchies) {
            if (t.getTestRule().equals(methodRule))
                return true;
        }
        return false;
    }

    public void addAll(List<TestRule> testRules, Object instance) {
        for (TestRule testRule : testRules)
            testRulePositionInTestHierarchies.add(new TestRuleInTestHierarchy(testRule, instance));
    }

    public boolean hasSome() {
        return !testRulePositionInTestHierarchies.isEmpty();
    }

    public Iterable<TestRule> getTestRulesDefinedForThisHierarchyLevel(Object instance) {
        List<TestRule> result = new ArrayList<TestRule>();
        for (TestRuleInTestHierarchy testRulePosition : testRulePositionInTestHierarchies)
            if (hierarchyOfTestsFromLowestToHighest.indexOf(testRulePosition.getObjectRepresentingHierarchyLevel()) >= hierarchyOfTestsFromLowestToHighest.indexOf(instance))
                result.add(testRulePosition.getTestRule());
        return result;
    }
}
