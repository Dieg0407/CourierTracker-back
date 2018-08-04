package com.pe;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
  
@Path("/hello")
public class HelloWorldService {
  
    @GET
    @Path("/{name}")
    public Response getMsg(@PathParam("name") String name) {
  
        String output = "Welcome   : " + name;
  
        return Response.status(200).entity(output).build();
  
    }

    @GET
    @Path("/hello2")
    public Response getMsg2(
    	@DefaultValue("Diego") @QueryParam("name") String name,
    	@DefaultValue("Pastor") @QueryParam("lname") String lname) {
  
        String output = String.format("%s : %s %s" , "Hola",name,lname);
  
        return Response.status(200).entity(output).build();
  
    }
}
  