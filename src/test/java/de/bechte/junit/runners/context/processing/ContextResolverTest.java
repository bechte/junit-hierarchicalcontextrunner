package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.Class1stLevel;
import de.bechte.junit.stubs.ContextTestClassStub;
import de.bechte.junit.stubs.EmptyTestClassStub;
import de.bechte.junit.stubs.SimpleTestClassStub;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ContextResolverTest {
    private ContextResolver resolver = new ContextResolver();

    @Test
    public void whenCalledWithNull_emptyListIsReturned() throws Exception {
        List<Class<?>> children = resolver.getChildren(null);
        assertThat(children, is(empty()));
    }

    @Test
    public void whenCalledWithEmptyTestClass_emptyListIsReturned() throws Exception {
        TestClass testClass = TestClassPool.forClass(EmptyTestClassStub.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, is(empty()));
    }

    @Test
    public void verifyThatTestMethodsAreNotContainedInListOfChildren() throws Exception {
        TestClass testClass = TestClassPool.forClass(SimpleTestClassStub.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, is(empty()));
    }

    @Test
    public void verifyThatSubContextsAreContainedInListOfChildren() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassStub.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, hasSize(2));
        assertThat(children, hasItems(ContextTestClassStub.A.class, ContextTestClassStub.B.class));
    }

    @Test
    public void verifyThatOnlyPublicMemberClassesAreContainedInListOfChildren() throws Exception {
        TestClass testClass = TestClassPool.forClass(Class1stLevel.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, hasSize(1));
        assertThat(children, hasItem(Class1stLevel.Class2ndLevel.class));
    }
}