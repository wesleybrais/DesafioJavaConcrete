package com.concrete.desafiojava.model;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {

	private String number;
	private int ddd;

	public Phone() {

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

}
