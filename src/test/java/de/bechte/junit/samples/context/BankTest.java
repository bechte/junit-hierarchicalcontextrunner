package de.bechte.junit.samples.context;

import org.junit.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class BankTest {
    private static final double MONEY_DELTA = .00001;

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

    private static void assertMoneyEquals(String message, double expected, double actual) {
        assertEquals(message, expected, actual, MONEY_DELTA);
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
            assertMoneyEquals("Bank interest rate was not set", 2.75, Bank.currentInterestRate);
        }

        public class NewAccountContext {
            private Account newAccount;

            @Before
            public void createNewAccount() throws Exception {
                newAccount = new Account();
            }

            @Test
            public void balanceIsZero() throws Exception {
                assertMoneyEquals("Balance was not zero", 0.0, newAccount.getBalance());
            }

            @Test
            public void interestRateIsSet() throws Exception {
                assertMoneyEquals("Interest rate is not fixed", 2.75, newAccount.getInterestRate());
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
                    assertMoneyEquals("Interest rate is not fixed", 2.75, oldAccount.getInterestRate());
                }
            }
        }
    }
}
