package de.bechte.junit.stubs.statements.rules.acceptancetest;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.bechte.junit.runners.context.statements.builder.acceptancetest.FieldInstantiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(HierarchicalContextRunner.class)
public class TestClassWithMethodRuleAndInnerContext {
    @Rule
    public FieldInstantiator rule = new FieldInstantiator();

    private Object fieldToBeSet;

    @Test
    public void outerTest() throws Exception {
        assertNotNull(fieldToBeSet);
    }

    public class Context {
        private Object fieldToBeSet;

        @Test
        public void innerTest() {
            assertNotNull(this.fieldToBeSet);
            assertNotNull(TestClassWithMethodRuleAndInnerContext.this.fieldToBeSet);
        }
    }
}
