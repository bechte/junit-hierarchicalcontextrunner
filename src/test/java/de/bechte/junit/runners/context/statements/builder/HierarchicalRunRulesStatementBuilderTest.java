package de.bechte.junit.runners.context.statements.builder;

import de.bechte.junit.stubs.statements.rules.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class HierarchicalRunRulesStatementBuilderTest {
    private final Statement nextStatement = mock(Statement.class);
    private final RunNotifier runNotifier = mock(RunNotifier.class);
    private final FrameworkMethod frameworkMethod = mock(FrameworkMethod.class);

    private final HierarchicalRunRulesStatementBuilder hierarchicalRunRulesStatementBuilder = new HierarchicalRunRulesStatementBuilder();

    @Test
    public void whenNoRulesArePresentInTestTheNextStatementRemainsUnwrapped() throws Exception {
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithoutRule.class), mock(FrameworkMethod.class), new TestWithoutRule(), nextStatement, Description.createTestDescription(TestWithoutRule.class, "Desc"), runNotifier);

        assertThat(statement, is(nextStatement));
    }

    @Test
    public void whenTestRuleIsPresent() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithTestRuleOnHighestLevel.class, "Test with TestRule");

        CapturingTestRuleStub capturingTestRuleStub = new CapturingTestRuleStub();
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithTestRuleOnHighestLevel.class), mock(FrameworkMethod.class), new TestWithTestRuleOnHighestLevel(capturingTestRuleStub), nextStatement, testDescription, runNotifier);

        assertThat(capturingTestRuleStub.getNumberOfApplications(), is(1));
        assertThat(capturingTestRuleStub.getDescriptionApplyWasCalledWith(), is(testDescription));
        assertThat(capturingTestRuleStub.getStatementAppliedWasCalledWith(), is(nextStatement));

        statement.evaluate();

        assertThat(capturingTestRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    @Test
    public void methodRuleIsPresentOnHighestLevelAndTestClassHasNoInnerContexts() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithMethodRuleOnHighestLevelWithoutInnerContexts.class, "Test with MethodRule");
        CapturingMethodRuleStub capturingMethodRuleStub = new CapturingMethodRuleStub();

        Object target = new TestWithMethodRuleOnHighestLevelWithoutInnerContexts(capturingMethodRuleStub);
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithMethodRuleOnHighestLevelWithoutInnerContexts.class), frameworkMethod, target, nextStatement, testDescription, runNotifier);

        assertThat(capturingMethodRuleStub.getNumberOfApplications(), is(1));
        assertThat(capturingMethodRuleStub.getTargetApplyWasCalledWith(), is(target));
        assertThat(capturingMethodRuleStub.getFrameworkMethodApplyWasCalledWith(), is(frameworkMethod));
        assertThat(capturingMethodRuleStub.getStatementApplyWasCalledWith(), is(nextStatement));

        statement.evaluate();

        assertThat(capturingMethodRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    @Test
    public void whenRuleImplementsBothTestRuleAndMethodRule_onlyTestRuleApplyIsExecutedAndOnlyOnce() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithRuleThatImplementsBothTestRuleAndMethodRule.class, "Test with rule that implements both TestRule and MethodRule");

        CapturingTestAndMethodRuleStub capturingTestAndMethodRuleStub = new CapturingTestAndMethodRuleStub();
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithRuleThatImplementsBothTestRuleAndMethodRule.class), frameworkMethod, new TestWithRuleThatImplementsBothTestRuleAndMethodRule(capturingTestAndMethodRuleStub), nextStatement, testDescription, runNotifier);

        assertThat(capturingTestAndMethodRuleStub.getNumberOfApplicationsOfTestRulesApplyMethod(), is(1));
        assertThat(capturingTestAndMethodRuleStub.getStatementTestRuleApplyWasCalledWith(), is(nextStatement));
        assertThat(capturingTestAndMethodRuleStub.getDescriptionTestRuleApplyWasCalledWith(), is(testDescription));

        statement.evaluate();

        assertThat(capturingTestAndMethodRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    @Test
    // refactoring of test is needed first because otherwise this test is found as an outer instance of the JUnit tests used in this test
    @Ignore
    public void methodRuleIsAppliedForEachHierarchy() throws Exception {
        CapturingMethodRuleStub capturingMethodRuleStub = new CapturingMethodRuleStub();
        hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithMethodRuleAndTwoHierarchies.class), frameworkMethod, new TestWithMethodRuleAndTwoHierarchies(capturingMethodRuleStub), nextStatement, Description.createTestDescription(TestWithMethodRuleAndTwoHierarchies.class, "Test with MethodRule and hierarchies"), runNotifier);

        assertThat(capturingMethodRuleStub.getNumberOfApplications(), is(2));
    }

}
