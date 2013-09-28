package de.bechte.junit.runners.validation;

import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link BooleanValidator}s allow to group {@link TestClassValidator}s with boolean algebra. All violations are
 * reported in the {@link List} of errors.
 */
public abstract class BooleanValidator implements TestClassValidator {
    public static BooleanValidator AND(final TestClassValidator... validators) {
        return new AndValidator(validators);
    }

    public static BooleanValidator OR(final TestClassValidator... validators) {
        return new OrValidator(validators);
    }

    protected final TestClassValidator[] validators;

    private BooleanValidator(final TestClassValidator[] validators) {
        this.validators = validators == null ? new TestClassValidator[0] : validators;
    }

    private static class AndValidator extends BooleanValidator {
        public AndValidator(TestClassValidator[] validators) {
            super(validators);
        }
        @Override
        public void validate(final TestClass testClass, final List<Throwable> errors) {
            for (final TestClassValidator validator : validators)
                validator.validate(testClass, errors);
        }
    }

    private static class OrValidator extends BooleanValidator {
        public OrValidator(final TestClassValidator[] validators) {
            super(validators);
        }

        @Override
        public void validate(final TestClass testClass, final List<Throwable> errors) {
            final List<Throwable> tempErrors = new ArrayList<Throwable>();
            final List<Throwable> currentErrors = new ArrayList<Throwable>();

            for (final TestClassValidator validator : validators) {
                currentErrors.clear();
                validator.validate(testClass, currentErrors);

                if (!currentErrors.isEmpty()) {
                    tempErrors.addAll(currentErrors);
                } else {
                    tempErrors.clear();
                    break;
                }
            }

            errors.addAll(tempErrors);
        }
    }
}
