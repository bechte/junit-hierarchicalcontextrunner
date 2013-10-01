package de.bechte.junit.runners.context.statements.builder;

import java.util.List;

/**
 * The {@link StatementBuilderFactory} resolves a {@link List} of {@link ClassStatementBuilder}s for classes and
 * {@link MethodStatementBuilder}s for methods. These two types might differ, as they require different processing.
 *
 * Note: Clients can register their own factory by setting the system property with key {@code PROPERTY_KEY}. This
 * property must contain a name of the implementation class, e.g. "com.you.factories.MyStatementBuilderFactory".
 * If no property is set the {@link DefaultStatementBuilderFactory} will be used instead.
 */
public abstract class StatementBuilderFactory {
    protected static final String PROPERTY_KEY = StatementBuilderFactory.class.getCanonicalName();
    private static StatementBuilderFactory factory = null;

    public static StatementBuilderFactory getDefault() {
        if (factory == null)
            lazyLoadFactory();
        return factory;
    }

    private static void lazyLoadFactory() {
        final String factoryName = System.getProperty(PROPERTY_KEY);
        try {
            factory = (factoryName == null)
                    ? new DefaultStatementBuilderFactory()
                    : (StatementBuilderFactory) Class.forName(factoryName).newInstance();
        } catch (final Throwable t) {
            throw new IllegalStateException("JUnit system not configured correctly. " +
                    "Cannot find StatementBuilderFactory! Invalid factory name given: " + factoryName, t);
        }
    }

    protected StatementBuilderFactory() {
        super();
    }

    public abstract List<ClassStatementBuilder> getBuildersForClasses();
    public abstract List<MethodStatementBuilder> getBuildersForMethods();
}