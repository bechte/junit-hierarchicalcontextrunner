package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;

public class TestWithTestRuleOnHighestLevel {
    @Rule
    public CapturingTestRuleStub rule;

    public TestWithTestRuleOnHighestLevel(CapturingTestRuleStub capturingTestRuleStub) {
        rule = capturingTestRuleStub;
    }

    public class Context {
        @Test
        public void aTest() throws Exception {
        }
    }
}
