package de.bechte.junit.runners.util;

import java.lang.reflect.Field;
import java.util.Stack;

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
     * @throws IllegalAccessException An instance of the enclosing class is kept in a private field within the enclosed
     * instance. Accessing the field might throw an {@link IllegalAccessException}.
     */
    public static Object getEnclosingInstance(final Object target) throws IllegalAccessException {
        final Class<?> targetClass = target.getClass();
        if (!targetClass.isMemberClass())
            return null;

        final Class<?> enclosingClass = targetClass.getEnclosingClass();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getType().equals(enclosingClass)) {
                field.setAccessible(true);
                return field.get(target);
            }
        }
        return null;
    }

    /**
     * Returns a {@link Stack} of classes, representing the hierarchy of the given {@link Class}.
     *
     * @param clazz the {@link Class} to retrieve the hierarchy for
     * @return the {@link Class} hierarchy
     */
    public static Stack<Class<?>> getClassHierarchy(final Class<?> clazz) {
        final Stack<Class<?>> classHierarchy = new Stack<Class<?>>();
        for (Class<?> c = clazz; c != null; c = c.getEnclosingClass())
            classHierarchy.push(c);
        return classHierarchy;
    }
}
