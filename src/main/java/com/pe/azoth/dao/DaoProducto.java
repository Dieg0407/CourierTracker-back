package com.pe.azoth.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Producto;

public interface DaoProducto {

	/**
	 * Lista todos los productos en la BD
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	List<Producto> listProductos() throws SQLException, NamingException;

	/**
	 * Lista los productos segun su ID
	 * @param ids
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	List<Producto> listProductos(Integer... ids) throws SQLException, NamingException;

	/**
	 * Obtienes un producto por su ID unico
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	Producto getProducto(Integer id) throws SQLException, NamingException;

	/**
	 * Se obtiene un producto por el codigo de empresa
	 * @param codigo
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	Producto getProducto(String codigo) throws SQLException, NamingException;

	int insertProducto(Producto producto, Connection connection) throws SQLException;

	int insertProducto(Producto producto) throws SQLException, NamingException;

	/**
	 * Se actualizan los campos
	 * @param producto
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	int updateProducto(Producto producto, Connection connection) throws SQLException;

	int updateProducto(Producto producto) throws SQLException, NamingException;

}