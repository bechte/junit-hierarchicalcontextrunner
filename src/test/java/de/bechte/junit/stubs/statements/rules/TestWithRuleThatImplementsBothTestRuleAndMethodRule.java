package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;

public class TestWithRuleThatImplementsBothTestRuleAndMethodRule {
    @Rule
    public CapturingTestAndMethodRuleStub rule;

    public TestWithRuleThatImplementsBothTestRuleAndMethodRule(CapturingTestAndMethodRuleStub capturingTestAndMethodRuleStub) {
        rule = capturingTestAndMethodRuleStub;
    }

    @Test
    public void aTest() {
    }
}
