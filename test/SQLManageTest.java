package atm;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLManageTest {

    static SQLManage sql;
    static int testUserId;
    static final String testCard = "9999888877776666";
    static final String testPin = "5555";

    @BeforeAll
    static void init() throws SQLException {
        sql = new SQLManage();
        // Add a new test user
        sql.adding(testCard, testPin, "Test User", "1000");

        // Fetch the ID of the newly added user
        ResultSet rs = sql.check(testCard, testPin);
        if (rs.next()) {
            testUserId = rs.getInt("id");
        }
    }

    @Test
    @Order(1)
    void testCheckValidUser() throws SQLException {
        ResultSet rs = sql.check(testCard, testPin);
        assertTrue(rs.next(), "Valid user should be found");
    }

    @Test
    @Order(2)
    void testCheckInvalidUser() throws SQLException {
        ResultSet rs = sql.check("invalidcard", "0000");
        assertFalse(rs.next(), "Invalid user should not be found");
    }

    @Test
    @Order(3)
    void testBalanceCheck() throws SQLException {
        int balance = sql.balCheck(testUserId);
        assertTrue(balance >= 0, "Balance should be >= 0");
    }

    @Test
    @Order(4)
    void testDeposit() throws SQLException {
        int before = sql.balCheck(testUserId);
        sql.deposit(500, testUserId);
        int after = sql.balCheck(testUserId);
        assertEquals(before + 500, after, "Balance should increase after deposit");
    }

    @Test
    @Order(5)
    void testWithdrawSufficientBalance() throws SQLException {
        int before = sql.balCheck(testUserId);
        int amount = 300;

        if (before >= amount) {
            int result = sql.withdraw(amount, testUserId);
            int after = sql.balCheck(testUserId);
            assertEquals(1, result, "Withdraw should return 1 (success)");
            assertEquals(before - amount, after, "Balance should decrease");
        } else {
            fail("Not enough balance for withdrawal test");
        }
    }

    @Test
    @Order(6)
    void testWithdrawInsufficientBalance() throws SQLException {
        int balance = sql.balCheck(testUserId);
        int result = sql.withdraw(balance + 1000, testUserId);
        assertEquals(0, result, "Withdraw should fail due to insufficient balance");
    }

    @Test
    @Order(7)
    void testPinChange() throws SQLException {
        String newPin = "4321";
        sql.pinchange(newPin, testUserId);
        ResultSet rs = sql.check(testCard, newPin);
        assertTrue(rs.next(), "PIN change should succeed");

        // Restore original pin for repeatability
        sql.pinchange(testPin, testUserId);
    }

    @AfterAll
    static void cleanup() throws SQLException {
        Statement stmt = sql.con.createStatement();
        stmt.executeUpdate("DELETE FROM users WHERE id = " + testUserId);
        stmt.executeUpdate("DELETE FROM transactions WHERE id = " + testUserId);
    }
}
