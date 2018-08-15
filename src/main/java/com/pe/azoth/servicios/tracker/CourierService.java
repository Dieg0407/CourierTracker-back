package com.pe.azoth.servicios.tracker;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class CourierService {
	
	
	//OBTENER LOS REGISTROS
	@POST
	@Path("/getTable")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getTable(
		@FormParam("token") @DefaultValue("") String jwt){
		JWTManager jwtManager = new JWTManager();

		if(jwtManager.parseJWT(jwt) != null){
			
			//SE EXTRAEN LOS PEDIDOS DE ESTE MEN
			return "{"+
				"\"state\":\"success\","+
				"\"pedidos\":["+
					"{\"id\":1 , \"desc\":\"pedido1\"},"+
					"{\"id\":2 , \"desc\":\"pedido2\"},"+
					"{\"id\":3 , \"desc\":\"pedido3\"},"+
					"{\"id\":4 , \"desc\":\"pedido4\"},"+
					"{\"id\":5 , \"desc\":\"pedido5\"},"+
					"{\"id\":6 , \"desc\":\"pedido6\"}"+
				"]"+
			"}";
		}
		else{
			return "{"+
				"\"state\":\"error\","+
				"\"message\":\"Credenciales inválidas\","+
				"\"token\":\""+jwt+"\""+
			"}";
		}

	}

}
