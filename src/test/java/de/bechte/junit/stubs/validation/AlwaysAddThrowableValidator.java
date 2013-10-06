package de.bechte.junit.stubs.validation;

import de.bechte.junit.runners.validation.TestClassValidator;
import org.junit.runners.model.TestClass;

import java.util.List;

public class AlwaysAddThrowableValidator implements TestClassValidator {
    @Override
    public void validate(final TestClass testClass, final List<Throwable> errors) {
        errors.add(new Throwable("This Throwable is always added!"));
    }
}