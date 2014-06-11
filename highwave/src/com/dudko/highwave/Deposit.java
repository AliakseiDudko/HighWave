package com.dudko.highwave;

public class Deposit {
	public String depositName;
	
	public int bankId;
	
	public Deposit(){
		
	}
	
	public Deposit(String depositName, int bankId){
		this.depositName = depositName;
		this.bankId = bankId;
	}
}
