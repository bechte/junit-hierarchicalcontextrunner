package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.context.description.Describer;
import de.bechte.junit.runners.context.statements.StatementExecutor;
import de.bechte.junit.runners.context.statements.builder.MethodStatementBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.Stack;

import static de.bechte.junit.runners.util.ReflectionUtil.createDeepInstance;
import static de.bechte.junit.runners.util.ReflectionUtil.getClassHierarchy;

/**
 * The {@link MethodExecutor} is responsible for executing a test for the given {@link FrameworkMethod}.
 *
 * This implementation creates a new test instance along the context hierarchy, i.e. it starts by creating an instance
 * of the outer class and then creates instances of the inner classes until the actual test class to which the current
 * {@link FrameworkMethod} belongs to is created.
 *
 * Currently, the following annotations are supported by this executor:
 * {@link Before}, {@link After}, {@link ExpectException}, {@link FailOnTimeout}, {@link Rule}
 *
 * Note: All annotations are evaluated for the entire context hierarchy of the test instance. Example:
 *
 * <pre>
 *   public class A {
 *     &#064;Before public void setUpA() {...}
 *     &#064;After public void tearDownA() {...}
 *
 *     &#064;Test public void test1() {...}
 *
 *     public class B {
 *       &#064;Before public void setUpB() {...}
 *       &#064;After public void tearDownB() {...}
 *
 *       &#064;Test public void test2() {...}
 *     }
 *   }
 * </pre>
 *
 * When test2 is executed, the following methods are called in sequence:
 * setUpA(), setUpB(), test2(), tearDownB(), tearDownA()
 */
public class MethodExecutor implements ChildExecutor<FrameworkMethod> {
    protected Describer<FrameworkMethod> describer;
    protected StatementExecutor statementExecutor;
    protected List<MethodStatementBuilder> statementBuilders;

    public MethodExecutor(final Describer<FrameworkMethod> describer, final StatementExecutor statementExecutor,
                          final List<MethodStatementBuilder> statementBuilders) {
        this.describer = describer;
        this.statementExecutor = statementExecutor;
        this.statementBuilders = statementBuilders;
    }

    public void run(final TestClass testClass, final FrameworkMethod method, final RunNotifier notifier) {
        final Description description = describer.describe(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            try {
                final Stack<Class<?>> classHierarchy = getClassHierarchy(testClass.getJavaClass());
                final Object target = createDeepInstance(classHierarchy);

                Statement statement = buildStatement(testClass, method, target, description, notifier);
                for (final MethodStatementBuilder builder : statementBuilders) {
                    statement = builder.createStatement(testClass, method, target, statement, description, notifier);
                }

                statementExecutor.execute(statement, notifier, description);
            } catch (Throwable t) {
                statementExecutor.execute(new Fail(t), notifier, description);
            }
        }
    }

    protected Statement buildStatement(final TestClass testClass, final FrameworkMethod method, final Object target,
                                       final Description description, final RunNotifier notifier) {
        return new InvokeMethod(method, target);
    }
}