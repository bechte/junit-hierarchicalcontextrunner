package de.bechte.junit.runners.validation;

import org.junit.*;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class FixtureValidatorTest {
    @Mock private TestClass testClass;
    @Mock private List<Throwable> errors;
    @Mock private FrameworkMethod method1;
    @Mock private FrameworkMethod method2;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void verifyMethodInvocationsForAfterClassMethods() throws Exception {
        verifyMethodInvocation(FixtureValidator.AFTER_CLASS_METHODS, AfterClass.class, true);
    }

    @Test
    public void verifyMethodInvocationsForBeforeClassMethods() throws Exception {
        verifyMethodInvocation(FixtureValidator.BEFORE_CLASS_METHODS, BeforeClass.class, true);
    }

    @Test
    public void verifyMethodInvocationsForAfterMethods() throws Exception {
        verifyMethodInvocation(FixtureValidator.AFTER_METHODS, After.class, false);
    }

    @Test
    public void verifyMethodInvocationsForBeforeMethods() throws Exception {
        verifyMethodInvocation(FixtureValidator.BEFORE_METHODS, Before.class, false);
    }

    @Test
    public void verifyMethodInvocationsForTestMethods() throws Exception {
        verifyMethodInvocation(FixtureValidator.TEST_METHODS, Test.class, false);
    }

    private void verifyMethodInvocation(FixtureValidator validator, Class<? extends Annotation> annotationClass, boolean isStatic) {
        when(testClass.getAnnotatedMethods(annotationClass)).thenReturn(Arrays.asList(method1, method2));

        validator.validate(testClass, errors);

        verify(method1).validatePublicVoidNoArg(isStatic, errors);
        verify(method2).validatePublicVoidNoArg(isStatic, errors);
        verifyNoMoreInteractions(method1, method2);
    }
}
