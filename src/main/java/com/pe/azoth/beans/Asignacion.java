package com.pe.azoth.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Asignacion {
	public Asignacion(String usuario, int producto, boolean activo) {
		super();
		this.usuario = usuario;
		this.producto = producto;
		this.activo = activo;
	}
	
	public Asignacion() {}
	
	
	private String usuario;
	private int producto;
	private Boolean activo;
	
	@JsonProperty("id_usuario")
	public String getUsuario() {
		return usuario;
	}
	public void setIdUsuario(String usuario) {
		this.usuario = usuario;
	}
	@JsonProperty("activo")
	public Boolean isActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	@JsonProperty("id_producto")
	public int getProducto() {
		return producto;
	}
	public void setIdProducto(int producto) {
		this.producto = producto;
	}
	
	
}
