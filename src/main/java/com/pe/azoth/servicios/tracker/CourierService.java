package com.pe.azoth.servicios.tracker;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pe.azoth.beans.Asignacion;
import com.pe.azoth.beans.Producto;
import com.pe.azoth.beans.Usuario;
import com.pe.azoth.dao.DaoAsignacion;
import com.pe.azoth.dao.DaoAsignacionImpl;
import com.pe.azoth.dao.DaoCliente;
import com.pe.azoth.dao.DaoClienteImpl;
import com.pe.azoth.dao.DaoLocalidad;
import com.pe.azoth.dao.DaoLocalidadImpl;
import com.pe.azoth.dao.DaoProducto;
import com.pe.azoth.dao.DaoProductoImpl;
import com.pe.azoth.modelo.JWTManager;

@Path("/")
public class CourierService {
	
	
	//OBTENER LOS REGISTROS
	@POST
	@Path("/getProductos")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Producto> getTable(
		@HeaderParam("token") @DefaultValue("") String jwt) throws JsonProcessingException{
		
		JWTManager jwtManager = new JWTManager();
		try {
			Usuario usr = jwtManager.parseJWT(jwt);
			if(usr != null){
				
				DaoProducto daoProducto = new DaoProductoImpl();
				DaoAsignacion daoAsignacion = new DaoAsignacionImpl();
				DaoLocalidad daoLocalidad = new DaoLocalidadImpl();
				DaoCliente daoCliente  = new DaoClienteImpl();
				
				
				List<String> temp = daoAsignacion.listAsignaciones(usr.getId())
				.stream()
				.map(as -> as.getCodProducto()+"-"+as.getProducto())
				.collect(Collectors.toList());
				

				String[] array = new String[temp.size()];
				array = temp.toArray(array);
				List<Producto> productos = daoProducto.listProductos(array);
				
				for(Producto p : productos) {
					p.setOrigen(daoLocalidad.getLocalidad(p.getOrigen().getId()));
					p.setDestino(daoLocalidad.getLocalidad(p.getDestino().getId()));
					p.setEnvio(daoCliente.getCliente(p.getEnvio().getId()));
					p.setRecepcion(daoCliente.getCliente(p.getRecepcion().getId()));
				}
				
				return productos;
			}
			else
				throw new WebApplicationException(
						this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN")
				);
		} 
		catch (IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			throw new WebApplicationException(
					this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())
			);
		}
	}
	
	@POST
	@Path("/getProducto")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Producto getProducto(
			@HeaderParam("token") @DefaultValue("") String jwt, 
			@FormParam("id_producto") @DefaultValue("-1") int id_producto,
			@FormParam("codigo") @DefaultValue("") String codigo) throws JsonProcessingException {
		
		try {
			JWTManager jwtManager = new JWTManager();
			Usuario usr = jwtManager.parseJWT(jwt);
			if(usr != null) {
				DaoProducto daoProducto = new DaoProductoImpl();
				DaoAsignacion daoAsignacion = new DaoAsignacionImpl();
				DaoLocalidad daoLocalidad = new DaoLocalidadImpl();
				DaoCliente daoCliente  = new DaoClienteImpl();
				
				Asignacion a = daoAsignacion.getAsignacion(id_producto,codigo,usr.getId());
				if(a != null) {
					Producto p = daoProducto.getProducto(id_producto, codigo);
					p.setOrigen(daoLocalidad.getLocalidad(p.getOrigen().getId()));
					p.setDestino(daoLocalidad.getLocalidad(p.getDestino().getId()));
					p.setEnvio(daoCliente.getCliente(p.getEnvio().getId()));
					p.setRecepcion(daoCliente.getCliente(p.getRecepcion().getId()));
					return p;
				}
					
				else
					throw this.exception(Response.Status.UNAUTHORIZED, "EL USUARIO NO TIENE ASIGNACION UN PRODUCTO CON EL CODIGO+ID: "+codigo+"-"+id_producto);
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
			
		}
		catch (IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			throw new WebApplicationException(
					this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())
			);
		}	
	}
	
	
	@POST
	@Path("/updateProducto")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateProducto(
			@HeaderParam("token") @DefaultValue("") String jwt,
			Producto producto) throws JsonProcessingException {
		
		try {
			JWTManager jwtManager = new JWTManager();
			Usuario usr = jwtManager.parseJWT(jwt);
			if(usr != null) {
				DaoAsignacion daoAsignacion =  new DaoAsignacionImpl();
				DaoProducto daoProducto =  new DaoProductoImpl();
				Asignacion a = daoAsignacion.getAsignacion(producto.getId(),producto.getCodigo(),usr.getId());
				if( a != null )
					if(daoProducto.updateProducto(producto) == 1)
						return Response.status(Response.Status.ACCEPTED).build();
				
				return Response.notModified().build();
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch (IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			throw new WebApplicationException(
					this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())
			);
		}
	}

	
	private WebApplicationException exception(Response.Status status, String mensaje) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("message", mensaje);
		throw new WebApplicationException(
				Response.status(status)
				.entity(mapper.writeValueAsString(node))
				.type(MediaType.APPLICATION_JSON)
				.build()
		);
	}

}
