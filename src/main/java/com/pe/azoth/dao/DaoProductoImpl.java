package com.pe.azoth.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.NamingException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pe.azoth.beans.Cliente;
import com.pe.azoth.beans.Estado;
import com.pe.azoth.beans.Localidad;
import com.pe.azoth.beans.Producto;

public class DaoProductoImpl implements DaoProducto {
	
	private Conexion conexion;
	
	public DaoProductoImpl() throws JsonParseException, JsonMappingException, IOException {	
		conexion = new Conexion();
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#listProductos()
	 */
	@Override
	public List<Producto> listProductos() throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			
			return new QueryRunner()
					.query(connection,
							"SELECT * FROM productos ", 
							new ArrayListHandler())
					.stream()
					.map( rs -> new Producto(
							(Integer)rs[0],//ID
							(String)rs[1],//CODIGO
							(String)rs[2],//DESCRIPCION
							new Localidad ((Integer)rs[3]),//ORIGEN
							new Localidad ((Integer)rs[4]),//DESTINO
							new Cliente ((Integer)rs[5]),//CLIENTE ENVIO
							new Cliente ((Integer)rs[6]),//CLIENTE RECEPCION
							new Estado ((Integer)rs[7])//Estado
							))
					.collect(Collectors.toList());
		}
		
	}

	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#getProducto(java.lang.String)
	 */
	@Override
	public List<Producto> listProductos(String codigo)  throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			return new QueryRunner()
					.query(connection, "SELECT * FROM productos WHERE codigo = ? ", new ArrayListHandler(),codigo)
					.stream()
					.map( rs -> new Producto(
							(Integer)rs[0],//ID
							(String)rs[1],//CODIGO
							(String)rs[2],//DESCRIPCION
							new Localidad ((Integer)rs[3]),//ORIGEN
							new Localidad ((Integer)rs[4]),//DESTINO
							new Cliente ((Integer)rs[5]),//CLIENTE ENVIO
							new Cliente ((Integer)rs[6]),//CLIENTE RECEPCION
							new Estado ((Integer)rs[7])//Estado
							))
					.collect(Collectors.toList());
			
		}
	}
	


	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#listProductos(java.lang.Integer)
	 */
	@Override
	public List<Producto> listProductos(String... codigos) throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			if(codigos.length == 0)
				return Arrays.asList();
			else {
				
				String sql = "(";
				for(String x : codigos) {
					sql += ("'"+x+"'" +",");
				}
				sql = sql.substring(0,sql.length()-1)+")";
				

				return new QueryRunner()
						.query(connection,
								"SELECT * FROM productos WHERE CONCAT(codigo,'-',id_producto) IN " + sql , 
								new ArrayListHandler())
						.stream()
						.map( rs -> new Producto(
								(Integer)rs[0],//ID
								(String)rs[1],//CODIGO
								(String)rs[2],//DESCRIPCION
								new Localidad ((Integer)rs[3]),//ORIGEN
								new Localidad ((Integer)rs[4]),//DESTINO
								new Cliente ((Integer)rs[5]),//CLIENTE ENVIO
								new Cliente ((Integer)rs[6]),//CLIENTE RECEPCION
								new Estado ((Integer)rs[7])//Estado
								))
						.collect(Collectors.toList());
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#getProducto(java.lang.Integer)
	 */
	@Override
	public Producto getProducto(Integer id, String codigoProducto) throws SQLException, NamingException{
		try(Connection connection = this.conexion.getConnection()){
			List<Producto> temp = new QueryRunner()
					.query(connection,"SELECT * FROM productos WHERE id_producto = ? AND codigo = ?", 
						new ArrayListHandler(),id,codigoProducto)
					.stream()
					.map( rs -> new Producto(
							(Integer)rs[0],//ID
							(String)rs[1],//CODIGO
							(String)rs[2],//DESCRIPCION
							new Localidad ((Integer)rs[3]),//ORIGEN
							new Localidad ((Integer)rs[4]),//DESTINO
							new Cliente ((Integer)rs[5]),//CLIENTE ENVIO
							new Cliente ((Integer)rs[6]),//CLIENTE RECEPCION
							new Estado ((Integer)rs[7])//Estado
							))
					.collect(Collectors.toList());
			if(temp.size() == 1)
				return temp.get(0);
			else
				return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#insertProducto(com.pe.azoth.beans.Producto, java.sql.Connection)
	 */
	@Override
	public int insertProducto(Producto producto, Connection connection) throws SQLException{
		try(PreparedStatement pst = connection.prepareStatement(
				"INSERT INTO productos (codigo,descripcion,origen,destino,cliente_envio,cliente_recepcion,id_estado,id_producto) " +
				"values (?,?,?,?,?,?,?,?) ")){
			pst.setString(1, producto.getCodigo());
			pst.setString(2, producto.getDescripcion());
			pst.setInt(3, producto.getOrigen().getId());
			pst.setInt(4, producto.getDestino().getId());
			pst.setInt(5, producto.getEnvio().getId());
			pst.setInt(6, producto.getRecepcion().getId());
			pst.setInt(7, producto.getEstado().getId());
			pst.setInt(8,producto.getId());
			
			return pst.executeUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#insertProducto(com.pe.azoth.beans.Producto)
	 */
	@Override
	public int insertProducto(Producto producto) throws SQLException, NamingException {
		try(Connection connection = this.conexion.getConnection()){
			producto.setId(this.getNextIdProducto(producto.getCodigo()));
			return this.insertProducto(producto,connection);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#updateProducto(com.pe.azoth.beans.Producto, java.sql.Connection)
	 */
	@Override
	public int updateProducto(Producto producto, Connection connection) throws SQLException {
		try(PreparedStatement pst = connection.prepareStatement(
				"UPDATE productos SET descripcion = ?, origen = ?, destino = ?, cliente_envio = ?, cliente_recepcion = ?, id_estado = ? "+
				"WHERE codigo = ? AND id_producto = ? ")){
			
			pst.setString(1, producto.getDescripcion());
			pst.setInt(2, producto.getOrigen().getId());
			pst.setInt(3, producto.getDestino().getId());
			pst.setInt(4, producto.getEnvio().getId());
			pst.setInt(5, producto.getRecepcion().getId());
			pst.setInt(6, producto.getEstado().getId());
			pst.setString(7, producto.getCodigo());
			pst.setInt(8,producto.getId());
			
			return pst.executeUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.DaoProducto#updateProducto(com.pe.azoth.beans.Producto)
	 */
	@Override
	public int updateProducto(Producto producto) throws SQLException, NamingException {
		try(Connection connection = conexion.getConnection()){
			return this.updateProducto(producto,connection);
		}
	}
	@Override
	public int getNextIdProducto(String codigo) throws SQLException, NamingException{
		try(Connection connection = conexion.getConnection()){
			try(PreparedStatement pst = connection.prepareStatement("select IFNULL(MAX(id_producto), 0) + 1  "
					+ "from courier_tracker.productos where codigo = ?")){
				pst.setString(1, codigo);
				
				try(ResultSet rs = pst.executeQuery()){
					rs.next();
					return rs.getInt(1);
				}
				
			}
			
		}
	}
	
	public int getNextIdProducto(String codigo, Connection connection) throws SQLException, NamingException{
		try(PreparedStatement pst = connection.prepareStatement("select IFNULL(MAX(id_producto), 0) + 1  "
				+ "from courier_tracker.productos where codigo = ?")){
			pst.setString(1, codigo);
			
			try(ResultSet rs = pst.executeQuery()){
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	@Override
	public List<String> getCodigos() throws SQLException, NamingException{
		try(Connection connection = conexion.getConnection()){
			return new QueryRunner()
					.query(connection, "SELECT distinct codigo FROM productos ", new ArrayListHandler())
					.stream()
					.map(e -> (String)e[0])
					.collect(Collectors.toList());
		}
	}
	
}
