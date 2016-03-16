package de.bechte.junit.stubs.statements.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class CapturingMethodRuleStub implements MethodRule {
    private int numberOfApplications;
    private Statement statementApplyWasCalledWith;
    private FrameworkMethod frameworkMethodApplyWasCalledWith;
    private Object targetApplyWasCalledWith;
    private boolean statementWasEvaluated;

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        this.statementApplyWasCalledWith = base;
        this.frameworkMethodApplyWasCalledWith = method;
        targetApplyWasCalledWith = target;
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

    public Object getTargetApplyWasCalledWith() {
        return targetApplyWasCalledWith;
    }

    public Statement getStatementApplyWasCalledWith() {
        return statementApplyWasCalledWith;
    }

    public FrameworkMethod getFrameworkMethodApplyWasCalledWith() {
        return frameworkMethodApplyWasCalledWith;
    }

    public boolean statementReturnedByRuleApplyMethodWasEvaluated() {
        return statementWasEvaluated;
    }
}
