package de.bechte.junit.runners.context.description;

import de.bechte.junit.runners.context.processing.ChildResolver;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * The {@link ContextDescriber} is responsible for creating the {@link Description} for a context hierarchy. A context
 * hierarchy can contain tests and other context hierarchies. This class handles all tests and context hierarchies
 * found by the given resolvers. For the description of the tests the injected {@link Describer} is called.
 */
public class ContextDescriber extends SuiteDescriber {
    private final ChildResolver<FrameworkMethod> methodResolver;
    private final Describer<FrameworkMethod> methodDescriber;

    public ContextDescriber(final ChildResolver<Class<?>> contextResolver,
            final ChildResolver<FrameworkMethod> methodResolver, final Describer<FrameworkMethod> methodDescriber) {
        super(contextResolver);
        this.methodResolver = methodResolver;
        this.methodDescriber = methodDescriber;
    }

    @Override
    protected void addChildren(final Description description, final TestClass testClass) {
        for (final FrameworkMethod method : methodResolver.getChildren(testClass))
            description.addChild(methodDescriber.describe(method));

        super.addChildren(description, testClass);
    }
}