package de.bechte.junit.runners.validation;

import org.junit.*;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * The {@link FixtureValidator}s validate methods annotated with @BeforeClass, @Before, @AfterClass, and @After.
 * These methods must be public accessible and must not have any arguments. If a method violates this it is reported
 * in the {@link List} of errors.
 */
public enum FixtureValidator implements TestClassValidator {
    BEFORE_CLASS_METHODS(BeforeClass.class, true),
    AFTER_CLASS_METHODS(AfterClass.class, true),
    BEFORE_METHODS(Before.class, false),
    AFTER_METHODS(After.class, false),
    TEST_METHODS(Test.class, false);

    private Class<? extends Annotation> annotationClass;
    private boolean isStatic;

    private FixtureValidator(final Class<? extends Annotation> annotationClass, final boolean isStatic) {
        this.annotationClass = annotationClass;
        this.isStatic = isStatic;
    }

    @Override
    public void validate(final TestClass testClass, final List<Throwable> errors) {
        final List<FrameworkMethod> methods = testClass.getAnnotatedMethods(annotationClass);
        for (final FrameworkMethod method : methods) {
            method.validatePublicVoidNoArg(isStatic, errors);
        }
    }
}
