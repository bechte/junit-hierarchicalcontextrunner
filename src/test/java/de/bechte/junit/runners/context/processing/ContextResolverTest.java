package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.model.TestClassPool;
import de.bechte.junit.stubs.*;
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

    @Test
    public void verifyThatInheritedSubContextsAreContainedInListOfChildren() throws Exception {
        TestClass testClass = TestClassPool.forClass(InheritedContextTestClassStub.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, hasSize(3));
        assertThat(children, hasItems(InheritedContextTestClassStub.A.class, InheritedContextTestClassStub.B.class, InheritedContextTestClassStub.I.class));
    }

    @Test
    public void verifyThatIgnoredSubContextsAreNotContainedInListOfChildren() throws Exception {
        TestClass testClass = TestClassPool.forClass(ContextTestClassWithIgnoreStub.class);
        List<Class<?>> children = resolver.getChildren(testClass);
        assertThat(children, hasSize(1));
        assertThat(children, hasItem(ContextTestClassWithIgnoreStub.A.class));
    }
}