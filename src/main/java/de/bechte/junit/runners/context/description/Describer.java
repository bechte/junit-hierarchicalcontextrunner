package de.bechte.junit.runners.context.description;

import org.junit.runner.Description;

/**
 * A {@link Describer} is responsible for creating the {@link Description} for the given object.
 *
 * @param <T> The type of objects that the {@link Describer} can handle.
 */
public interface Describer<T extends Object> {
    /**
     * Returns a {@link Description} for the given object of type T.
     *
     * @param object the object of type T
     * @return a {@link Description} of the object
     */
    public Description describe(final T object);
}
