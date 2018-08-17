package com.pe.azoth.beans;

public class Producto {
	public Producto() {}
	
	public Producto(int id, String codigo, String descripcion, Localidad origen, Localidad destino, Cliente envio,
			Cliente recepcion, Estado estado) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.origen = origen;
		this.destino = destino;
		this.envio = envio;
		this.recepcion = recepcion;
		this.estado = estado;
	}
	
	public Producto(int id) {
		this.id = id;
	}


	private int id;
	private String codigo;
	private String descripcion;
	private Localidad origen;
	private Localidad destino;
	private Cliente envio;
	private Cliente recepcion;
	private Estado estado;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Localidad getOrigen() {
		return origen;
	}
	public void setOrigen(Localidad origen) {
		this.origen = origen;
	}
	public Localidad getDestino() {
		return destino;
	}
	public void setDestino(Localidad destino) {
		this.destino = destino;
	}
	public Cliente getEnvio() {
		return envio;
	}
	public void setEnvio(Cliente envio) {
		this.envio = envio;
	}
	public Cliente getRecepcion() {
		return recepcion;
	}
	public void setRecepcion(Cliente recepcion) {
		this.recepcion = recepcion;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", codigo=" + codigo + ", descripcion=" + descripcion + ", origen=" + origen
				+ ", destino=" + destino + ", envio=" + envio + ", recepcion=" + recepcion + ", estado=" + estado + "]";
	}
	
	
}
