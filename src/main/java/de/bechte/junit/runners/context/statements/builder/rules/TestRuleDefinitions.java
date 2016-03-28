package de.bechte.junit.runners.context.statements.builder.rules;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.List;

public class TestRuleDefinitions {
    private List<TestRuleInTestHierarchy> testRulePositionInTestHierarchies = new ArrayList<TestRuleInTestHierarchy>();
    private List<MethodRuleInTestHierarchy> methodRulePositionInTestHierarchies = new ArrayList<MethodRuleInTestHierarchy>();
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

    public void addTestRules(List<TestRule> testRules, Object instance) {
        for (TestRule testRule : testRules)
            testRulePositionInTestHierarchies.add(new TestRuleInTestHierarchy(testRule, instance));
    }

    public void addMethodRules(List<MethodRule> annotatedFieldValues, Object instance) {
        for (MethodRule methodRule : annotatedFieldValues)
            methodRulePositionInTestHierarchies.add(new MethodRuleInTestHierarchy(methodRule, instance));
    }

    public boolean testRulesPresent() {
        return !testRulePositionInTestHierarchies.isEmpty();
    }

    public Iterable<TestRule> getTestRulesDefinedForThisHierarchyLevel(Object instance) {
        List<TestRule> result = new ArrayList<TestRule>();
        for (TestRuleInTestHierarchy testRulePosition : testRulePositionInTestHierarchies)
            if (hierarchyOfTestsFromLowestToHighest.indexOf(testRulePosition.getObjectRepresentingHierarchyLevel()) >= hierarchyOfTestsFromLowestToHighest.indexOf(instance))
                result.add(testRulePosition.getTestRule());
        return result;
    }

    public List<MethodRule> getMethodRulesDefinedForThisHierarchyLevel(Object hierarchyContext) {
        List<MethodRule> result = new ArrayList<MethodRule>();
        for (MethodRuleInTestHierarchy testRulePosition : methodRulePositionInTestHierarchies)
            if (hierarchyOfTestsFromLowestToHighest.indexOf(testRulePosition.getObjectRepresentingHierarchyLevel()) >= hierarchyOfTestsFromLowestToHighest.indexOf(hierarchyContext))
                result.add(testRulePosition.getMethodRule());
        return result;
    }
}
