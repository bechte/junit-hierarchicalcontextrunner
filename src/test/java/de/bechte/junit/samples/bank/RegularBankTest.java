package de.bechte.junit.samples.bank;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class RegularBankTest {
    private Account account;

    @Before
    public void setCurrentInterestRate() {
        Bank.currentInterestRate = 2.75;
        account = new Account();
    }

    @Test
    public void givenNewAccount_balanceIsZero() throws Exception {
        assertMoneyEquals(0.0, account.getBalance());
    }

    @Test
    public void givenNewAccount_interestRateIsSet() throws Exception {
        assertMoneyEquals(2.75, account.getInterestRate());
    }

    @Test
    public void givenNewAccount_whenInterestRateChanges_shouldHaveOldInterestRate() throws Exception {
        Bank.currentInterestRate = 3.25;

        assertMoneyEquals(2.75, account.getInterestRate());
    }

    @Test
    public void whenInterestRateChanges_newAccountShouldHaveNewInterestRate() throws Exception {
        Bank.currentInterestRate = 3.25;
        Account newAccount = new Account();
        assertMoneyEquals(3.25, newAccount.getInterestRate());
    }

    private static final double MONEY_DELTA = .00001;

    private static void assertMoneyEquals(double expected, double actual) {
        assertEquals(expected, actual, MONEY_DELTA);
    }
}