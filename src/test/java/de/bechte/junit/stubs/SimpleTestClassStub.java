package de.bechte.junit.stubs;

import org.junit.Test;

import static org.junit.Assert.fail;

public class SimpleTestClassStub {
    @Test
    public void testMethod() throws Exception {
    }

    @Test
    public void failingTestMethod() throws Exception {
        fail("Failing!");
    }
}
