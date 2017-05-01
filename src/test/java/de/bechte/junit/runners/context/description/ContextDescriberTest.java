package de.bechte.junit.runners.context.description;

import de.bechte.junit.runners.context.processing.ChildResolver;
import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.ContextTestClassStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.bechte.junit.matchers.CollectionMatchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class ContextDescriberTest {
    private Class contextTestClass;
    private MethodDescriber methodDescriber;
    private ChildResolver methodResolver;
    private ChildResolver<Class<?>> contextResolver;
    private ContextDescriber describer;

    @Before
    public void setUp() throws Exception {
        contextTestClass = ContextTestClassStub.class;
        contextResolver = mock(ChildResolver.class);
        methodResolver = mock(ChildResolver.class);
        methodDescriber = mock(MethodDescriber.class);

        describer = new ContextDescriber(contextResolver, methodResolver, methodDescriber);
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenDescribeIsCalledWithNull_anIllegalArgumentExceptionIsRaised() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Class must not be null!");
        describer.describe(null);
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectTestClass() throws Exception {
        Description description = describer.describe(contextTestClass);
        assertThat(description.getTestClass(), is(equalTo(contextTestClass)));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectDisplayName() throws Exception {
        Description description = describer.describe(contextTestClass);
        assertThat(description.getDisplayName(), is(equalTo(contextTestClass.getCanonicalName())));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectAnnotations() throws Exception {
        Description description = describer.describe(contextTestClass);
        assertThat(description.getAnnotations(), containsInAnyOrder(contextTestClass.getAnnotations()));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectChildren() throws Exception {
        Description descriptionA = mock(Description.class);
        Description descriptionB = mock(Description.class);
        FrameworkMethod outerTestMethod = mock(FrameworkMethod.class);
        Description descriptionOuterTestMethod = mock(Description.class);

        SuiteDescriber spiedDescriber = spy(describer);
        doReturn(descriptionA).when(spiedDescriber).describe(ContextTestClassStub.A.class);
        doReturn(descriptionB).when(spiedDescriber).describe(ContextTestClassStub.B.class);

        List<Class<?>> contexts = Arrays.asList(ContextTestClassStub.A.class, ContextTestClassStub.B.class);
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.class);
        when(contextResolver.getChildren(testClass)).thenReturn(contexts);

        when(methodResolver.getChildren(testClass)).thenReturn(Collections.singletonList(outerTestMethod));
        when(methodDescriber.describe(outerTestMethod)).thenReturn(descriptionOuterTestMethod);

        Description description = spiedDescriber.describe(ContextTestClassStub.class);
        assertThat(description.getChildren(), containsInAnyOrder(descriptionA, descriptionB, descriptionOuterTestMethod));

        verify(spiedDescriber).addChildren(any(Description.class), same(testClass));
        verify(spiedDescriber).describe(ContextTestClassStub.class);
        verify(spiedDescriber).describe(ContextTestClassStub.A.class);
        verify(spiedDescriber).describe(ContextTestClassStub.B.class);
    }
}
