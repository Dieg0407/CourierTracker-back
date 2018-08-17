package com.pe.azoth.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.NamingException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pe.azoth.beans.Asignacion;

public class DaoAsignacionImpl implements DaoAsignacion {
	
	private Conexion conexion;
	
	public DaoAsignacionImpl() throws JsonParseException, JsonMappingException, IOException {
		this.conexion = new Conexion();
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#listAsignaciones()
	 */
	@Override
	public List<Asignacion> listAsignaciones() throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			
			return new QueryRunner()
					.query(connection, "SELECT * FROM asignaciones ", new ArrayListHandler())
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[1], //ID USUARIO
							(Integer)rs[0], //ID PRODUCTO
							(Boolean)rs[2] //ACTIVO 
							))
					.collect(Collectors.toList());
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#listAsignaciones(java.lang.String)
	 */
	@Override
	public List<Asignacion> listAsignaciones(String idUsuario) throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			
			return new QueryRunner()
					.query(connection, "SELECT * FROM asignaciones WHERE id_usuario = ? ", new ArrayListHandler(),idUsuario)
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[1], //ID USUARIO
							(Integer)rs[0], //ID PRODUCTO
							(Boolean)rs[2] //ACTIVO 
							))
					.collect(Collectors.toList());
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#listAsignaciones(java.lang.Integer)
	 */
	@Override
	public List<Asignacion> listAsignaciones(Integer idProducto) throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			
			return new QueryRunner()
					.query(connection, "SELECT * FROM asignaciones WHERE id_producto = ? ", new ArrayListHandler(),idProducto)
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[1], //ID USUARIO
							(Integer)rs[0], //ID PRODUCTO
							(Boolean)rs[2] //ACTIVO 
							))
					.collect(Collectors.toList());
			
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#insertAsignacion(com.pe.azoth.beans.Asignacion, java.sql.Connection)
	 */
	@Override
	public int insertAsignacion (Asignacion asignacion, Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement(
				"INSERT INTO asignaciones "+
				"(id_producto, id_usuario, activo) "+
				"VALUES (?,?,?) ")){
			
			pst.setInt(1, asignacion.getProducto());
			pst.setString(2, asignacion.getUsuario());
			pst.setBoolean(3, asignacion.isActivo() == null?true:asignacion.isActivo());
			
			return pst.executeUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#insertAsignacion(com.pe.azoth.beans.Asignacion)
	 */
	@Override
	public int insertAsignacion (Asignacion asignacion) throws SQLException, NamingException {
		try(Connection connection = this.conexion.getConnection()){
			return this.insertAsignacion(asignacion,connection);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#updateAsignacion(com.pe.azoth.beans.Asignacion, java.sql.Connection)
	 */
	@Override
	public int updateAsignacion(Asignacion asignacion,Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement(
				"UPDATE asignaciones "+
				"SET activo = ? "+
				"WHERE id_usuario = ? AND id_producto = ? ")){
			
			pst.setInt(3, asignacion.getProducto());
			pst.setString(2, asignacion.getUsuario());
			pst.setBoolean(1, asignacion.isActivo() );
			
			return pst.executeUpdate();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#updateAsignacion(com.pe.azoth.beans.Asignacion)
	 */
	@Override
	public int updateAsignacion (Asignacion asignacion) throws SQLException, NamingException {
		try(Connection connection = this.conexion.getConnection()){
			return this.updateAsignacion(asignacion,connection);
		}
	}
}
