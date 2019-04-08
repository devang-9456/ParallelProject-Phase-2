package com.cg.wallet.dao;

import java.sql.SQLException;

import com.cg.wallet.dto.Account;
import com.cg.wallet.dto.Transactions;
import com.cg.wallet.exception.NotFoundException;

public interface AccountDao {

	public void connect() throws Exception;
	public void addAccount(Account acc) throws SQLException;
	public void addTransaction(Transactions tr) throws SQLException;
	public void updateTransaction(Transactions tr,Account acc) throws SQLException;
	public double showBalance(long accId) throws NotFoundException, SQLException;
	public double deposit(long accId, double amount) throws NotFoundException, SQLException;
	public double withdraw(long accId, double amount) throws NotFoundException, SQLException;
	public boolean fundTransfer(long accId1, double amount, long accId2) throws NotFoundException, SQLException;
	public void showTransactions(long accId) throws NotFoundException, SQLException;
	public Account showMyAccountInfo(long accId) throws NotFoundException, SQLException;
	public Account decBalance(long accId, double amt) throws SQLException;
	public Account incBalance(long accId, double amt) throws SQLException;
	public long getAccId();
	public int getTranSeq();
}
