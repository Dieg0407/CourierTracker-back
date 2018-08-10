package com.pe.azoth.servicios.autenticacion;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class AuthenticationService {
	/*
	QUERY PARAM -> SOLO CON GET
	FORM PARAM -> POST
	SI VA A ENTRAR UN JSON COMO PETICION ENTONCES
		@Consumes(MediaType.APPLICATION_JSON)
	
	//GET
	@GET
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getToken(
			@QueryParam("user") @DefaultValue("") String user, 
			@QueryParam("password") @DefaultValue("") String pass) {
		
		JWTManager jwtManager = new JWTManager();
		HashMap<String,Object> valores = new HashMap<>();
		
		valores.put("email","admin@hotmail.com");
		valores.put("nombres","Alexander Sheppard");

		String token = jwtManager.createJWT(user, pass, valores);
		if(token != null){
			return "{ "+
				"\"state\":\"succes\","+
				"\"token\" : \""+token+"\","+
				"\"usuario\": {"+
					"\"id\":"+"\""+user+"\","+
					"\"password\":"+"\""+pass+"\""+
				"}"+
			"}";
		}
		else{
			return "{ "+
				"\"state\":\"error\","+
				"\"message\":\"Usuario o Contraseña inválida\","+
				"\"usuario\": {"+
					"\"id\":"+"\""+user+"\","+
					"\"password\":"+"\""+pass+"\""+
				"}"+
			"}";
		}
	}
	
	//ENTRA UN JSON COMO PARAMETROS
	@POST
	@Path("/getResponse")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getResponse(String jsonBody){
		return jsonBody;
	}
	*/

	//POST REGULAR
	@POST
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getToken(
			@FormParam("user") @DefaultValue("") String user, 
			@FormParam("password") @DefaultValue("") String pass) {
		
		JWTManager jwtManager = new JWTManager();
		HashMap<String,Object> valores = new HashMap<>();
		
		valores.put("email","admin@hotmail.com");
		valores.put("nombres","Ramón Quiróz");

		String token = jwtManager.createJWT(user, pass, valores);
		if(token != null){
			return "{ "+
				"\"state\":\"success\","+
				"\"token\" : \""+token+"\""+
			"}";
		}
		else{
			return "{ "+
				"\"state\":\"error\","+
				"\"message\":\"Usuario o Contraseña inválida\""+
			"}";
		}
	}
}
