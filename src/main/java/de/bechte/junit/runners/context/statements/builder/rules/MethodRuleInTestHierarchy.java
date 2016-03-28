package de.bechte.junit.runners.context.statements.builder.rules;

import org.junit.rules.MethodRule;

public class MethodRuleInTestHierarchy {
    private final MethodRule methodRule;
    private final Object instance;

    public MethodRuleInTestHierarchy(MethodRule methodRule, Object instance) {
        this.methodRule = methodRule;
        this.instance = instance;
    }

    public MethodRule getMethodRule() {
        return methodRule;
    }

    public Object getObjectRepresentingHierarchyLevel() {
        return instance;
    }
}
