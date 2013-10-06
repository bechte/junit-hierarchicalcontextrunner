package de.bechte.junit.stubs;

import org.junit.Test;

import static org.junit.Assert.fail;

public class ContextTestClassStub {
    public class A {
        @Test
        public void innerTestMethod() throws Exception {
        }
    }

    public class B {
        public class C {
            @Test
            public void failingTestMethod() throws Exception {
                fail("Failing!");
            }
        }
    }

    @Test
    public void outerTestMethod() throws Exception {
    }
}
