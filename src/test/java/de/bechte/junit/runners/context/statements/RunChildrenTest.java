package de.bechte.junit.runners.context.statements;

import de.bechte.junit.runners.context.processing.ChildExecutor;
import de.bechte.junit.runners.context.processing.ChildResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunChildrenTest {
    @Mock
    private TestClass testClass;

    @Mock
    private ChildExecutor<Object> childExecutor;

    @Mock
    private ChildResolver<Object> childResolver;

    @Mock
    private RunNotifier notifier;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void verifyChildExecutorIsCalledForEachChild() throws Throwable {
        Object child1 = new Object();
        Object child2 = new Object();
        Object child3 = new Object();

        when(childResolver.getChildren(testClass)).thenReturn(Arrays.asList(child1, child2, child3));
        new RunChildren<Object>(testClass, childExecutor, childResolver, notifier).evaluate();

        verify(childExecutor).run(testClass, child1, notifier);
        verify(childExecutor).run(testClass, child2, notifier);
        verify(childExecutor).run(testClass, child3, notifier);
    }
}