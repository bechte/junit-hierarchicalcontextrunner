package de.bechte.junit.samples.bank;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.rules.ExpectedException;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(HierarchicalContextRunner.class)
public class BankTest {
    private static final double MONEY_DELTA = .00001;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeClassFirstLevel() throws Exception {
        // Silly, just for demonstration, that before class works for the main class
        System.out.println("Setup Database, etc...");
    }

    @AfterClass
    public static void afterClassFirstLevel() throws Exception {
        // Silly, just for demonstration, that after class works for the main class
        System.out.println("Cleanup Database, etc...");
    }

    private static void assertMoneyEquals(double expected, double actual) {
        assertEquals(expected, actual, MONEY_DELTA);
    }

    public class BankContext {

        @Before
        public void setCurrentInterestRate() {
            Bank.currentInterestRate = 2.75;
        }

        @Test
        public void interestRateIsSet() {
            // Rather stupid test, but it shows, that tests
            // on this level get also executed smoothly...
            assertMoneyEquals(2.75, Bank.currentInterestRate);
        }

        public class NewAccountContext {
            private Account newAccount;

            @Before
            public void createNewAccount() throws Exception {
                newAccount = new Account();
            }

            @Test
            public void balanceIsZero() throws Exception {
                assertMoneyEquals(0.0, newAccount.getBalance());
            }

            @Test
            public void interestRateIsSet() throws Exception {
                assertMoneyEquals(2.75, newAccount.getInterestRate());
            }
        }

        public class OldAccountContext {
            private Account oldAccount;

            @Before
            public void createOldAccount() throws Exception {
                oldAccount = new Account();
            }

            public class AfterInterestRateChangeContext {
                @Before
                public void changeInterestRate() {
                    Bank.currentInterestRate = 3.25;
                }

                @Test
                public void shouldHaveOldInterestRate() throws Exception {
                    assertMoneyEquals(2.75, oldAccount.getInterestRate());
                }
            }

        }

        public class FailingAndIgnoredTestContext {
            @Test
            public void failingTest() throws Exception {
                fail("I always fail!");
            }

            @Test
            public void testExpectingAnExceptionWithRule() throws Exception {
                exception.expect(Exception.class);
            }

            @Test
            @Ignore
            public void ignoredTest() throws Exception {
                System.out.println("I should never be executed!");
            }
        }
    }
}
