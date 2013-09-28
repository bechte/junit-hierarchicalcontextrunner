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
import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.runners.validation.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
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

    public HierarchicalContextRunner(final Class<?> clazz) throws InitializationError {
        this.testClass = TestClassPool.forClass(clazz);

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
     * Returns a {@link List} of {@link TestClassValidator}s that validate the {@link TestClass} instance after the
     * {@link HierarchicalContextRunner} has been created for the corresponding {@link Class}.
     *
     * Note: Clients may override this method to add or remove validators.
     *
     * @return a {@link List} of {@link TestClassValidator}s
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
                RuleValidator.CLASS_RULE_METHOD_VALIDATOR,
                RuleValidator.RULE_VALIDATOR,
                RuleValidator.RULE_METHOD_VALIDATOR
        );
    }

    /**
     * Initializes all dependencies for the {@link HierarchicalContextRunner}.
     *
     * Note: Clients may override this method to provide other dependencies.
     */
    protected void initialize() {
        methodResolver = new MethodResolver();
        methodDescriber = new MethodDescriber();
        methodRunner = new MethodExecutor(methodDescriber);

        contextResolver = new ContextResolver();
        contextDescriber = new ContextDescriber(contextResolver, methodResolver, methodDescriber);
        contextRunner = new ContextExecutor(contextDescriber);
    }

    @Override
    public Description getDescription() {
        return contextDescriber.describe(testClass.getJavaClass());
    }

    @Override
    public void run(final RunNotifier notifier) {
        final Statement statement = buildStatement(notifier);
        new StatementExecutor().execute(statement, notifier, getDescription());
    }

    protected Statement buildStatement(RunNotifier notifier) {
        Statement statement = childrenInvoker(testClass, notifier);
        statement = withBeforeClasses(testClass, statement);
        statement = withAfterClasses(testClass, statement);
        statement = withClassRules(testClass, statement);
        return statement;
    }

    protected Statement childrenInvoker(final TestClass testClass, final RunNotifier notifier) {
        return new RunAll(
            new RunChildren<FrameworkMethod>(testClass, methodRunner, methodResolver, notifier),
            new RunChildren<Class<?>>(testClass, contextRunner, contextResolver, notifier)
        );
    }

    protected Statement withBeforeClasses(final TestClass testClass, final Statement next) {
        List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? next : new RunBefores(next, befores, null);
    }

    protected Statement withAfterClasses(final TestClass testClass, final Statement next) {
        List<FrameworkMethod> afters = testClass.getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? next : new RunAfters(next, afters, null);
    }

    protected Statement withClassRules(final TestClass testClass, final Statement next) {
        final List<TestRule> classRules = new ArrayList<TestRule>();
        classRules.addAll(testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class));
        classRules.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return classRules.isEmpty() ? next : new RunRules(next, classRules, contextDescriber.describe(testClass.getJavaClass()));
    }
}
