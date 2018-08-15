package com.pe.azoth.servicios.autenticacion;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class AuthenticationService {

	@POST
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getToken(
			@FormParam("user") @DefaultValue("") String user, 
			@FormParam("password") @DefaultValue("") String pass) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		JWTManager jwtManager = new JWTManager();
		
		try {
			String token = jwtManager.createJWT(user, pass);
			if(token != null){
				ObjectNode node = mapper.createObjectNode();
				node.put("token", token);
				return mapper.writeValueAsString(node);
			}
			else{
				ObjectNode node = mapper.createObjectNode();
				node.put("message", "Credenciales inválidas");
				throw new WebApplicationException(
						Response.status(Status.UNAUTHORIZED)
						.entity(mapper.writeValueAsString(node))
						.type(MediaType.APPLICATION_JSON)
						.build()
				);
			}
			
		} catch (NullPointerException | IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			ObjectNode node = mapper.createObjectNode();
			node.put("message", e.getMessage());
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(mapper.writeValueAsString(node))
					.type(MediaType.APPLICATION_JSON)
					.build()
			);
		}
		
	}
	
	@POST
	@Path("/getTokenJson")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
	public String getToken(String jsonObject) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		JWTManager jwtManager = new JWTManager();
		try {

			JsonNode credenciales = mapper.readTree(jsonObject);
			
			String token = jwtManager.createJWT(
					credenciales.get("user").textValue(), 
					credenciales.get("password").textValue()
			);
			
			if(token != null){
				ObjectNode node = mapper.createObjectNode();
				node.put("token", token);
				return mapper.writeValueAsString(node);
			}
			else{
				ObjectNode node = mapper.createObjectNode();
				node.put("message", "Credenciales inválidas");
				throw new WebApplicationException(
						Response.status(Status.UNAUTHORIZED)
						.entity(mapper.writeValueAsString(node))
						.type(MediaType.APPLICATION_JSON)
						.build()
				);
			}
			
		} catch (NullPointerException | IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			ObjectNode node = mapper.createObjectNode();
			node.put("message", e.getMessage());
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(mapper.writeValueAsString(node))
					.type(MediaType.APPLICATION_JSON)
					.build()
			);
		}
		
	}
	
}
