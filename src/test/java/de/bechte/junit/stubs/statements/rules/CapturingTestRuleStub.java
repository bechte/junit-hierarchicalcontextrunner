package de.bechte.junit.stubs.statements.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Collection;

public class CapturingTestRuleStub implements TestRule {

    private int numberOfApplications;
    private Collection<ApplyMethodParameter> applyMethodParameters = new ArrayList<ApplyMethodParameter>();
    private boolean statementWasEvaluated;

    public Statement apply(Statement base, Description description) {
        applyMethodParameters.add(new ApplyMethodParameter(base, description));
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

    public boolean statementReturnedByRuleApplyMethodWasEvaluated() {
        return statementWasEvaluated;
    }

    public Collection<ApplyMethodParameter> getApplyMethodParameters() {
        return applyMethodParameters;
    }

    public class ApplyMethodParameter {
        private Statement statementAppliedWasCalledWith;
        private Description descriptionApplyWasCalledWith;

        public ApplyMethodParameter(Statement statementAppliedWasCalledWith, Description descriptionApplyWasCalledWith) {
            this.statementAppliedWasCalledWith = statementAppliedWasCalledWith;
            this.descriptionApplyWasCalledWith = descriptionApplyWasCalledWith;
        }

        public Statement getStatementAppliedWasCalledWith() {
            return statementAppliedWasCalledWith;
        }

        public Description getDescriptionApplyWasCalledWith() {
            return descriptionApplyWasCalledWith;
        }
    }
}
