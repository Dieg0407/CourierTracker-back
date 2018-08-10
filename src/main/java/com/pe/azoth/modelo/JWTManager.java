package com.pe.azoth.modelo;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTManager{
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
	//ESTO SE TIENE QUE PONER EN LA BD HASHEADO
	private static final String tokenKey = "ct-12344321-7894564512345adsadasdadsadasdxczcaxczcxzcsdacscsdcsdc";
	
	public String createJWT(String user, String pass , Map<String,Object> extraClaims)  {
		
		if(user.trim().equals("admin") && pass.trim().equals("admin")) {
			JwtBuilder builder = Jwts.builder();
			//SETTING THE HEADERS
			builder.setHeaderParam("typ","JWT");
			//SETTING THE CLAIMS
			builder.claim("id",   user);
			builder.claim("pass", pass);
			
			
			if(extraClaims != null)
				builder.addClaims(extraClaims);
			
			builder.signWith(this.key);
		
			return builder.compact();
		}
		else
			return null;
		
	}

	public boolean parseJWT(String jwtString) {
		
		JwtParser parser = Jwts.parser();
		parser.setSigningKey(key);
		
		return parser.isSigned(jwtString);
	}
	
}