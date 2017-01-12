package de.bechte.junit.stubs.statements.rules.acceptancetest;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.bechte.junit.runners.context.statements.builder.acceptancetest.FieldInstantiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(HierarchicalContextRunner.class)
public class TestClassWithMethodRuleDefinedOnInnerContext {
    private Object fieldToBeSet;

    public class Context {
        @Rule
        public FieldInstantiator rule = new FieldInstantiator();

        private Object fieldToBeSet;

        @Test
        public void innerTest() {
            assertNotNull(this.fieldToBeSet);
            assertNull("outer field should not be set:", TestClassWithMethodRuleDefinedOnInnerContext.this.fieldToBeSet);
        }
    }
}
