package com.cg.wallet.dto;

public class Account {
	
	private Long accNo;
	private String accHolderName;
	private Double balance;
	
	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(Long accNo, String accHolderName, Double balance) {
		super();
		this.accNo = accNo;
		this.accHolderName = accHolderName;
		this.balance = balance;
	}

	public Long getaccNo() {
		return accNo;
	}

	public void setaccNo(Long accNo) {
		this.accNo = accNo;
	}

	public String getAccHolderName() {
		return accHolderName;
	}

	public void setAccHolderName(String accHolderName) {
		this.accHolderName = accHolderName;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account [Account No. = " + accNo + ", Account Holder = " + accHolderName
				+ ", Balance = " + balance + "]\n";
	}
	
}
