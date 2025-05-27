package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {
    
    // Singleton Class
    private static AccountDAO instance;

    public static AccountDAO getInstance() {
        if(instance == null) {
            instance = new AccountDAOImpl();
        }
        return instance;
    }

    private Connection connection;

    private AccountDAOImpl() {
        this.connection = ConnectionUtil.getConnection();
    }

    @Override
    public int addAccount(Account newAccount) {
        try {
            String[] generatedColumn = {"account_id"};

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?)", generatedColumn);

            stmt.setString(1, newAccount.getUsername());
            stmt.setString(2, newAccount.getPassword());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            while(rs.next()) {
                int id = rs.getInt(1);

                return id;
            }

            // Reach here if insert failed
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Account getAccountByUsername(String username) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM account WHERE username=?");

            stmt.setString(1, username);

            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                int newId = result.getInt("account_id");
                String newUsername = result.getString("username");
                String newPassword = result.getString("password");

                Account newAccount = new Account(newId, newUsername, newPassword);
                
                return newAccount;
            }

            // Return here if no record was found
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Account getAccountById(int accountId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM account WHERE account_id=?");

            stmt.setInt(1, accountId);

            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                int resultId = result.getInt("account_id");
                String resultUsername = result.getString("username");
                String resultPassword = result.getString("password");

                Account newAccount = new Account(resultId, resultUsername, resultPassword);
                
                return newAccount;
            }

            // Return here if no record was found
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
