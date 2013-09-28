package de.bechte.junit.runners.context.description;

import de.bechte.junit.runners.context.processing.ChildResolver;
import de.bechte.junit.runners.model.TestClassPool;
import org.junit.runner.Description;
import org.junit.runners.model.TestClass;

/**
 * The {@link SuiteDescriber} is responsible for creating the {@link Description} for a test suite. A test suite can be
 * a single test or a test suite containing several test classes. This class only handles classes and subclasses found
 * by the given {@link ChildResolver}.
 */
public class SuiteDescriber implements Describer<Class<?>> {
    private final ChildResolver<Class<?>> childResolver;

    public SuiteDescriber(final ChildResolver<Class<?>> childResolver) {
        this.childResolver = childResolver;
    }

    @Override
    public Description describe(final Class<?> contextClass) {
        final Description description = Description.createSuiteDescription(contextClass);
        final TestClass contextTestClass = TestClassPool.forClass(contextClass);
        addChildren(description, contextTestClass);
        return description;
    }

    protected void addChildren(final Description description, final TestClass contextTestClass) {
        for (Class<?> context : childResolver.getChildren(contextTestClass))
            description.addChild(this.describe(context));
    }
}
