package com.pe.azoth.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Asignacion;

public interface DaoAsignacion {

	List<Asignacion> listAsignaciones() throws SQLException, NamingException;

	List<Asignacion> listAsignaciones(String idUsuario) throws SQLException, NamingException;

	List<Asignacion> listAsignaciones(Integer idProducto) throws SQLException, NamingException;

	int insertAsignacion(Asignacion asignacion, Connection connection) throws SQLException;

	int insertAsignacion(Asignacion asignacion) throws SQLException, NamingException;

	int updateAsignacion(Asignacion asignacion, Connection connection) throws SQLException;

	int updateAsignacion(Asignacion asignacion) throws SQLException, NamingException;

}