package com.pe.azoth.beans;

public class Localidad {
	
	public Localidad(int id) {
		this.id = id;
	}
	
	public Localidad() {}
	
	
	public Localidad(int id, String departamento) {
		super();
		this.id = id;
		this.departamento = departamento;
	}


	private int id;
	private String departamento;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
	
}
