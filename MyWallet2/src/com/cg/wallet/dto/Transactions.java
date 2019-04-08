package com.cg.wallet.dto;

public class Transactions {

	private Integer transSeq;
	private Long transId;
	private String tType;
	private Double amount;
	private Double balance;
	private String date;
	
	public Transactions() {
		// TODO Auto-generated constructor stub
	}

	public Transactions(Integer transSeq, Long transId, String tType, Double amount, Double balance, String date) {
		super();
		this.transId = transId;
		this.transSeq = transSeq;
		this.tType = tType;
		this.amount = amount;
		this.balance=balance;
		this.date=date;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public Integer getTransSeq() {
		return transSeq;
	}

	public void setTransSeq(Integer transSeq) {
		this.transSeq = transSeq;
	}

	public String gettType() {
		return tType;
	}

	public void settType(String tType) {
		this.tType = tType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Transactions [Transaction Sequence = " + transSeq + ", Transaction ID = " + transId
				+ ", Transaction Type = " + tType + ", \n\tAmount = " + amount + ", Balance = "
				+ balance + ", Transaction Date = " + date + "]\n";
	}
	
}
