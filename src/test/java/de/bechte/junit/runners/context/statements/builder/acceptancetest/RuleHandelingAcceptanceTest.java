package de.bechte.junit.runners.context.statements.builder.acceptancetest;

import de.bechte.junit.stubs.statements.rules.acceptancetest.TestClassWithMethodRuleAndInnerContext;
import de.bechte.junit.stubs.statements.rules.acceptancetest.TestClassWithMethodRuleDefinedOnInnerContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class RuleHandelingAcceptanceTest {
    @Test
    public void methodRulesAreAppliedToContextsDeeperInHierarchy() throws Exception {
        JUnitCore.runClasses(TestClassWithMethodRuleAndInnerContext.class);
    }

    @Test
    @Ignore
    // currently fails because rules are applied to all hierarchies
    public void methodRuleShouldNotBeAppliedOnHierarchyLevelsAboveTheLevelTheRuleIsDeclared() throws Exception {
        JUnitCore.runClasses(TestClassWithMethodRuleDefinedOnInnerContext.class);
    }
}
