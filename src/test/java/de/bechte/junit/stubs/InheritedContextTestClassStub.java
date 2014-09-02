package de.bechte.junit.stubs;

import org.junit.Test;

public class InheritedContextTestClassStub extends ContextTestClassStub {
    public class I {
        @Test
        public void innerTestMethod() throws Exception {
        }
    }

    @Test
    public void outerTestMethod() throws Exception {
    }
}