package de.bechte.junit.stubs.singletests;

import org.junit.Test;

public class SingleTestWithExpectedAnnotation {
    @Test(expected = IllegalArgumentException.class)
    public void someTest() {
        throw new IllegalArgumentException("Illegal Argument!");
    }
}
