package de.bechte.junit.runners.context.statements;

/**
 * The {@link StatementExecutorFactory} resolves the {@link StatementExecutor}s for classes and methods. These two
 * types might differ, as they require different reporting.
 *
 * Note: Clients can register their own factory by setting the system property with key {@code PROPERTY_KEY}. This
 * property must contain a name of the implementation class, e.g. "com.you.factories.MyStatementExecutorFactory".
 * If no property is set the {@link DefaultStatementExecutorFactory} will be used instead.
 */
public abstract class StatementExecutorFactory {
    protected static final String PROPERTY_KEY = StatementExecutorFactory.class.getCanonicalName();
    private static StatementExecutorFactory factory = null;

    public static StatementExecutorFactory getDefault() {
        if (factory == null)
            lazyLoadFactory();
        return factory;
    }

    private static void lazyLoadFactory() {
        final String factoryName = System.getProperty(PROPERTY_KEY);
        try {
            factory = (factoryName == null)
                    ? new DefaultStatementExecutorFactory()
                    : (StatementExecutorFactory) Class.forName(factoryName).newInstance();
        } catch (final Throwable t) {
            throw new IllegalStateException("JUnit system not configured correctly. " +
                    "Cannot find StatementExecutorFactory! Invalid factory name given: " + factoryName, t);
        }
    }

    protected StatementExecutorFactory() {
        super();
    }

    public abstract StatementExecutor getExecutorForClasses();
    public abstract StatementExecutor getExecutorForMethods();
}