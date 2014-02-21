package de.bechte.junit.runners.validation;

import de.bechte.junit.runners.context.processing.ChildResolver;
import de.bechte.junit.runners.context.processing.ContextResolver;
import de.bechte.junit.runners.context.processing.MethodResolver;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link ChildrenCountValidator}s validate that the given {@link TestClass} contains at least 1 child. If there
 * is no child within the {@link TestClass} an error is reported in the {@link List} of errors.
 */
public enum ChildrenCountValidator implements TestClassValidator {
    TEST_METHODS(new MethodResolver(), "No tests found!"),
    CONTEXT_HIERARCHIES(new ContextResolver(), "No contexts found!");

    private final ChildResolver<?> childResolver;
    private final String errorMessage;

    private ChildrenCountValidator(final ChildResolver<?> childResolver, final String errorMessage) {
        this.childResolver = childResolver;
        this.errorMessage = errorMessage;
    }

    public void validate(final TestClass testClass, final List<Throwable> errors) {
        if (childResolver.getChildren(testClass).isEmpty())
            errors.add(new Exception(errorMessage));
    }
}
