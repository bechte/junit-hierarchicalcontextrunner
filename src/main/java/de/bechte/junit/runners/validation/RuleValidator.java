package de.bechte.junit.runners.validation;

import org.junit.internal.runners.rules.RuleFieldValidator;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link RuleValidator}s validate methods and fields annotated with @Rule. This validators delegate the validation
 * call to the implementation of the {@link RuleFieldValidator} class. This class also has a validate method but it
 * does not implement the {@link TestClassValidator} interface. Therefore, a simple delegate was created.
 */
public enum RuleValidator implements TestClassValidator {
    CLASS_RULE_VALIDATOR(RuleFieldValidator.CLASS_RULE_VALIDATOR),
    RULE_VALIDATOR(RuleFieldValidator.RULE_VALIDATOR);

    private final RuleFieldValidator validator;

    private RuleValidator(final RuleFieldValidator validator) {
        this.validator = validator;
    }

    public void validate(final TestClass testClass, final List<Throwable> errors) {
        validator.validate(testClass, errors);
    }
}
