package de.bechte.junit.stubs;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

public class ContextTestClassWithIgnoreStub {
    public class A {
        @Test
        public void innerTestMethod() throws Exception {
        }
    }

    @Ignore
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
