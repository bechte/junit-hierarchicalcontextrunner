package de.bechte.junit.runners.context.statements.builder;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
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

public class BeforeClassStatementBuilderTest {
    @Mock private TestClass testClass;
    @Mock private Statement next;
    @Mock private Description description;
    @Mock private RunNotifier notifier;
    @Mock private FrameworkMethod method1;
    @Mock private FrameworkMethod method2;

    private BeforeClassStatementBuilder builder = new BeforeClassStatementBuilder();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void givenTestClassWithoutBeforeClassAnnotatedMethods_returnsNextStatement() throws Exception {
        when(testClass.getAnnotatedMethods(BeforeClass.class)).thenReturn(Collections.EMPTY_LIST);

        Statement statement = builder.createStatement(testClass, next, description, notifier);
        assertThat(statement, is(equalTo(next)));
    }

    @Test
    public void givenTestClassWithBeforeClassAnnotatedMethods_returnsRunBeforeStatement() throws Exception {
        List<FrameworkMethod> befores = Arrays.asList(method1, method2);
        when(testClass.getAnnotatedMethods(BeforeClass.class)).thenReturn(befores);

        Statement actual = builder.createStatement(testClass, next, description, notifier);
        assertThat(actual, is(instanceOf(RunBefores.class)));
    }
}