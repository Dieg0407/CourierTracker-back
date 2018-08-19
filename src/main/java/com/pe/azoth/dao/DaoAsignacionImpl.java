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
					.query(connection, "SELECT a.id_usuario, a.id_producto, a.codigo, p.id_estado "
							+ "FROM asignaciones a "
							+ "INNER JOIN productos p ON (p.id_producto = a.id_producto ) "
							, new ArrayListHandler())
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[0], //ID USUARIO
							(Integer)rs[1], //ID PRODUCTO
							(String)rs[2],//CODIGO PRODUCTO
							(Integer)rs[3] //ACTIVO 
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
					.query(connection, "SELECT a.id_usuario, a.id_producto, a.codigo, p.id_estado "
							+ "FROM asignaciones a "
							+ "INNER JOIN productos p ON (p.id_producto = a.id_producto ) "
							+ "WHERE a.id_usuario = ? ", new ArrayListHandler(),idUsuario)
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[0], //ID USUARIO
							(Integer)rs[1], //ID PRODUCTO
							(String)rs[2],//CODIGO PRODUCTO
							(Integer)rs[3] //ACTIVO 
							))
					.collect(Collectors.toList());
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#listAsignaciones(java.lang.Integer)
	 */
	@Override
	public List<Asignacion> listAsignaciones(Integer idProducto, String codigoProducto) throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			
			return new QueryRunner()
					.query(connection, "SELECT a.id_usuario, a.id_producto, a.codigo, p.id_estado "
							+ "FROM asignaciones a "
							+ "INNER JOIN productos p ON (p.id_producto = a.id_producto ) "
							+ "WHERE a.id_producto = ? AND a.codigo = ?", 
							new ArrayListHandler(),
							idProducto,codigoProducto)
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[0], //ID USUARIO
							(Integer)rs[1], //ID PRODUCTO
							(String)rs[2],//CODIGO PRODUCTO
							(Integer)rs[3] //ACTIVO 
							))
					.collect(Collectors.toList());
			
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#getAsignacion(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Asignacion getAsignacion(Integer idProducto, String codigoProducto, String idUsuario) throws SQLException, NamingException {
		
		try(Connection connection = this.conexion.getConnection()){
			
			List<Asignacion> temp =  new QueryRunner()
					.query(connection,"SELECT a.id_usuario, a.id_producto, a.codigo, p.id_estado "
							+ "FROM asignaciones a "
							+ "INNER JOIN productos p ON (p.id_producto = a.id_producto ) "
							+ "WHERE a.id_usuario = ? AND p.codigo = ? AND p.id_producto = ?", 
							new ArrayListHandler(),idUsuario,codigoProducto,idProducto)
					.stream()
					.map(rs -> new Asignacion(
							(String)rs[0], //ID USUARIO
							(Integer)rs[1], //ID PRODUCTO
							(String)rs[2],//CODIGO PRODUCTO
							(Integer)rs[3] //ACTIVO 
							))
					.collect(Collectors.toList());
			
			return (temp.size() == 1)?temp.get(0):null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoAsignacion#insertAsignacion(com.pe.azoth.beans.Asignacion, java.sql.Connection)
	 */
	@Override
	public int insertAsignacion (Asignacion asignacion, Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement(
				"INSERT INTO asignaciones "+
				"(id_producto, id_usuario, codigo) "+
				"VALUES (?,?,?) ")){
			
			pst.setInt(1, asignacion.getProducto());
			pst.setString(2, asignacion.getUsuario());
			pst.setString(3,asignacion.getCodProducto());
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
	
}
