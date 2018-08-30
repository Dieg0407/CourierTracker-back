package com.pe.azoth.servicios.tracker;


import java.io.IOException;
import java.sql.Connection;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pe.azoth.beans.Asignacion;
import com.pe.azoth.beans.Cliente;
import com.pe.azoth.beans.Producto;
import com.pe.azoth.beans.Usuario;
import com.pe.azoth.dao.Conexion;
import com.pe.azoth.dao.DaoAsignacion;
import com.pe.azoth.dao.DaoAsignacionImpl;
import com.pe.azoth.dao.DaoCliente;
import com.pe.azoth.dao.DaoClienteImpl;
import com.pe.azoth.dao.DaoEstado;
import com.pe.azoth.dao.DaoEstadoImpl;
import com.pe.azoth.dao.DaoLocalidad;
import com.pe.azoth.dao.DaoLocalidadImpl;
import com.pe.azoth.dao.DaoProducto;
import com.pe.azoth.dao.DaoProductoImpl;
import com.pe.azoth.dao.DaoUsuario;
import com.pe.azoth.dao.DaoUsuarioImpl;
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
				DaoEstado daoEstado = new DaoEstadoImpl();
				
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
					p.setEstado(daoEstado.getEstado(p.getEstado().getId()));
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
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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
						return Response.status(Response.Status.ACCEPTED).entity("{\"state\":\"success\"}").build();
				
				return Response.notModified().entity("{\"state\":\"notModified\"}").build();
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
	@Path("/insertProducto")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insertProducto(
			@HeaderParam("token") @DefaultValue("") String jwt,
			Producto producto) throws JsonProcessingException {
		try {
			JWTManager jwtManager = new JWTManager();
			Usuario usr = jwtManager.parseJWT(jwt);
			if(usr != null) {
				try {
					insert(producto);
					return Response.ok().build();
				}
				catch(IOException | SQLException | NamingException e) {
					e.printStackTrace(System.err);
					throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
				}
			}
			else {
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
			}
		}
		catch (IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private synchronized void insert(Producto producto) throws IOException, NamingException, SQLException {
		DaoProducto daoProducto = new DaoProductoImpl();
		DaoCliente daoCliente = new DaoClienteImpl();
		Connection connection = null;
		try{
			connection = new Conexion().getConnection();
			
			connection.setAutoCommit(false);
			
			if(daoCliente.getCliente(producto.getEnvio().getId()) == null)
				daoCliente.insertCliente(producto.getEnvio(), connection);
			if(daoCliente.getCliente(producto.getRecepcion().getId()) == null)
				daoCliente.insertCliente(producto.getRecepcion(), connection);
			daoProducto.insertProducto(producto, connection);
			
			connection.commit();
			connection.close();
		}
		catch(SQLException e) {
			try { connection.rollback(); } catch(Exception ex) {}
			throw e;
		}
	}
	
	@POST
	@Path("/listUsuarios")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Usuario> listUsuarios(
			@HeaderParam("token") @DefaultValue("") String token,
			String json) throws JsonProcessingException{
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			JWTManager jwtManager = new JWTManager();
			if(jwtManager.parseJWT(token) != null) {
				DaoUsuario daoUsuarios = new DaoUsuarioImpl();
				return daoUsuarios.listUsuarios(
						mapper.readTree(json).get("id_usuario").textValue()
				);
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch (IOException | SQLException | NamingException e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	
	@POST
	@Path("/getUsuario")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Usuario getUsuario(
			@HeaderParam("token") @DefaultValue("") String token,
			String jsonBody
			) throws JsonProcessingException{
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			JWTManager jwtManager = new JWTManager();
			if(jwtManager.parseJWT(token) != null) {
				DaoUsuario daoUsuarios = new DaoUsuarioImpl();
				Usuario response =  daoUsuarios.getUsuario(
						mapper.readTree(jsonBody).get("id_usuario").textValue()
				);
				if(response != null)
					return response;
				else
					throw this.exception(Response.Status.NOT_FOUND, "ID DEL USUARIO INVÁLIDA");
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch (IOException | SQLException | NamingException  e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}
	
	@POST
	@Path("/updateUsuario")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateUsuario(
			@HeaderParam("token") @DefaultValue("") String token,
			String jsonObject) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
	
		try {
			Usuario usr = new JWTManager().parseJWT(token);
			if(usr != null){
				if(usr.getRango() == 1) {					
					DaoUsuario dao = new DaoUsuarioImpl();
					
					Usuario usuario = mapper.readValue(
							mapper.readTree(jsonObject).get("usuario").toString(), 
							Usuario.class);
					int res = dao.updateUsuario(
							usuario, 
							mapper.readTree(jsonObject).get("new_password").asBoolean()
					);
					
					if(res == 1)
						return Response.ok().build();
					else
						return Response.status(Response.Status.NOT_MODIFIED).build();
					
				}
				else
					throw this.exception(Response.Status.UNAUTHORIZED, "EL USUARIO NO TIENE EL RANGO DE ADMINISTRADOR");
			}
			else 
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
			
			
		}
		catch (IOException | SQLException | NamingException  e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@POST
	@Path("/insertUsuario")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insertUsuario(
			@HeaderParam("token") @DefaultValue("") String token,
			Usuario newUser) throws JsonProcessingException {
		try {
			Usuario usuario =  new JWTManager().parseJWT(token) ;
			if(usuario != null) {
				if(usuario.getRango() == 1) {
					
					DaoUsuario dao = new DaoUsuarioImpl();
					dao.insertUsuario(newUser);
					return Response.ok().build();
				}
				else
					throw this.exception(Response.Status.UNAUTHORIZED, "EL USUARIO NO TIENE EL RANGO DE ADMINISTRADOR");
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch (IOException | SQLException | NamingException  e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@POST
	@Path("/getCliente")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Cliente getCliente(@HeaderParam("token") @DefaultValue("") String token,
			String jsonObject) throws JsonProcessingException {
		try {
			if(new JWTManager().parseJWT(token) != null) {
				
				DaoCliente daoCliente = new DaoClienteImpl();
				JsonNode json = new ObjectMapper().readTree(jsonObject);
				System.err.println(json.get("dni").textValue());
				
				return daoCliente.getCliente(json.get("dni").textValue());
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch(IOException | SQLException | NamingException  e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}
	
	@POST
	@Path("/updateCliente")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateCliente(
			@HeaderParam("token") @DefaultValue("") String token,
			Cliente cliente
			) throws JsonProcessingException{
		try {
			if(new JWTManager().parseJWT(token) != null) {
				
				DaoCliente daoCliente = new DaoClienteImpl();
				if( daoCliente.updateCliente(cliente) == 1)
					return Response.ok().build();
				else
					return Response.notModified().build();
				
			}
			else
				throw this.exception(Response.Status.UNAUTHORIZED, "ERROR DE AUTENTICACIÓN");
		}
		catch(IOException | SQLException | NamingException  e) {
			e.printStackTrace(System.err);
			throw this.exception(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
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
