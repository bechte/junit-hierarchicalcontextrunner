package de.bechte.junit.runners.context.description;

import de.bechte.junit.stubs.SimpleTestClassStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;

import static de.bechte.junit.matchers.CollectionMatchers.containsInAnyOrder;
import static de.bechte.junit.matchers.CollectionMatchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MethodDescriberTest {
    private Class simpleTestClass;
    private String testMethodName;
    private String testMethodDisplayName;
    private FrameworkMethod testMethod;
    private MethodDescriber describer;

    @Before
    public void setUp() throws Exception {
        simpleTestClass = SimpleTestClassStub.class;
        testMethodName = "testMethod";
        testMethodDisplayName = String.format("%s(%s)", testMethodName, simpleTestClass.getCanonicalName());
        testMethod = new FrameworkMethod(simpleTestClass.getMethod(testMethodName));
        describer = new MethodDescriber();
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenDescribeIsCalledWithNull_anIllegalArgumentExceptionIsRaised() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method must not be null!");
        describer.describe(null);
    }

    @Test
    public void givenValidMethod_descriptionDefinesATest() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.isTest(), is(true));
        assertThat(description.isSuite(), is(false));
    }

    @Test
    public void givenValidMethod_descriptionHasNoChildren() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.getChildren(), is(emptyCollectionOf(Description.class)));
    }

    @Test
    public void givenValidMethod_descriptionHasTheCorrectTestClass() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.getTestClass(), is(equalTo(simpleTestClass)));
    }

    @Test
    public void givenValidMethod_descriptionHasTheCorrectMethodName() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.getMethodName(), is(equalTo(testMethodName)));
    }

    @Test
    public void givenValidMethod_descriptionHasTheCorrectDisplayName() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.getDisplayName(), is(equalTo(testMethodDisplayName)));
    }

    @Test
    public void givenValidMethod_descriptionHasTheCorrectAnnotations() throws Exception {
        Description description = describer.describe(testMethod);
        assertThat(description.getAnnotations(), containsInAnyOrder(testMethod.getAnnotations()));
    }
}
