package com.pe.azoth.modelo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.sql.SQLException;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pe.azoth.beans.Usuario;
import com.pe.azoth.dao.UsuarioDao;
import com.pe.azoth.dao.UsuarioDaoImpl;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTManager{
	
	
	
	//ESTO SE TIENE QUE PONER EN LA BD HASHEADO
	private static final String tokenKey = "ct-12344321-7894564512345adsadasdadsadasdxczcaxczcxzcsdacscsdcsdc";
		
	private Key key;
	public JWTManager() {
		try {
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
			byte[] apiKeySecretBytes;
			apiKeySecretBytes = tokenKey.getBytes("UTF-8");
			key = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String createJWT(String user, String pass) throws JsonParseException, JsonMappingException, IOException,
				NullPointerException, SQLException, NamingException  {
		
		UsuarioDao dao = new UsuarioDaoImpl();
		Usuario usr = dao.getUsuario(user, pass);
		
		if(usr != null) {
			
			JwtBuilder builder = Jwts.builder();
			//SETTING THE HEADERS
			builder.setHeaderParam("typ","JWT");
			//SETTING THE CLAIMS
			builder.setPayload(new ObjectMapper().writeValueAsString(usr));		
			
			builder.signWith(this.key);
		
			return builder.compact();
		}
		else
			return null;
		
	}
	
	public Usuario parseJWT(String jwtString) {
		
		JwtParser parser = Jwts.parser();
		parser.setSigningKey(key);
		
		if(parser.isSigned(jwtString)) {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(jwtString.split("\\.")[1], Usuario.class);
		}
		else 
			return null;
	}
	
}