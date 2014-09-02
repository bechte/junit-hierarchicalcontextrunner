package de.bechte.junit.runners.validation;

import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * The {@link RuleValidator}s validate methods and fields annotated with @Rule. This validators delegate the validation
 * call to the implementation of the {@link RuleMemberValidator} class. This class also has a validate method but it
 * does not implement the {@link TestClassValidator} interface. Therefore, a simple delegate was created.
 */
public enum RuleValidator implements TestClassValidator {
    CLASS_RULE_VALIDATOR(RuleMemberValidator.CLASS_RULE_VALIDATOR),
    RULE_VALIDATOR(RuleMemberValidator.RULE_VALIDATOR),
    CLASS_RULE_METHOD_VALIDATOR(RuleMemberValidator.CLASS_RULE_METHOD_VALIDATOR),
    RULE_METHOD_VALIDATOR(RuleMemberValidator.RULE_METHOD_VALIDATOR);

    private final RuleMemberValidator validator;

    private RuleValidator(final RuleMemberValidator validator) {
        this.validator = validator;
    }

    public void validate(final TestClass testClass, final List<Throwable> errors) {
        validator.validate(testClass, errors);
    }
}
