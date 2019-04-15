package de.bechte.junit.runners.context.processing;

import de.bechte.junit.runners.context.description.ContextDescriber;
import de.bechte.junit.stubs.ContextTestClassStub;
import de.bechte.junit.stubs.ContextTestClassWithInheritedAbstractInnerClassStub;
import de.bechte.junit.stubs.EmptyTestClassStub;
import de.bechte.junit.stubs.SimpleTestClassStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContextExecutorTest {
    @Mock
    private ContextDescriber describer;

    @Mock
    private RunNotifier notifier;

    private ContextExecutor executor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        executor = new ContextExecutor(describer);
    }

    @Test
    public void verifyNotificationsForExecutionOf_SimpleTestClass() throws Exception {
        executor.run(null, SimpleTestClassStub.class, notifier);

        verify(notifier, times(2)).fireTestStarted(any(Description.class));
        verify(notifier, times(1)).fireTestFailure(any(Failure.class));
        verify(notifier, times(2)).fireTestFinished(any(Description.class));
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void verifyNotificationsForExecutionOf_ContextTestClass() throws Exception {
        executor.run(null, ContextTestClassStub.class, notifier);

        verify(notifier, times(3)).fireTestStarted(any(Description.class));
        verify(notifier, times(1)).fireTestFailure(any(Failure.class));
        verify(notifier, times(3)).fireTestFinished(any(Description.class));
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void verifyNotificationsForExecutionOf_EmptyTestClass() throws Exception {
        executor.run(null, EmptyTestClassStub.class, notifier);

        verify(notifier, times(2)).fireTestFailure(any(Failure.class));
        verifyNoMoreInteractions(notifier);
    }

    @Test
    public void verifyNotificationsForExecutionOf_ContextTestClassWithInheritedAbstractInnerClassStub() throws Exception {
        executor.run(null, ContextTestClassWithInheritedAbstractInnerClassStub.class, notifier);

        verify(notifier, times(3)).fireTestStarted(any(Description.class));
        verify(notifier, times(0)).fireTestFailure(any(Failure.class));
        verify(notifier, times(3)).fireTestFinished(any(Description.class));
        verifyNoMoreInteractions(notifier);
    }

}