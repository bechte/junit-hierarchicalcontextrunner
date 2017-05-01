package de.bechte.junit.runners.validation;

import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.validation.ClassWithInvalidConstructorArguments;
import de.bechte.junit.stubs.validation.ClassWithInvalidConstructorCount;
import de.bechte.junit.stubs.validation.StaticClassWithInvalidConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static de.bechte.junit.matchers.CollectionMatchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConstructorValidatorTest {
    @Mock private TestClass testClass;
    @Spy private List<Throwable> errors = new ArrayList<Throwable>();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void staticClassesAreNeverValidated() throws Exception {
        TestClass testClass1 = TestClassPool.forClass(StaticClassWithInvalidConstructor.PrivateAccessor.class);
        TestClass testClass2 = TestClassPool.forClass(StaticClassWithInvalidConstructor.InvalidCount.class);

        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass1, errors);
        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass2, errors);

        verifyNoMoreInteractions(errors);
    }

    @Test
    public void whenTopLevelClassHasMoreThanOnConstructor_multipleConstructorExceptionIsReported() throws Exception {
        Class javaClass = ClassWithInvalidConstructorCount.class;
        when(testClass.getJavaClass()).thenReturn(javaClass);

        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("Test class should have exactly one public constructor")));
    }

    @Test
    public void whenMemberClassHasMoreThanOnConstructor_multipleConstructorExceptionIsReported() throws Exception {
        Class javaClass = ClassWithInvalidConstructorCount.MemberWithInvalidConstructorCount.class;
        when(testClass.getJavaClass()).thenReturn(javaClass);

        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("Test class should have exactly one public constructor")));
    }

    @Test
    public void whenTopLevelClassConstructorHasArguments_invalidArgumentCountIsReported() throws Exception {
        Class javaClass = ClassWithInvalidConstructorArguments.class;
        when(testClass.getJavaClass()).thenReturn(javaClass);

        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("Test class should have exactly one public zero-argument constructor")));
    }

    @Test
    public void whenMemberClassConstructorHasMoreThanOneArgument_invalidArgumentCountIsReported() throws Exception {
        Class javaClass = ClassWithInvalidConstructorArguments.MemberWithInvalidArguments.class;
        when(testClass.getJavaClass()).thenReturn(javaClass);

        ConstructorValidator.VALID_CONSTRUCTOR.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("Test class within a hierarchical context should have exactly one public one-argument constructor")));
    }
}
