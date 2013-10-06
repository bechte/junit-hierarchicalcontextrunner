package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.context.description.MethodDescriber;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import de.bechte.junit.runners.context.statements.builder.MethodStatementBuilder;
import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.ContextTestClassStub;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MethodExecutorTest {
    @Mock
    private MethodDescriber methodDescriber;

    @Mock
    private Description description;

    @Mock
    private StatementExecutor statementExecutor;

    @Mock
    private MethodStatementBuilder methodStatementBuilder1;

    @Mock
    private MethodStatementBuilder methodStatementBuilder2;

    @Mock
    private Statement statement1;

    @Mock
    private Statement statement2;

    @Mock
    private FrameworkMethod method;

    @Mock
    private Ignore ignoreAnnotation;

    @Mock
    private RunNotifier notifier;

    private TestClass testClass;
    private List<MethodStatementBuilder> statementBuilders;
    private MethodExecutor methodExecutor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        testClass = TestClassPool.forClass(ContextTestClassStub.class);
        statementBuilders = Arrays.asList(methodStatementBuilder1, methodStatementBuilder2);
        methodExecutor = new MethodExecutor(methodDescriber, statementExecutor, statementBuilders);

        when(methodDescriber.describe(method)).thenReturn(description);
        when(ignoreAnnotation.value()).thenReturn("ignore");

        when(methodStatementBuilder1.createStatement(same(testClass), same(method), anyObject(), any(InvokeMethod.class), same(description), same(notifier))).thenReturn(statement1);
        when(methodStatementBuilder2.createStatement(same(testClass), same(method), anyObject(), same(statement1), same(description), same(notifier))).thenReturn(statement2);
    }

    @Test
    public void whenCalledForMethodWithIgnoreAnnotation_testIsIgnored() throws Exception {
        when(method.getAnnotation(Ignore.class)).thenReturn(ignoreAnnotation);

        methodExecutor.run(testClass, method, notifier);

        verifyNoMoreInteractions(statementExecutor);
        verify(notifier).fireTestIgnored(description);
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenCalledForMethod_statementExecutorIsExecutedWithAStatement() throws Exception {
        methodExecutor.run(testClass, method, notifier);

        verify(statementExecutor).execute(any(Statement.class), same(notifier), same(description));
        verifyNoMoreInteractions(statementExecutor);
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenCalledForMethod_statementBuildersAreExecuted() throws Exception {
        methodExecutor.run(testClass, method, notifier);

        verify(statementExecutor).execute(statement2, notifier, description);
        verifyNoMoreInteractions(statementExecutor);
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void whenStatementExecutorThrowsException_failureIsReportedAtTheNotifier() throws Exception {
        doThrow(new RuntimeException("")).when(statementExecutor).execute(statement2, notifier, description);

        methodExecutor.run(testClass, method, notifier);

        verify(statementExecutor).execute(isA(Fail.class), same(notifier), same(description));
        verifyNoMoreInteractions(notifier);
    }
}