package atm;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ATMIntegrationTest {

    static SQLManage sql;
    static int userId;
    static final String card = "5555666677778888";
    static final String pin = "1111";

    @BeforeAll
    static void setup() throws SQLException {
        sql = new SQLManage();
        sql.adding(card, pin, "IntegrationTestUser", "1000");

        ResultSet rs = sql.check(card, pin);
        if (rs.next()) {
            userId = rs.getInt("id");
        } else {
            fail("Test user creation failed");
        }
    }

    @Test
    @Order(1)
    void testLoginAndBalance() throws SQLException {
        ResultSet rs = sql.check(card, pin);
        assertTrue(rs.next(), "Login should succeed");

        int balance = sql.balCheck(userId);
        assertTrue(balance >= 1000, "Initial balance should be >= 1000");
    }

    @Test
    @Order(2)
    void testDepositAndVerify() throws SQLException {
        int before = sql.balCheck(userId);
        sql.deposit(300, userId);
        int after = sql.balCheck(userId);
        assertEquals(before + 300, after, "Balance should reflect deposit");
    }

    @Test
    @Order(3)
    void testWithdrawAndVerify() throws SQLException {
        int before = sql.balCheck(userId);
        if (before >= 200) {
            int result = sql.withdraw(200, userId);
            assertEquals(1, result, "Withdraw should succeed");
            int after = sql.balCheck(userId);
            assertEquals(before - 200, after, "Balance should decrease");
        } else {
            fail("Not enough balance for withdrawal test");
        }
    }

    @Test
    @Order(4)
    void testMiniStatementEntries() throws SQLException {
        ResultSet rs = sql.stmt(userId);
        assertTrue(rs.next(), "Mini statement should have at least one record");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        Statement stmt = sql.con.createStatement();
        stmt.executeUpdate("DELETE FROM users WHERE id = " + userId);
        stmt.executeUpdate("DELETE FROM transactions WHERE id = " + userId);
    }
}
