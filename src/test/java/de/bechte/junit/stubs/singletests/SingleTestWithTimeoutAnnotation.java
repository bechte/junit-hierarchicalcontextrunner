package de.bechte.junit.stubs.singletests;

import org.junit.Test;

public class SingleTestWithTimeoutAnnotation {
    @Test(timeout = 1000)
    public void someTest() {
    }
}
