package de.bechte.junit.stubs.statements.rules;

import org.junit.Rule;
import org.junit.Test;

public class TestWithMethodRuleOnHighestLevelWithoutInnerContexts {
    @Rule
    public CapturingMethodRuleStub rule;

    public TestWithMethodRuleOnHighestLevelWithoutInnerContexts(CapturingMethodRuleStub capturingMethodRuleStub) {
        rule = capturingMethodRuleStub;
    }

    @Test
    public void aTest() {
    }
}
