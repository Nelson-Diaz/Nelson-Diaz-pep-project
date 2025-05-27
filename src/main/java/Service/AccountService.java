package Service;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import Model.Account;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = AccountDAOImpl.getInstance();
    }

    public Account addAccount(Account newAccount) {
        // Validate account information
        String username = newAccount.getUsername();
        boolean usernameValid = (username != null && username.length() > 0);

        String password = newAccount.getPassword();
        boolean passwordValid = (password != null && password.length() > 4);

        Account existingUser = accountDAO.getAccountByUsername(username);

        // Early return if new user is invalid
        if(!usernameValid || !passwordValid || (existingUser != null)) {
            return null;
        }

        // Add account
        int id = accountDAO.addAccount(newAccount);

        if(id >= 0) {
            return accountDAO.getAccountById(id);
        } else {
            return null;
        }
    }

    public Account login(String username, String password) {
        Account foundAccount = accountDAO.getAccountByUsername(username);
        if(foundAccount == null) {
            return null;
        }

        String foundPassword = foundAccount.getPassword();
        if(!foundPassword.equals(password)) {
            return null;
        }

        return foundAccount;
    }

    public Account getAccount(int accountId) {
        return accountDAO.getAccountById(accountId);
    }
}
