package de.bechte.junit.runners.context.processing;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodResolverTest {
    private MethodResolver resolver = new MethodResolver();

    @Test
    public void whenCalledWithNull_emptyListIsReturned() throws Exception {
        List<FrameworkMethod> children = resolver.getChildren(null);
        assertThat(children, is(empty()));
    }

    @Test
    public void verifyThatAllMethodsAnnotatedWithTestAreContainedInTheListOfChildren() throws Exception {
        TestClass testClass = mock(TestClass.class);
        List<FrameworkMethod> testMethods = mock(List.class);
        when(testClass.getAnnotatedMethods(Test.class)).thenReturn(testMethods);

        List<FrameworkMethod> children = resolver.getChildren(testClass);
        assertThat(children, is(testMethods));
    }
}