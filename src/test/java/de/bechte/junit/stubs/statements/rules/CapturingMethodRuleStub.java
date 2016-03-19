package de.bechte.junit.stubs.statements.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

public class CapturingMethodRuleStub implements MethodRule {
    private int numberOfApplications;
    private boolean statementWasEvaluated;
    private List<ApplyInvocationParameter> applyInvocationParameters = new ArrayList<ApplyInvocationParameter>();

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        applyInvocationParameters.add(new ApplyInvocationParameter(base, method, target));
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

    public List<ApplyInvocationParameter> getApplyInvocationParameters() {
        return applyInvocationParameters;
    }

    public class ApplyInvocationParameter {
        private final Statement statementApplyWasCalledWith;
        private final FrameworkMethod frameworkMethodApplyWasCalledWith;
        private final Object targetApplyWasCalledWith;

        public ApplyInvocationParameter(Statement statementApplyWasCalledWith, FrameworkMethod frameworkMethodApplyWasCalledWith, Object targetApplyWasCalledWith) {
            this.statementApplyWasCalledWith = statementApplyWasCalledWith;
            this.frameworkMethodApplyWasCalledWith = frameworkMethodApplyWasCalledWith;
            this.targetApplyWasCalledWith = targetApplyWasCalledWith;
        }

        public Statement getStatementApplyWasCalledWith() {
            return statementApplyWasCalledWith;
        }

        public FrameworkMethod getFrameworkMethodApplyWasCalledWith() {
            return frameworkMethodApplyWasCalledWith;
        }

        public Object getTargetApplyWasCalledWith() {
            return targetApplyWasCalledWith;
        }
    }
}
