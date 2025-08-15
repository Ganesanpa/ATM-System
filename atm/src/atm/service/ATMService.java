package atm.service;

import atm.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ATMService {
    private final UserRepository repo;

    public ATMService() throws SQLException {
        repo = new UserRepository();
    }

    public boolean validateLogin(String card, String pin) throws SQLException {
        ResultSet rs = repo.checkUser(card, pin);
        return rs.next();
    }

    public void deposit(int userId, int amount) throws SQLException {
        int current = repo.getBalance(userId);
        repo.updateBalance(userId, current + amount);
    }

    public boolean withdraw(int userId, int amount) throws SQLException {
        int current = repo.getBalance(userId);
        if (current >= amount) {
            repo.updateBalance(userId, current - amount);
            return true;
        }
        return false;
    }

    public int getBalance(int userId) throws SQLException {
        return repo.getBalance(userId);
    }

    public void changePin(int userId, String newPin) throws SQLException {
        repo.updatePin(userId, newPin);
    }

    public void close() throws SQLException {
        repo.close();
    }
}
