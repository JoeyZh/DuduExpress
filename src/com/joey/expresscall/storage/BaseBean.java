package com.joey.expresscall.storage;

import java.io.Serializable;

public abstract class BaseBean implements Serializable{

	protected String id;
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
}
