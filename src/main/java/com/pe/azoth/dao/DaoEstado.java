package com.pe.azoth.dao;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Estado;

public interface DaoEstado {

	List<Estado> listEstados() throws SQLException, NamingException;

	Estado listEstados(int id) throws SQLException, NamingException;

}