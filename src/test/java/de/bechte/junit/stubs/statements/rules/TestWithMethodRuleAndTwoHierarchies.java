package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

public class TestWithMethodRuleAndTwoHierarchies {
    @Rule
    public MethodRule rule;

    public TestWithMethodRuleAndTwoHierarchies(CapturingMethodRuleStub capturingMethodRuleStub) {
        rule = capturingMethodRuleStub;
    }

    public class Context {
        @Test
        public void aTest() {
        }
    }
}
