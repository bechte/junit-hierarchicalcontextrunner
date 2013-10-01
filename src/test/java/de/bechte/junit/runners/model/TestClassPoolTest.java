package de.bechte.junit.runners.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.TestClass;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestClassPoolTest {
    private static final Class TEST_CLASS = Object.class;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenForClassIsCalledWithNull_thenAnIllegalArgumentExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("TestClass must not be null!");
        TestClassPool.forClass(null);
    }

    @Test
    public void whenForClassIsCalledWithAClass_thenATestClassObjectForThatClassIsReturned() throws Exception {
        TestClass testClass = TestClassPool.forClass(TEST_CLASS);
        assertThat(testClass, is(notNullValue()));
        assertThat((Class)testClass.getJavaClass(), is(equalTo(TEST_CLASS)));
    }

    @Test
    public void whenForClassIsCalledWithAClassTwice_theSameTestClassObjectIsReturned() throws Exception {
        TestClass testClass1 = TestClassPool.forClass(TEST_CLASS);
        TestClass testClass2 = TestClassPool.forClass(TEST_CLASS);
        assertThat(testClass1, is(equalTo(testClass2)));
    }
}