package com.pe.azoth.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Asignacion {
	public Asignacion(String usuario, int producto, String codProducto, Integer estado) {
		super();
		this.usuario = usuario;
		this.producto = producto;
		this.estado = estado;
		this.codProducto = codProducto;
	}
	
	public Asignacion() {}
	
	
	private String usuario;
	private String codProducto;
	private int producto;
	private Integer estado;
	
	@JsonProperty("id_usuario")
	public String getUsuario() {
		return usuario;
	}
	public void setIdUsuario(String usuario) {
		this.usuario = usuario;
	}
	@JsonProperty("estado")
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	@JsonProperty("id_producto")
	public int getProducto() {
		return producto;
	}
	public void setIdProducto(int producto) {
		this.producto = producto;
	}

	@JsonProperty("codigo")
	public String getCodProducto(){
		return this.codProducto;
	}
	
	public void setCodProducto(String codProducto){
		this.codProducto = codProducto;
	}
	
}
