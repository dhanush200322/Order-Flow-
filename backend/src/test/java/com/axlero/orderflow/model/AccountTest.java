package com.axlero.orderflow.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Should create an Account with all required fields")
    void testAccountCreation() {
        long now = System.currentTimeMillis();
        Account account = new Account("ACC-01", "Dhanush Trading", 10000.0, now);

        assertEquals("ACC-01", account.accountId());
        assertEquals("Dhanush Trading", account.accountName());
        assertEquals(10000.0, account.balance());
        assertEquals(now, account.createdAt());
    }

    @Test
    @DisplayName("Should reject invalid account arguments")
    void testInvalidAccountArguments() {
        long now = System.currentTimeMillis();
        assertThrows(IllegalArgumentException.class, () ->
            new Account(null, "Dhanush Trading", 10000.0, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Account("", "Dhanush Trading", 10000.0, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Account("ACC-01", null, 10000.0, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Account("ACC-01", " ", 10000.0, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Account("ACC-01", "Dhanush Trading", -500.0, now));
    }
}
