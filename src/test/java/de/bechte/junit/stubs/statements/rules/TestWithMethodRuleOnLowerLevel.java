package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;

public class TestWithMethodRuleOnLowerLevel {

    public class Context {

        public Context(CapturingMethodRuleStub rule) {
            this.rule = rule;
        }

        @Rule
        public CapturingMethodRuleStub rule;

        @Test
        public void aTest() throws Exception {
        }
    }
}
