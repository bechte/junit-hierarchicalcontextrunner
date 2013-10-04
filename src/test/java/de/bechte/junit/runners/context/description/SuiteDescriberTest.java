package de.bechte.junit.runners.context.description;

import de.bechte.junit.runners.context.processing.ChildResolver;
import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.ContextTestClassStub;
import de.bechte.junit.stubs.SimpleTestClassStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runners.model.TestClass;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SuiteDescriberTest {
    private Class simpleTestClass;
    private ChildResolver<Class<?>> childrenResolver;
    private SuiteDescriber describer;

    @Before
    public void setUp() throws Exception {
        simpleTestClass = SimpleTestClassStub.class;
        childrenResolver = mock(ChildResolver.class);
        describer = new SuiteDescriber(childrenResolver);
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
        Description description = describer.describe(simpleTestClass);
        assertThat((Class) description.getTestClass(), is(equalTo(simpleTestClass)));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectDisplayName() throws Exception {
        Description description = describer.describe(simpleTestClass);
        assertThat(description.getDisplayName(), is(equalTo(simpleTestClass.getCanonicalName())));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectAnnotations() throws Exception {
        Description description = describer.describe(simpleTestClass);
        assertThat(description.getAnnotations(), containsInAnyOrder(simpleTestClass.getAnnotations()));
    }

    @Test
    public void whenDescribeIsCalled_descriptionHasTheCorrectChildren() throws Exception {
        Description descriptionA = mock(Description.class);
        Description descriptionB = mock(Description.class);

        SuiteDescriber spiedDescriber = spy(describer);
        doReturn(descriptionA).when(spiedDescriber).describe(ContextTestClassStub.A.class);
        doReturn(descriptionB).when(spiedDescriber).describe(ContextTestClassStub.B.class);

        List<Class<?>> children = Arrays.asList(ContextTestClassStub.A.class, ContextTestClassStub.B.class);
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.class);
        when(childrenResolver.getChildren(testClass)).thenReturn(children);

        Description description = spiedDescriber.describe(ContextTestClassStub.class);
        assertThat(description.getChildren(), containsInAnyOrder(descriptionA, descriptionB));

        verify(spiedDescriber).addChildren(any(Description.class), same(testClass));
        verify(spiedDescriber).describe(ContextTestClassStub.class);
        verify(spiedDescriber).describe(ContextTestClassStub.A.class);
        verify(spiedDescriber).describe(ContextTestClassStub.B.class);
    }
}