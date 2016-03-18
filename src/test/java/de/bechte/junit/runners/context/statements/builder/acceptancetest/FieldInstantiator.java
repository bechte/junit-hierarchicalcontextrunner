package de.bechte.junit.runners.context.statements.builder.acceptancetest;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;

public class FieldInstantiator implements MethodRule {
    public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Field fieldToBeSet = target.getClass().getDeclaredField("fieldToBeSet");
                fieldToBeSet.setAccessible(true);
                fieldToBeSet.set(target, new Object());
                fieldToBeSet.setAccessible(false);
                base.evaluate();
            }
        };
    }
}
