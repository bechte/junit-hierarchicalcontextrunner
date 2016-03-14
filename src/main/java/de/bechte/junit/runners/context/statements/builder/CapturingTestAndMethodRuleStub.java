package de.bechte.junit.runners.context.statements.builder;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class CapturingTestAndMethodRuleStub implements TestRule, MethodRule {

    private int numberOfApplicationsOfTestRulesApplyMethod;
    private Statement statementTestRuleApplyWasCalledWith;
    private Description descriptionTestRuleApplyWasCalledWith;
    private boolean statementWasEvaluated;

    // apply from TestRule
    public Statement apply(Statement base, Description description) {
        this.statementTestRuleApplyWasCalledWith = base;
        this.descriptionTestRuleApplyWasCalledWith = description;
        numberOfApplicationsOfTestRulesApplyMethod++;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                statementWasEvaluated = true;
            }
        };
    }

    // apply from MethodRule
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        throw new RuntimeException("MethodRule#apply should not be called.");
    }

    public int getNumberOfApplicationsOfTestRulesApplyMethod() {
        return numberOfApplicationsOfTestRulesApplyMethod;
    }

    public Statement getStatementTestRuleApplyWasCalledWith() {
        return statementTestRuleApplyWasCalledWith;
    }

    public Description getDescriptionTestRuleApplyWasCalledWith() {
        return descriptionTestRuleApplyWasCalledWith;
    }

    public void setDescriptionTestRuleApplyWasCalledWith(Description descriptionTestRuleApplyWasCalledWith) {
        this.descriptionTestRuleApplyWasCalledWith = descriptionTestRuleApplyWasCalledWith;
    }

    public boolean statementReturnedByRuleApplyMethodWasEvaluated() {
        return statementWasEvaluated;
    }
}
