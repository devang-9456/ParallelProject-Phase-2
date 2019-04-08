package com.cg.wallet.service;

import java.sql.SQLException;

import com.cg.wallet.dto.Account;
import com.cg.wallet.dto.Transactions;

public interface AccountService {

	public void connect() throws Exception;
	public void createAccount(Account acc) throws SQLException;
	public void createTransaction(Transactions tr) throws SQLException;
	public double showBalance(long accId);
	public double deposit(long accId, double amount);
	public double withdraw(long accId, double amount);
	public boolean fundTransfer(long accId1, double amount, long accId2);
	public void showTransactionByAccountId(long accId);
	public Account showMyAccountInfo(long accId);
	public long getAccId();
	public int getTranSeq();
}
