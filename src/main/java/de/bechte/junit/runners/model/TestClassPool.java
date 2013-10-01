package de.bechte.junit.runners.model;

import org.junit.runners.model.TestClass;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This pool of {@link TestClass}es is used to reduce the expensive look-ups and reflection calls that are performed
 * during the construction of the {@link TestClass} instances. The pool will keep a reference to each instance so that
 * no {@code TestClass} has to be constructed twice.
 *
 * Note: The additional memory consumption for keeping the instances is very low, but the pool results in a tremendous
 * speed-up in case of the {@link de.bechte.junit.runners.context.HierarchicalContextRunner}, when {@link TestClass}es
 * must be recreated several times along the context hierarchy.
 */
public final class TestClassPool {
    private static final Map<Class<?>, TestClass> testClasses =
            Collections.synchronizedMap(new WeakHashMap<Class<?>, TestClass>());

    // Avoid instantiation
    private TestClassPool() {
        super();
    }

    /**
     * Returns a {@link TestClass} wrapping the given {@code testClass}. {@link TestClass}es will be shared and held
     * within the pool, to avoid the expensive process that is performed during construction of the instances.
     *
     * @param testClass the {@link Class} to create an {@link TestClass} instance for
     * @return the {@link TestClass} instance
     * @throws IllegalArgumentException If {@code testClass} is {@code null}.
     */
    public static TestClass forClass(final Class<?> testClass) {
        if (testClass == null)
            throw new IllegalArgumentException("TestClass must not be null!");

        if (!testClasses.containsKey(testClass))
            testClasses.put(testClass, new TestClass(testClass));

        return testClasses.get(testClass);
    }
}