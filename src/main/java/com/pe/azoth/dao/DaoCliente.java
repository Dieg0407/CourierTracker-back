package com.pe.azoth.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Cliente;

public interface DaoCliente {

	List<Cliente> listClientes() throws SQLException, NamingException;

	List<Cliente> listClientes(String dni) throws SQLException, NamingException;

	Cliente listClientes(Integer idCliente) throws SQLException, NamingException;

	int insertCliente(Cliente cliente, Connection connection) throws SQLException, NamingException;

	int insertCliente(Cliente cliente) throws SQLException, NamingException;

	int updateCliente(Cliente cliente, Connection connection) throws SQLException, NamingException;

	int updateCliente(Cliente cliente) throws SQLException, NamingException;

}