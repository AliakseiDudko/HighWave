package com.dudko.highwave;

public class Deposit {
	public String depositName;
	
	public int depositId;
	
	public int bankId;
	
	public Deposit(){
		
	}
	
	public Deposit(String depositName, int depositId, int bankId){
		this.depositName = depositName;
		this.depositId = depositId;
		this.bankId = bankId;
	}
}
