package de.bechte.junit.runners.context.processing;

import org.junit.runners.model.TestClass;

import java.util.List;


/**
 * A {@link ChildResolver} is responsible for resolving all children for the given {@link TestClass}. Typically, the
 * {@link ChildResolver} is used together with the {@link ChildExecutor}.
 *
 * @param <T> The type of objects that the {@link ChildResolver} returns.
 */
public interface ChildResolver<T extends Object> {
    /**
     * Resolved the children of type T for the given {@link TestClass}.
     *
     * @param testClass the {@link TestClass} to resolve the children for
     * @return a {@link List} of children of type T
     */
    public List<T> getChildren(final TestClass testClass);
}
