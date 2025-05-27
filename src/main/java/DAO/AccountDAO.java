package DAO;

import Model.Account;

public interface AccountDAO {

    int addAccount(Account newAccount);

    Account getAccountByUsername(String username);

    Account getAccountById(int accountId);

}
