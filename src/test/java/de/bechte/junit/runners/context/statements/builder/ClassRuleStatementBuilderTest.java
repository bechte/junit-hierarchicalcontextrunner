package de.bechte.junit.runners.context.statements.builder;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ClassRuleStatementBuilderTest {
    @Mock private TestClass testClass;
    @Mock private Statement next;
    @Mock private Description description;
    @Mock private RunNotifier notifier;
    @Mock private TestRule rule1;
    @Mock private TestRule rule2;

    private ClassRuleStatementBuilder builder = new ClassRuleStatementBuilder();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void givenTestClassWithoutRuleAnnotations_returnsNextStatement() throws Exception {
        when(testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class)).thenReturn(Collections.EMPTY_LIST);
        when(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class)).thenReturn(Collections.EMPTY_LIST);

        Statement statement = builder.createStatement(testClass, next, description, notifier);
        assertThat(statement, is(equalTo(next)));
    }

    @Test
    public void givenTestClassWithRuleAnnotatedMethods_returnsRunRulesStatement() throws Exception {
        List<TestRule> methods = Arrays.asList(rule1, rule2);
        when(testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class)).thenReturn(methods);
        when(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class)).thenReturn(Collections.EMPTY_LIST);

        Statement actual = builder.createStatement(testClass, next, description, notifier);
        assertThat(actual, is(instanceOf(RunRules.class)));
    }

    @Test
    public void givenTestClassWithRuleAnnotatedFields_returnsRunRulesStatement() throws Exception {
        List<TestRule> fields = Arrays.asList(rule1, rule2);
        when(testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class)).thenReturn(Collections.EMPTY_LIST);
        when(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class)).thenReturn(fields);

        Statement actual = builder.createStatement(testClass, next, description, notifier);
        assertThat(actual, is(instanceOf(RunRules.class)));
    }

    @Test
    public void givenTestClassWithRuleAnnotatedMethodsAndFields_returnsRunRulesStatement() throws Exception {
        List<TestRule> methods = Arrays.asList(rule1);
        List<TestRule> fields = Arrays.asList(rule2);
        when(testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class)).thenReturn(methods);
        when(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class)).thenReturn(fields);

        Statement actual = builder.createStatement(testClass, next, description, notifier);
        assertThat(actual, is(instanceOf(RunRules.class)));
    }
}