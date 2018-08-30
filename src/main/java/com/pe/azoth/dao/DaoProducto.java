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
	 * Lista los productos segun su ID y CODIGO DE LA SIGUIENTE FORMA CODIGO-ID
	 * @param CODIGO-ID concatenados
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	List<Producto> listProductos(String...strings ) throws SQLException, NamingException;
	
	/**
	 * Obtienes un producto por su ID unico
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	Producto getProducto(Integer id, String codigo) throws SQLException, NamingException;

	/**
	 * Se obtiene un producto por el codigo de empresa
	 * @param codigo
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	List<Producto> listProductos(String codigo) throws SQLException, NamingException;

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

	/**
	 * Devuelve el proximo id correspondiente a un codigo determinado, de no existir dicho codigo devuelve 1
	 * @param codigo
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	 int getNextIdProducto(String codigo) throws SQLException, NamingException;
	
	/**
	 * Retorna una Lista con todos los codigos que existen actualmente en la BD
	 * @return
	 * @throws A
	 * @throws NamingException
	 */
	List<String> getCodigos() throws SQLException, NamingException;

}