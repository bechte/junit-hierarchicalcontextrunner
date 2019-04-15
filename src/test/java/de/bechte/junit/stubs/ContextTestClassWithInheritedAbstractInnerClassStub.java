package de.bechte.junit.stubs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ContextTestClassWithInheritedAbstractInnerClassStub {
    public abstract class A {
        @Before
        public void setup() throws Exception {
        }

        @Test
        public void runTestTwice() throws Exception {
        }
    }

    public class B extends A {
        @Test
        public void runTestOnce() throws Exception {
        }
    }

    public class C extends A {
    }
}
