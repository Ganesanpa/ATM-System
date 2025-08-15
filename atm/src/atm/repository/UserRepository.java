package atm.repository;

import java.sql.*;

public class UserRepository {
    private Connection con;

    public UserRepository() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/atm";
        String user = "root";
        String pass = "Bankai@123";
        con = DriverManager.getConnection(url, user, pass);
    }

    public ResultSet checkUser(String card, String pin) throws SQLException {
        String query = "SELECT * FROM users WHERE card = ? AND pin = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, card);
        stmt.setString(2, pin);
        return stmt.executeQuery();
    }

    public void addUser(String card, String pin, String name, int balance) throws SQLException {
        String query = "INSERT INTO users (card, pin, uname, bal) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, card);
        stmt.setString(2, pin);
        stmt.setString(3, name);
        stmt.setInt(4, balance);
        stmt.executeUpdate();
    }

    public void updatePin(int userId, String newPin) throws SQLException {
        String query = "UPDATE users SET pin = ? WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, newPin);
        stmt.setInt(2, userId);
        stmt.executeUpdate();
    }

    public int getBalance(int userId) throws SQLException {
        String query = "SELECT bal FROM users WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("bal") : -1;
    }

    public void updateBalance(int userId, int amount) throws SQLException {
        String query = "UPDATE users SET bal = ? WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, amount);
        stmt.setInt(2, userId);
        stmt.executeUpdate();
    }

    public void close() throws SQLException {
        if (con != null) con.close();
    }
}
