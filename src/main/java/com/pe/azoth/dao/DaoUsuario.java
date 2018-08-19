package com.pe.azoth.dao;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.pe.azoth.beans.Usuario;

public interface DaoUsuario {

	List<Usuario> listUsuarios() throws SQLException, NamingException, NullPointerException;

	Usuario getUsuario(String id, String pass) throws SQLException, NamingException, NullPointerException;

	void insertUsuario(Usuario usuario) throws SQLException, NamingException, NullPointerException;

	boolean isActivo(String idUsuario) throws SQLException, NamingException;

}