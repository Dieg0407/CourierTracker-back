package com.pe.azoth.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5758207726143940780L;

	public Usuario(String id, String pass, String nombres, String apellidos, String dni, String correo, int rango, Boolean activo) {
		super();
		this.id = id;
		this.pass = pass;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.dni = dni;
		this.correo = correo;
		this.rango = rango;
		this.setActivo(activo);
	}

	public Usuario() {}
	
	private String id;
	private String pass;
	
	private String nombres;
	private String apellidos;
	private String dni;
	private String correo;
	
	private int rango;
	private Boolean activo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getRango() {
		return rango;
	}

	public void setRango(int rango) {
		this.rango = rango;
	}

	public Boolean isActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	
}
