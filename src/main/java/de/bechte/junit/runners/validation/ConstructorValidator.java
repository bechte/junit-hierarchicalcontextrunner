package de.bechte.junit.runners.validation;

import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * The {@link ConstructorValidator} validates that test classes have only one public constructor that takes no
 * arguments for top-level classes and exactly one argument for enclosed classes of type of the enclosing class.
 */
public enum ConstructorValidator implements TestClassValidator {
    VALID_CONSTRUCTOR;

    @Override
    public void validate(final TestClass testClass, final List<Throwable> errors) {
        if (!isAllowed(testClass.getJavaClass()))
            return;

        validateOnlyOneConstructor(testClass, errors);
        validateConstructorArgumentCount(testClass, errors);
    }

    private boolean isAllowed(final Class<?> testClass) {
        return !Modifier.isStatic(testClass.getModifiers());
    }

    private void validateOnlyOneConstructor(final TestClass testClass, final List<Throwable> errors) {
        if (testClass.getJavaClass().getConstructors().length != 1) {
            errors.add(new Exception("Test class should have exactly one public constructor"));
        }
    }

    private void validateConstructorArgumentCount(final TestClass testClass, final List<Throwable> errors) {
        if (testClass.getJavaClass().isMemberClass())
            validateMemberClassConstructorArgumentCount(testClass, errors);
        else
            validateTopLevelClassConstructorArgumentCount(testClass, errors);
    }

    private void validateMemberClassConstructorArgumentCount(TestClass testClass, List<Throwable> errors) {
        final Constructor<?>[] constructors = testClass.getJavaClass().getConstructors();
        if (constructors.length == 1) {
            final Class<?> enclosingClass = testClass.getJavaClass().getEnclosingClass();
            final Class<?>[] parameterTypes = constructors[0].getParameterTypes();
            if (parameterTypes.length != 1 || !parameterTypes[0].isAssignableFrom(enclosingClass))
                errors.add(new Exception("Test class within a hierarchical context " +
                        "should have exactly one public one-argument constructor"));
        }
    }

    private void validateTopLevelClassConstructorArgumentCount(TestClass testClass, List<Throwable> errors) {
        final Constructor<?>[] constructors = testClass.getJavaClass().getConstructors();
        if (constructors.length == 1) {
            final Class<?>[] parameterTypes = constructors[0].getParameterTypes();
            if (parameterTypes.length != 0)
                errors.add(new Exception("Test class should have exactly one public zero-argument constructor"));
        }
    }
}
