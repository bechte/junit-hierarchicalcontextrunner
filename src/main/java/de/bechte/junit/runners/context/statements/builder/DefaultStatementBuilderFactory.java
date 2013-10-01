package de.bechte.junit.runners.context.statements.builder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@link DefaultStatementBuilderFactory} resolves a {@link List} of {@link ClassStatementBuilder}s for classes and
 * {@link MethodStatementBuilder}s for methods. These two types might differ, as they require different processing.
 */
public class DefaultStatementBuilderFactory extends StatementBuilderFactory {
    private List<ClassStatementBuilder> classStatementBuilders;
    private List<MethodStatementBuilder> methodStatementBuilders;

    public DefaultStatementBuilderFactory() {
        super();

        classStatementBuilders = Collections.unmodifiableList(getClassStatementBuilders());
        methodStatementBuilders = Collections.unmodifiableList(getMethodStatementBuilders());
    }

    protected List<ClassStatementBuilder> getClassStatementBuilders() {
        final List<ClassStatementBuilder> builders = new LinkedList<ClassStatementBuilder>();
        builders.add(new BeforeClassStatementBuilder());
        builders.add(new AfterClassStatementBuilder());
        builders.add(new ClassRuleStatementBuilder());
        return builders;
    }

    protected List<MethodStatementBuilder> getMethodStatementBuilders() {
        final List<MethodStatementBuilder> builders = new LinkedList<MethodStatementBuilder>();
        builders.add(new ExpectExceptionStatementBuilder());
        builders.add(new FailOnTimeoutStatementBuilder());
        builders.add(new HierarchicalRunBeforeStatementBuilder());
        builders.add(new HierarchicalRunAfterStatementBuilder());
        builders.add(new HierarchicalRunRulesStatementBuilder());
        return builders;
    }

    public List<ClassStatementBuilder> getBuildersForClasses() {
        return classStatementBuilders;
    }

    public List<MethodStatementBuilder> getBuildersForMethods() {
        return methodStatementBuilders;
    }
}