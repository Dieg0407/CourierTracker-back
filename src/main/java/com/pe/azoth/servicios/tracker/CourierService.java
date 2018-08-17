package com.pe.azoth.servicios.tracker;


import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import com.pe.azoth.beans.Usuario;
import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class CourierService {
	
	
	//OBTENER LOS REGISTROS
	@POST
	@Path("/getTable")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getTable(
		@HeaderParam("token") @DefaultValue("") String jwt) throws JsonProcessingException{
		
		JWTManager jwtManager = new JWTManager();
		ObjectMapper mapper = new ObjectMapper();
		
		Usuario usr;
		try {
			usr = jwtManager.parseJWT(jwt);
			if(usr != null){
				
				//SE EXTRAEN LOS PEDIDOS DE ESTE MEN
				return null;
			}
			else{
				ObjectNode node = mapper.createObjectNode();
				node.put("message", "Error de Autenticación");
				throw new WebApplicationException(
						Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(mapper.writeValueAsString(node))
						.type(MediaType.APPLICATION_JSON)
						.build()
				);
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
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
	
	@GET
	@Path("/testeo")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public JsonNode testing() {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode node = mapper.createObjectNode();
		
		ObjectNode node1 = mapper.createObjectNode();
		
		ObjectNode node2 = mapper.createObjectNode();
		
		node.put("TEST", "nodo raiz");
		node1.put("TEST", "nodo hijo 1");
		node2.put("TEST", "nodo hijo 2");
		
		node.set("leaf1", node1);
		node.set("leaf2", node2);
		
		return node;
	}

}
