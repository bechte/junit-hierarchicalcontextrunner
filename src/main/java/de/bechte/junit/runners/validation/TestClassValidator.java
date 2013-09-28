package de.bechte.junit.runners.validation;

import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link TestClassValidator} interface provides a simple {@link #validate(TestClass, List)} method. With this
 * interface, {@link TestClass} validation can be generalized and handled by the runner, without knowledge about the
 * individual implementations of the different validators. All validators should implement this interface.
 */
public interface TestClassValidator {
    /**
     * Validates the given {@link TestClass} and adds all errors to the given {@link List}.
     *
     * @param testClass the {@link TestClass} to validate
     * @param errors the {@link List} of errors
     */
    public void validate(final TestClass testClass, final List<Throwable> errors);
}