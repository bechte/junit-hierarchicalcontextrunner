package de.bechte.junit.runners.context.statements.builder.rules;

import org.junit.rules.TestRule;

public class TestRuleInTestHierarchy {
    private TestRule testRule;
    private Object objectRepresentingHierarchy;

    public TestRuleInTestHierarchy(TestRule testRule, Object objectRepresentingHierarchy) {
        this.testRule = testRule;
        this.objectRepresentingHierarchy = objectRepresentingHierarchy;
    }

    public TestRule getTestRule() {
        return testRule;
    }

    public Object getObjectRepresentingHierarchyLevel() {
        return objectRepresentingHierarchy;
    }
}
