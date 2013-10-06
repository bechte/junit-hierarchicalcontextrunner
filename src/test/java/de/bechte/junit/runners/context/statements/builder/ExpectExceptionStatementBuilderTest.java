package de.bechte.junit.runners.context.statements.builder;

import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.singletests.SingleTestWithExpectedAnnotation;
import de.bechte.junit.stubs.singletests.SingleTestWithoutAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExpectExceptionStatementBuilderTest {
    @Mock private Object target;
    @Mock private Statement next;
    @Mock private Description description;
    @Mock private RunNotifier notifier;
    @Mock private Test testAnnotation;

    private ExpectExceptionStatementBuilder builder = new ExpectExceptionStatementBuilder();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void whenTestAnnotationDoesNotSpecifyAnException_returnNextStatement() throws Exception {
        TestClass testClass = TestClassPool.forClass(SingleTestWithoutAnnotations.class);
        FrameworkMethod method = testClass.getAnnotatedMethods(Test.class).get(0);

        Statement actual = builder.createStatement(testClass, method, target, next, description, notifier);
        assertThat(actual, is(equalTo(next)));
    }

    @Test
    public void whenTestAnnotationDoesSpecifyAnException_returnExpectExceptionStatement() throws Exception {
        TestClass testClass = TestClassPool.forClass(SingleTestWithExpectedAnnotation.class);
        FrameworkMethod method = testClass.getAnnotatedMethods(Test.class).get(0);

        Statement actual = builder.createStatement(testClass, method, target, next, description, notifier);
        assertThat(actual, is(instanceOf(ExpectException.class)));
    }
}