package com.pe.azoth.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pe.azoth.beans.Usuario;

public class DaoUsuarioImpl implements DaoUsuario {
	
	private Conexion conexion;
	
	private static String coded = "courier-tracker-15082018	";
	
	public DaoUsuarioImpl() throws JsonParseException, JsonMappingException, IOException {	
		conexion = new Conexion();
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.UsuarioDao#listUsuarios()
	 */
	@Override
	public List<Usuario> listUsuarios() throws SQLException, NamingException, NullPointerException{
		try(Connection conn = conexion.getConnection()){
			return new QueryRunner()
					.query( conn, "SELECT * FROM usuarios", new ArrayListHandler())
					.stream()
					.map(rs -> new Usuario(
						(String) rs[0],//ID
						this.decodePass( (String) rs[1] ,DaoUsuarioImpl.coded),//PASS
						(String) rs[2],//NOMBRES
						(String) rs[3],//APELLIDOS
						(String) rs[4],//DNI
						(String) rs[5],//CORREO
						(Integer) rs[6],//RANGO
						(Boolean) rs[7] //ACTIVO
					))
					.collect(Collectors.toList());
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.UsuarioDao#getUsuario(java.lang.String, java.lang.String)
	 */
	@Override
	public Usuario getUsuario(String id, String pass) throws SQLException, NamingException , NullPointerException{
		try(Connection conn = conexion.getConnection()){
			pass = this.encodePass(pass, DaoUsuarioImpl.coded);		
			List<Usuario> res = 
					new QueryRunner()
					.query(conn,  
							"SELECT * FROM usuarios WHERE pass = ? AND (id_usuario = ? OR correo = ? )",
							new ArrayListHandler(),
							pass , id , id)
					.stream()
					.map(rs -> new Usuario(
							(String) rs[0],//ID
							(String) rs[1],//PASS <- Se enviará al Token, no se decodifica
							(String) rs[2],//NOMBRES
							(String) rs[3],//APELLIDOS
							(String) rs[4],//DNI
							(String) rs[5],//CORREO
							(Integer) rs[6],//RANGO
							(Boolean) rs[7] //ACTIVO
					))
					.collect(Collectors.toList());
			
			if(res.size() == 1)
				return res.get(0);
			else
				return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pe.azoth.dao.UsuarioDao#insertUsuario(com.pe.azoth.beans.Usuario)
	 */
	@Override
	public void insertUsuario(Usuario usuario) throws SQLException, NamingException , NullPointerException{
		try(Connection conn = conexion.getConnection()){
			usuario.setPass(
					this.encodePass(usuario.getPass(), DaoUsuarioImpl.coded)
			);
			
			try(PreparedStatement pst = conn.prepareStatement(
					"INSERT INTO usuarios (id_usuario,pass,nombres,apellidos,dni,correo,id_rango,activo) "+
					"VALUES (?,?,?,?,?,?,?,?) ")){
				
				pst.setString(1, usuario.getId());
				pst.setString(2, usuario.getPass());
				pst.setString(3, usuario.getNombres());
				pst.setString(4, usuario.getApellidos());
				pst.setString(5, usuario.getDni());
				pst.setString(6, usuario.getCorreo());
				pst.setInt(7, usuario.getRango());
				pst.setBoolean(8, usuario.isActivo() == null ? true : usuario.isActivo()  );
				
				pst.executeUpdate();
			}
		}
	}
	
	@Override
	public boolean isActivo(String idUsuario) throws SQLException, NamingException {
		try(Connection connection = conexion.getConnection()){
			List<Usuario> temp = new QueryRunner()
					.query(connection,"SELECT * FROM usuarios WHERE id_usuario = ? ", new ArrayListHandler(),idUsuario)
					.stream()
					.map(rs -> new Usuario(
							(String) rs[0],//ID
							(String) rs[1],//PASS <- Se enviará al Token, no se decodifica
							(String) rs[2],//NOMBRES
							(String) rs[3],//APELLIDOS
							(String) rs[4],//DNI
							(String) rs[5],//CORREO
							(Integer) rs[6],//RANGO
							(Boolean) rs[7] //ACTIVO
					))
					.collect(Collectors.toList());
			
			return (temp.size() == 1 )? temp.get(0).isActivo() : false;
		}
	}
	
	private String decodePass(String codedPass, String key) throws NullPointerException{
		try {
			SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes("utf-8"),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal( Base64.getDecoder().decode(codedPass) );
			
			return new String(decrypted,"utf-8");
			
		} catch (UnsupportedEncodingException | InvalidKeyException | 
				NoSuchAlgorithmException | NoSuchPaddingException | 
				IllegalBlockSizeException | BadPaddingException e) {
			
			e.printStackTrace(System.err);
			throw new NullPointerException("Error en la decodificación");
		}
	}
	private String encodePass(String rawPass , String key) throws NullPointerException {
		try {
			
			SecretKey skey = new SecretKeySpec(key.getBytes("utf-8"),"Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			byte[] data = cipher.doFinal(rawPass.getBytes("utf-8"));
			
			return Base64.getEncoder().encodeToString(data);
			
		} catch (UnsupportedEncodingException | InvalidKeyException | 
				NoSuchAlgorithmException | NoSuchPaddingException | 
				IllegalBlockSizeException | BadPaddingException e) {
			
			e.printStackTrace(System.err);
			throw new NullPointerException("Error en la codificación");
		}
	}

	@Override
	public List<Usuario> listUsuarios(String not_consider) throws SQLException, NamingException, NullPointerException {
		try(Connection conn = conexion.getConnection()){
			return new QueryRunner()
					.query( conn, 
							"SELECT * FROM usuarios where id_usuario != ? AND id_usuario != 'admin' ", 
							new ArrayListHandler(), 
							not_consider)
					.stream()
					.map(rs -> new Usuario(
						(String) rs[0],//ID
						this.decodePass( (String) rs[1] ,DaoUsuarioImpl.coded),//PASS
						(String) rs[2],//NOMBRES
						(String) rs[3],//APELLIDOS
						(String) rs[4],//DNI
						(String) rs[5],//CORREO
						(Integer) rs[6],//RANGO
						(Boolean) rs[7] //ACTIVO
					))
					.collect(Collectors.toList());
			
		}
	}

	@Override
	public Usuario getUsuario(String id) throws SQLException, NamingException, NullPointerException {
		try(Connection conn = conexion.getConnection()){
			List<Usuario> res = 
					new QueryRunner()
					.query(conn,  
							"SELECT * FROM usuarios WHERE id_usuario = ? ",
							new ArrayListHandler(),
							id )
					.stream()
					.map(rs -> new Usuario(
							(String) rs[0],//ID
							(String) rs[1],//PASS <- Se enviará al Token, no se decodifica
							(String) rs[2],//NOMBRES
							(String) rs[3],//APELLIDOS
							(String) rs[4],//DNI
							(String) rs[5],//CORREO
							(Integer) rs[6],//RANGO
							(Boolean) rs[7] //ACTIVO
					))
					.collect(Collectors.toList());
			
			if(res.size() == 1)
				return res.get(0);
			else
				return null;
		}
	}

	@Override
	public int updateUsuario(Usuario usuario, boolean newPass) throws SQLException, NamingException {
		try(Connection conn = conexion.getConnection()){
			try(PreparedStatement pst = conn.prepareStatement(
					"UPDATE usuarios "+
					"SET pass = ?, nombres = ?, apellidos = ?, dni = ?, correo = ?, id_rango = ?, activo = ? "+
					"WHERE id_usuario = ? ")){
				pst.setString(1, newPass?this.encodePass(usuario.getPass(), DaoUsuarioImpl.coded):usuario.getPass());
				pst.setString(2, usuario.getNombres());
				pst.setString(3, usuario.getApellidos());
				pst.setString(4, usuario.getDni());
				pst.setString(5, usuario.getCorreo());
				pst.setInt(6, usuario.getRango());
				pst.setBoolean(7, usuario.isActivo());
				
				pst.setString(8, usuario.getId());
				
				return pst.executeUpdate();
			}
		}
	}
}
