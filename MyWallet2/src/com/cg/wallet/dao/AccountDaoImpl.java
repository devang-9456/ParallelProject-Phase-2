package com.cg.wallet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cg.wallet.dto.Account;
import com.cg.wallet.dto.Transactions;
import com.cg.wallet.exception.NotFoundException;
import com.cg.wallet.util.StaticDB;

public class AccountDaoImpl implements AccountDao {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	Connection con = null;

	public void connect() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "hr");
	}
	
	@Override
	public void addAccount(Account acc) throws SQLException {
		String query1 = "select max(Account_NUMBER) from bank_accounts";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long accountNumber = 0;
		if (rs1.next())
			accountNumber = rs1.getLong(1);
		acc.setaccNo(accountNumber + 1);
		st1.close();

		String query = "insert into bank_accounts values (?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, "" + acc.getaccNo());
		pst.setString(2, acc.getAccHolderName());
		pst.setString(3, "" + acc.getBalance());
		pst.executeUpdate();

		pst.close();
		System.out.println(acc.getaccNo()+"  Account added.");
	}

	@Override
	public void addTransaction(Transactions tr) throws SQLException {
		Account acc = new Account();
		String query1 = "select max(Account_NUMBER) from bank_accounts";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long accountNumber = 0;
		if (rs1.next())
			accountNumber = rs1.getLong(1);
		acc.setaccNo(accountNumber);
		st1.close();
		
		String query2 = "select max(TRANSACTION_NUMBER) from bank_transaction";
		Statement st2 = con.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		Integer transactionSeq = 0;
		if (rs2.next())
			transactionSeq = rs2.getInt(1);
		tr.setTransSeq(transactionSeq + 1);
		st2.close();
		
		Long tId=(acc.getaccNo())*10+1;
		tr.setTransId(tId);
		String query = "insert into bank_transaction values (?,?,?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, tr.getTransSeq());
		pst.setLong(2, tr.getTransId());
		pst.setString(3, tr.gettType());
		pst.setDouble(4, tr.getAmount());
		pst.setDouble(5, tr.getBalance());
		pst.setString(6, tr.getDate());
		pst.executeUpdate();
		pst.close();
		System.out.println("Transaction added");
	}
	
	public void updateTransaction(Transactions tr,Account acc) throws SQLException { 
		String query2 = "select max(TRANSACTION_NUMBER) from bank_transaction";
		Statement st2 = con.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		Integer transactionSeq = 0;
		if (rs2.next())
			transactionSeq = rs2.getInt(1);
		tr.setTransSeq(transactionSeq + 1);
		st2.close();
		
		Long tId=(acc.getaccNo())*10+1;
		tr.setTransId(tId);
		String query = "insert into bank_transaction values (?,?,?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, tr.getTransSeq());
		pst.setLong(2, tr.getTransId());
		pst.setString(3, tr.gettType());
		pst.setDouble(4, tr.getAmount());
		pst.setDouble(5, tr.getBalance());
		pst.setString(6, tr.getDate());
		pst.executeUpdate();
		pst.close();
		System.out.println("Transaction added");
	}

	@Override
	public double showBalance(long accId) throws NotFoundException, SQLException {

		Account account = incBalance(accId, 0.00);
		if(account == null) {
			throw new NotFoundException("Account doesn't exist.");
		}
		return account.getBalance();
	}

	@Override
	public double deposit(long accId, double amount) throws NotFoundException, SQLException {
		Account acc = incBalance(accId, amount);
		if(acc == null) {
			throw new NotFoundException("Account doesn't exist.");
		}
		long tId=accId*10+1;
		LocalDateTime now = LocalDateTime.now(); 
		Transactions ts=new Transactions(++StaticDB.tSeq, tId, "Deposit", amount, acc.getBalance(), dtf.format(now));
		try {
			updateTransaction(ts,acc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query2 = "update bank_accounts set account_balance = " + acc.getBalance() + " where Account_number = "
				+ acc.getaccNo();
		PreparedStatement pst = con.prepareStatement(query2);
		pst.executeUpdate();
		System.out.println("Deposit Successful");
		pst.close();
		return acc.getBalance();
	}

	@Override
	public double withdraw(long accId, double amount) throws NotFoundException, SQLException {
		Account acc = decBalance(accId, amount);
		if(acc == null) {
			throw new NotFoundException("Account doesn't exist.");
		}
		long tId=accId*10+1;
		LocalDateTime now = LocalDateTime.now();
		Transactions ts=new Transactions(++StaticDB.tSeq, tId, "Withdraw", amount, acc.getBalance(), dtf.format(now));
		try {
			updateTransaction(ts,acc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query2 = "update bank_accounts set account_balance = " + acc.getBalance() + " where Account_number = "
				+ acc.getaccNo();
		PreparedStatement pst = con.prepareStatement(query2);
		pst.executeUpdate();
		System.out.println("Withdraw Successful");
		pst.close();
		return acc.getBalance();
	}

	@Override
	public boolean fundTransfer(long accId1, double amount, long accId2) throws NotFoundException, SQLException {
		Account account1 = decBalance(accId1, amount);
		Account account2 = incBalance(accId2, amount);
		if(account1 == null||account2 == null) {
			throw new NotFoundException("Account doesn't exist.");
		}
			long tId1 = accId1*10+1;
			long tId2 = accId2*10+1;
			LocalDateTime now = LocalDateTime.now();
			Transactions ts1 = new Transactions(++StaticDB.tSeq, tId1,"Transfer",amount,account1.getBalance(),dtf.format(now));
			try {
				updateTransaction(ts1,account1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Transactions ts2 = new Transactions(++StaticDB.tSeq, tId2,"Transfer",amount,account2.getBalance(),dtf.format(now));
			try {
				updateTransaction(ts2,account2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	}

	@Override
	public void showTransactions(long accId) throws NotFoundException, SQLException {
		String query = "select * from bank_transaction where transaction_id="+((accId*10)+1);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			String userData = rs.getInt(1) + " : " + rs.getLong(2) + " : " + rs.getString(3) + " : " + rs.getDouble(4)
					+ " : " + rs.getDouble(5) + " : " + rs.getString(6);
			System.out.println(userData);
		}
		st.close();
	}

	@Override
	public Account showMyAccountInfo(long accId) throws NotFoundException, SQLException {
		Account account = incBalance(accId, 0.00);
		if(account == null) {
			throw new NotFoundException("Account doesn't exist.");
		}
		return account;
	}

	@Override
	public Account decBalance(long accId, double amt) throws SQLException {
		String query = "select * from bank_accounts where account_Number=" + accId;
		Account acc = new Account();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setaccNo(rs.getLong(1));
		acc.setBalance(rs.getDouble(3) - amt);
		st.close();
		return acc;
	}

	@Override
	public Account incBalance(long accId, double amt) throws SQLException {
		String query = "select * from bank_accounts where account_Number=" + accId;
		Account acc = new Account();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setaccNo(rs.getLong(1));
		;
		acc.setBalance(rs.getDouble(3) + amt);
		acc.setAccHolderName(rs.getString(2));
		st.close();
		return acc;
	}

	@Override
	public long getAccId() {
		// TODO Auto-generated method stub
		return ++StaticDB.accId;
	}

	@Override
	public int getTranSeq() {
		// TODO Auto-generated method stub
		return ++StaticDB.tSeq;
	}

}
