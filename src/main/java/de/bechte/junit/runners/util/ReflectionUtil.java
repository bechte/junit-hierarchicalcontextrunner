package de.bechte.junit.runners.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import static java.lang.reflect.Modifier.isStatic;

/**
 * Set of helper methods to retrieve information using Java's reflection API.
 */
public final class ReflectionUtil {
    // Avoid instantiation
    private ReflectionUtil() {
        super();
    }

    /**
     * Returns the {@link Object} that encloses the given {@code target}. This method returns {@code null} if the given
     * {@code target} is null or its {@link Class} is not a member class.
     *
     * @param target the {@link Object} to retrieve the enclosing instance for
     * @return the enclosing {@link Object} of {@code target}
     * @throws IllegalArgumentException If {@code target} is {@code null}.
     * @throws IllegalAccessException An instance of the enclosing class is kept in a private field within the enclosed
     * instance. Accessing the field might throw an {@link IllegalAccessException}.
     * @throws IllegalStateException If no field containing the enclosing instance can be found.
     */
    public static Object getEnclosingInstance(final Object target) throws IllegalAccessException {
        if (target == null)
            throw new IllegalArgumentException("Target must not be null!");

        final Class<?> targetClass = target.getClass();
        if (isStatic(targetClass.getModifiers()) || !targetClass.isMemberClass())
            return null;

        final Class<?> enclosingClass = targetClass.getEnclosingClass();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getType().equals(enclosingClass)) {
                field.setAccessible(true);
                return field.get(target);
            }
        }

        throw new IllegalStateException("Member instance has no field containing the enclosing instance!");
    }

    /**
     * Returns a {@link Stack} of classes, representing the hierarchy of the given {@link Class}.
     *
     * @param clazz the {@link Class} to retrieve the hierarchy for
     * @return the {@link Class} hierarchy
     */
    public static Stack<Class<?>> getClassHierarchy(final Class<?> clazz) {
        final Stack<Class<?>> classHierarchy = new Stack<Class<?>>();
        Class<?> c = clazz;
        while (c != null) {
            classHierarchy.push(c);
            c = (isStatic(c.getModifiers())) ? null : c.getEnclosingClass();
        }
        return classHierarchy;
    }

    /**
     * Returns an instance of the {@link Class}, represented by the given class hierarchy.
     *
     * @param classHierarchy the hierarchy representing a deep class
     * @return the newly created instance
     * @throws Throwable if errors occurred during construction of the instance
     */
    public static Object createDeepInstance(final Stack<Class<?>> classHierarchy) throws Throwable {
        if (classHierarchy == null || classHierarchy.isEmpty())
            throw new IllegalArgumentException("Stack must not be null or empty!");

        try {
            // Top level class has empty constructor
            Class<?> outerClass = classHierarchy.pop();
            Object test = outerClass.newInstance();

            // Inner class constructors require the enclosing instance
            while (!classHierarchy.empty()) {
                final Class<?> innerClass = classHierarchy.pop();
                final Constructor<?> innerConstructor = innerClass.getConstructor(outerClass);
                test = innerConstructor.newInstance(test);
                outerClass = innerClass;
            }
            return test;
        } catch (final InvocationTargetException e) {
            throw ((InvocationTargetException) e).getTargetException();
        }
    }
}
