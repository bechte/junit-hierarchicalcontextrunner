package de.bechte.junit.runners.validation;

import de.bechte.junit.stubs.validation.AlwaysAddThrowableValidator;
import de.bechte.junit.stubs.validation.NeverAddThrowableValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static de.bechte.junit.matchers.CollectionMatchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BooleanValidatorTest {
    @Mock private TestClass testClass;
    @Spy private TestClassValidator passValidator1 = new NeverAddThrowableValidator();
    @Spy private TestClassValidator passValidator2 = new NeverAddThrowableValidator();
    @Spy private TestClassValidator failValidator1 = new AlwaysAddThrowableValidator();
    @Spy private TestClassValidator failValidator2 = new AlwaysAddThrowableValidator();
    @Spy private List<Throwable> errors = new ArrayList<Throwable>();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void givenAndValidator_allGivenValidatorsAreExecuted() throws Exception {
        BooleanValidator validator = BooleanValidator.AND(passValidator1, passValidator2);
        validator.validate(testClass, errors);

        verify(passValidator1).validate(same(testClass), anyList());
        verify(passValidator2).validate(same(testClass), anyList());
    }

    @Test
    public void givenAndValidator_allGivenErrorsAreGathered() throws Exception {
        BooleanValidator validator = BooleanValidator.AND(failValidator1, failValidator2);
        validator.validate(testClass, errors);

        assertThat(errors, hasSize(2));

        verify(failValidator1).validate(same(testClass), anyList());
        verify(failValidator2).validate(same(testClass), anyList());
    }

    @Test
    public void givenOrValidator_allGivenValidatorsAreExecutedIfNoneOfThemPasses() throws Exception {
        BooleanValidator validator = BooleanValidator.OR(failValidator1, failValidator2);
        validator.validate(testClass, errors);

        verify(failValidator1).validate(same(testClass), anyList());
        verify(failValidator2).validate(same(testClass), anyList());
    }

    @Test
    public void givenOrValidator_afterOneValidatorPasses_noMoreValidatorsAreExecuted() throws Exception {
        BooleanValidator validator = BooleanValidator.OR(failValidator1, passValidator1, failValidator2);
        validator.validate(testClass, errors);

        verify(failValidator1).validate(same(testClass), anyList());
        verify(passValidator1).validate(same(testClass), anyList());
        verify(failValidator2, never()).validate(same(testClass), anyList());
    }

    @Test
    public void givenOrValidator_ifNoValidatorPasses_allGivenErrorsAreGathered() throws Exception {
        BooleanValidator validator = BooleanValidator.OR(failValidator1, failValidator2);
        validator.validate(testClass, errors);

        assertThat(errors, hasSize(2));

        verify(failValidator1).validate(same(testClass), anyList());
        verify(failValidator2).validate(same(testClass), anyList());
    }
}
