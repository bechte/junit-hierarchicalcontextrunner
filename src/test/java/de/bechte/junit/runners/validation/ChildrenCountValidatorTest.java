package de.bechte.junit.runners.validation;

import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.ContextTestClassStub;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

import static de.bechte.junit.matchers.CollectionMatchers.empty;
import static de.bechte.junit.matchers.CollectionMatchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ChildrenCountValidatorTest {
    @Test
    public void givenClassWithContexts_ContextHierarchiesCountValidatorReportsNoError() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.class);

        List<Throwable> errors = new ArrayList<Throwable>();
        ChildrenCountValidator.CONTEXT_HIERARCHIES.validate(testClass, errors);

        assertThat(errors, is(empty()));
    }

    @Test
    public void givenClassWithoutContexts_ContextHierarchiesCountValidatorReportsAnError() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.A.class);

        List<Throwable> errors = new ArrayList<Throwable>();
        ChildrenCountValidator.CONTEXT_HIERARCHIES.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("No contexts found!")));
    }
    @Test
    public void givenClassWithMethods_TestMethodsCountValidatorReportsNoError() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.class);

        List<Throwable> errors = new ArrayList<Throwable>();
        ChildrenCountValidator.TEST_METHODS.validate(testClass, errors);

        assertThat(errors, is(empty()));
    }

    @Test
    public void givenClassWithoutMethods_TestMethodsCountValidatorReportsAnError() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.B.class);

        List<Throwable> errors = new ArrayList<Throwable>();
        ChildrenCountValidator.TEST_METHODS.validate(testClass, errors);

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is(equalTo("No tests found!")));
    }
}