package com.pe.azoth.dao;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Localidad;

public interface DaoLocalidad {

	List<Localidad> listLocalidades() throws SQLException, NamingException;

	Localidad listLocalidades(int id) throws SQLException, NamingException;

}