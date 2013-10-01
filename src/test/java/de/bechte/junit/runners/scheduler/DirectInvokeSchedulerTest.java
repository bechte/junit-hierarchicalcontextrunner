package de.bechte.junit.runners.scheduler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DirectInvokeSchedulerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void givenADirectInvokeScheduler() throws Exception {
        directInvokeScheduler = new DirectInvokeScheduler();
    }

    private DirectInvokeScheduler directInvokeScheduler;

    @Test
    public void whenScheduleIsCalledWithNull_thenAnIllegalArgumentExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Runnable must not be null!");
        directInvokeScheduler.schedule(null);
    }

    @Test
    public void whenScheduleIsCalledWithRunnable_thenRunnableIsRun() throws Exception {
        Runnable runnable = mock(Runnable.class);
        directInvokeScheduler.schedule(runnable);
        verify(runnable).run();
    }
}