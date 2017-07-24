package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.ascargon.rocketshow.Manager;

@Path("/status")
public class Status {

	@Context
	ServletContext context;
    
	@GET
	@Produces("application/json")
	public Response convertFtoC() throws JSONException {
		Manager manager = (Manager)context.getAttribute("manager");
		System.out.println("Out: " + manager.test());
		
		JSONObject jsonObject = new JSONObject();
		Double fahrenheit = 98.24;
		Double celsius;
		celsius = (fahrenheit - 32) * 5 / 9;
		jsonObject.put("F Value", fahrenheit);
		jsonObject.put("C Value", celsius);

		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	@Path("{f}")
	@GET
	@Produces("application/json")
	public Response convertFtoCfromInput(@PathParam("f") float f) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		float celsius;
		celsius = (f - 32) * 5 / 9;
		jsonObject.put("F Value", f);
		jsonObject.put("C Value", celsius);

		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

}
