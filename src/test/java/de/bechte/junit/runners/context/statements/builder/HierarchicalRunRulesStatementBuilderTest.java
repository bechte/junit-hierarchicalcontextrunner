package de.bechte.junit.runners.context.statements.builder;

import org.junit.Rule;
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

    private final CapturingTestRuleStub capturingTestRuleStub = new CapturingTestRuleStub();
    private final CapturingMethodRuleStub capturingMethodRuleStub = new CapturingMethodRuleStub();
    private CapturingTestAndMethodRuleStub capturingTestAndMethodRuleStub = new CapturingTestAndMethodRuleStub();
    private Statement nextStatement = mock(Statement.class);

    private HierarchicalRunRulesStatementBuilder hierarchicalRunRulesStatementBuilder = new HierarchicalRunRulesStatementBuilder();
    private final RunNotifier runNotifier = mock(RunNotifier.class);
    private final FrameworkMethod frameworkMethod = mock(FrameworkMethod.class);

    @Test
    public void whenNoRulesArePresentInTestTheNextStatementRemainsUnwrapped() throws Exception {
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithoutRule.class), mock(FrameworkMethod.class), new TestWithoutRule(), nextStatement, Description.createTestDescription(TestWithoutRule.class, "Desc"), runNotifier);

        assertThat(statement, is(nextStatement));
    }

    public class TestWithoutRule {
        public class Context {
            @Test
            public void aTest() {
            }
        }
    }

    @Test
    public void whenTestRuleIsPresent() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithTestRuleOnHighestLevel.class, "Test with TestRule");

        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithTestRuleOnHighestLevel.class), mock(FrameworkMethod.class), new TestWithTestRuleOnHighestLevel(), nextStatement, testDescription, runNotifier);

        assertThat(capturingTestRuleStub.getNumberOfApplications(), is(1));
        assertThat(capturingTestRuleStub.getDescriptionApplyWasCalledWith(), is(testDescription));
        assertThat(capturingTestRuleStub.getStatementAppliedWasCalledWith(), is(nextStatement));

        statement.evaluate();

        assertThat(capturingTestRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    public class TestWithTestRuleOnHighestLevel {
        @Rule
        public CapturingTestRuleStub rule = capturingTestRuleStub;

        public class Context {
            @Test
            public void aTest() throws Exception {
            }
        }
    }

    @Test
    public void methodRuleIsPresentOnHighestLevelAndTestClassHasNoInnerContexts() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithMethodRuleOnHighestLevelWithoutInnerContexts.class, "Test with MethodRule");

        Object target = new TestWithMethodRuleOnHighestLevelWithoutInnerContexts();
        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithMethodRuleOnHighestLevelWithoutInnerContexts.class), frameworkMethod, target, nextStatement, testDescription, runNotifier);

        assertThat(capturingMethodRuleStub.getNumberOfApplications(), is(1));
        assertThat(capturingMethodRuleStub.getTargetApplyWasCalledWith(), is(target));
        assertThat(capturingMethodRuleStub.getFrameworkMethodApplyWasCalledWith(), is(frameworkMethod));
        assertThat(capturingMethodRuleStub.getStatementApplyWasCalledWith(), is(nextStatement));

        statement.evaluate();

        assertThat(capturingMethodRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    public class TestWithMethodRuleOnHighestLevelWithoutInnerContexts {
        @Rule
        public CapturingMethodRuleStub rule = capturingMethodRuleStub;

        @Test
        public void aTest() {
        }
    }

    @Test
    public void whenRuleImplementsBothTestRuleAndMethodRule_onlyTestRuleApplyIsExecutedAndOnlyOnce() throws Throwable {
        Description testDescription = Description.createTestDescription(TestWithRuleThatImplementsBothTestRuleAndMethodRule.class, "Test with rule that implements both TestRule and MethodRule");

        Statement statement = hierarchicalRunRulesStatementBuilder.createStatement(new TestClass(TestWithRuleThatImplementsBothTestRuleAndMethodRule.class), frameworkMethod, new TestWithRuleThatImplementsBothTestRuleAndMethodRule(), nextStatement, testDescription, runNotifier);

        assertThat(capturingTestAndMethodRuleStub.getNumberOfApplicationsOfTestRulesApplyMethod(), is(1));
        assertThat(capturingTestAndMethodRuleStub.getStatementTestRuleApplyWasCalledWith(), is(nextStatement));
        assertThat(capturingTestAndMethodRuleStub.getDescriptionTestRuleApplyWasCalledWith(), is(testDescription));

        statement.evaluate();

        assertThat(capturingTestAndMethodRuleStub.statementReturnedByRuleApplyMethodWasEvaluated(), is(true));
    }

    public class TestWithRuleThatImplementsBothTestRuleAndMethodRule {
        @Rule
        public CapturingTestAndMethodRuleStub rule = capturingTestAndMethodRuleStub;

        public class Context {
            @Test
            public void aTest() {
            }
        }
    }
}
