package de.bechte.junit.runners.context.statements.builder;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class CapturingTestRuleStub implements TestRule {

    private int numberOfApplications;
    private Statement statementAppliedWasCalledWith;
    private Description descriptionApplyWasCalledWith;
    private boolean statementWasEvaluated;

    public Statement apply(Statement base, Description description) {
        statementAppliedWasCalledWith = base;
        descriptionApplyWasCalledWith = description;
        numberOfApplications++;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                statementWasEvaluated = true;
            }
        };
    }

    public int getNumberOfApplications() {
        return numberOfApplications;
    }

    public Statement getStatementAppliedWasCalledWith() {
        return statementAppliedWasCalledWith;
    }

    public Description getDescriptionApplyWasCalledWith() {
        return descriptionApplyWasCalledWith;
    }

    public boolean statementReturnedByRuleApplyMethodWasEvaluated() {
        return statementWasEvaluated;
    }
}
