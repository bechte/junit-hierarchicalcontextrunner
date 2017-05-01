package de.bechte.junit.runners.context;

import de.bechte.junit.runners.context.description.ContextDescriber;
import de.bechte.junit.runners.context.description.Describer;
import de.bechte.junit.runners.context.description.MethodDescriber;
import de.bechte.junit.runners.context.processing.ChildExecutor;
import de.bechte.junit.runners.context.processing.ContextExecutor;
import de.bechte.junit.runners.context.processing.MethodExecutor;
import de.bechte.junit.runners.context.processing.ChildResolver;
import de.bechte.junit.runners.context.processing.ContextResolver;
import de.bechte.junit.runners.context.processing.MethodResolver;
import de.bechte.junit.runners.context.statements.RunAll;
import de.bechte.junit.runners.context.statements.RunChildren;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import de.bechte.junit.runners.context.statements.StatementExecutorFactory;
import de.bechte.junit.runners.context.statements.builder.ClassStatementBuilder;
import de.bechte.junit.runners.context.statements.builder.StatementBuilderFactory;
import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.runners.validation.BooleanValidator;
import de.bechte.junit.runners.validation.ChildrenCountValidator;
import de.bechte.junit.runners.validation.ConstructorValidator;
import de.bechte.junit.runners.validation.FixtureValidator;
import de.bechte.junit.runners.validation.RuleValidator;
import de.bechte.junit.runners.validation.TestClassValidator;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link HierarchicalContextRunner} allows test classes to have member classes. These member classes are
 * interpreted as context hierarchies, allowing you to group your JUnit tests that use the same preconditions.
 *
 * Please refer to the wiki for a sample and more information:
 * https://github.com/bechte/junit-hierarchicalcontextrunner/wiki
 */
public class HierarchicalContextRunner extends Runner {
    protected final TestClass testClass;

    protected ChildResolver<FrameworkMethod> methodResolver;
    protected Describer<FrameworkMethod> methodDescriber;
    protected ChildExecutor<FrameworkMethod> methodRunner;
    protected ChildResolver<Class<?>> contextResolver;
    protected Describer<Class<?>> contextDescriber;
    protected ChildExecutor<Class<?>> contextRunner;
    protected StatementExecutor statementExecutor;
    protected List<ClassStatementBuilder> statementBuilders;

    public HierarchicalContextRunner(final Class<?> testClass) throws InitializationError {
        this.testClass = TestClassPool.forClass(testClass);

        validate();
        initialize();
    }

    private void validate() throws InitializationError {
        final List<Throwable> errors = new ArrayList<Throwable>();
        getValidator().validate(testClass, errors);
        if (!errors.isEmpty())
            throw new InitializationError(errors);
    }

    /**
     * Returns a {@link TestClassValidator} that validates the {@link TestClass} instance after the
     * {@link HierarchicalContextRunner} has been created for the corresponding {@link Class}. To use multiple
     * {@link TestClassValidator}s, please use the {@link BooleanValidator} AND and OR to group your validators.
     *
     * Note: Clients may override this method to add or remove validators.
     *
     * @return a {@link TestClassValidator}
     */
    protected TestClassValidator getValidator() {
        return BooleanValidator.AND(
                ConstructorValidator.VALID_CONSTRUCTOR,
                BooleanValidator.OR(
                        ChildrenCountValidator.CONTEXT_HIERARCHIES,
                        ChildrenCountValidator.TEST_METHODS
                ),
                FixtureValidator.BEFORE_CLASS_METHODS,
                FixtureValidator.AFTER_CLASS_METHODS,
                FixtureValidator.BEFORE_METHODS,
                FixtureValidator.AFTER_METHODS,
                FixtureValidator.TEST_METHODS,
                RuleValidator.CLASS_RULE_VALIDATOR,
                RuleValidator.RULE_VALIDATOR
        );
    }

    /**
     * Initializes all dependencies for the {@link HierarchicalContextRunner}.
     *
     * Note: Clients may override this method to provide other dependencies.
     */
    protected void initialize() {
        final StatementExecutorFactory statementExecutorFactory = StatementExecutorFactory.getDefault();
        final StatementBuilderFactory statementBuilderFactory = StatementBuilderFactory.getDefault();

        methodResolver = new MethodResolver();
        methodDescriber = new MethodDescriber();
        methodRunner = new MethodExecutor(methodDescriber,
                statementExecutorFactory.getExecutorForMethods(),
                statementBuilderFactory.getBuildersForMethods());

        contextResolver = new ContextResolver();
        contextDescriber = new ContextDescriber(contextResolver, methodResolver, methodDescriber);
        contextRunner = new ContextExecutor(contextDescriber);

        statementExecutor = statementExecutorFactory.getExecutorForClasses();
        statementBuilders = statementBuilderFactory.getBuildersForClasses();
    }

    @Override
    public Description getDescription() {
        return contextDescriber.describe(testClass.getJavaClass());
    }

    @Override
    public void run(final RunNotifier notifier) {
        final Description description = getDescription();

        Statement statement = runChildren(description, notifier);
        for (final ClassStatementBuilder builder : statementBuilders) {
            statement = builder.createStatement(testClass, statement, description, notifier);
        }

        statementExecutor.execute(statement, notifier, description);
    }

    /**
     * This method returns a {@link Statement} that is responsible for running all children of the given test class. In
     * order to run more than one {@link Statement}, please use the {@link RunAll} statement for grouping.
     *
     * Note: Clients may override this method. The statement returned should only be responsible for running the
     * children. Extra work may be registered with the {@code statementBuilders} list which is initialized during the
     * call of {@link #initialize()}. Please register additional payload, e.g. the run of {@code @BeforeClass},
     * {@code @AfterClass} or {@code @ClassRule}, there. {@link de.bechte.junit.runners.context.statements.builder.ClassStatementBuilder}s will be called in the order they are
     * registered.
     *
     * @param description the {@link Description} of the class
     * @param notifier the {@link RunNotifier} used for this iteration
     * @return a {@link Statement} that runs all children
     */
    protected Statement runChildren(final Description description, final RunNotifier notifier) {
        return new RunAll(
            new RunChildren<FrameworkMethod>(testClass, methodRunner, methodResolver, notifier),
            new RunChildren<Class<?>>(testClass, contextRunner, contextResolver, notifier)
        );
    }
}
