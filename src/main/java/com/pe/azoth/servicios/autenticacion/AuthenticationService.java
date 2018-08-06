package com.pe.azoth.servicios.autenticacion;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class AuthenticationService {
	
	@GET
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON)
	public String getToken(
			@QueryParam("user") @DefaultValue("") String user, 
			@QueryParam("password") @DefaultValue("") String pass) {
		
		JWTManager jwtManager = new JWTManager();
		String token = jwtManager.createJWT(user, user, null);
		if(token != null)
			return "{ \"state\":\"succes\" ,\"token\" : \""+token+"\" }";
		else
			return "{ \"state\":\"error\"}";
	}
	
}
