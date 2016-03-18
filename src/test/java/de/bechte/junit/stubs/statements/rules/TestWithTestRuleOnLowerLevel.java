package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;

public class TestWithTestRuleOnLowerLevel {

    public class Context {

        public Context(CapturingTestRuleStub rule) {
            this.rule = rule;
        }

        @Rule
        public CapturingTestRuleStub rule;

        @Test
        public void aTest() throws Exception {
        }
    }
}
