package com.nysiis.restjersey;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



import eu.cec.move.CommonServices;
import eu.cec.move.IllegalEncodingException;

@Path("/convert")
public class Convert {

   
   @GET
   @Path("/{c}/{d}")
   @Produces("application/json")
   public Response convertCtoFfromInput(@PathParam("c") String c,@PathParam("d") String d) {
      	   
    	   CommonServices cs = new CommonServices();
    	   String nysiis_forename;
		try {
			nysiis_forename = cs.getNYSIISSearchKey(c, false);
		} catch (IllegalEncodingException e) {
			
			return Response.serverError().entity(e.getMessage()).build();
		}
    	   String nysiis_family_name;
		try {
			nysiis_family_name = cs.getNYSIISSearchKey(d, false);
		} catch (IllegalEncodingException e) {
			
			return Response.serverError().entity(e.getMessage()).build();
		}
    	   
    	   String json = "{\"nysiisFirstName\":\""+nysiis_forename+"\",\"nysiisFamilyName\":\""+nysiis_family_name+"\"}";
    	   
    	   return Response.ok(json, MediaType.APPLICATION_JSON).build();
    
   }
  
   @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response convertCtoFfromInput(String data) throws IllegalEncodingException {
		
		String volFirstName = null;
		String volFamilyName = null;
		
		JSONParser parser = new JSONParser();
		
		try{
			
			Object obj = parser.parse(data);
			JSONObject jsonObject = (JSONObject) obj;
			volFirstName = (String) jsonObject.get("volFirstName");
			volFamilyName = (String) jsonObject.get("volFamilyName");
			
			if(volFirstName.isEmpty() || volFamilyName.isEmpty()) {
		          return Response.serverError().entity("VOL names cannot be null").build();
				
		       }
			else{
			  	 CommonServices cs = new CommonServices(); // Invoking the nysiis jar and assume that it is multi threaded
		
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("nysiisFirstName" , cs.getNYSIISSearchKey(volFirstName, false));
				jsonobj.put("nysiisFamilyName" , cs.getNYSIISSearchKey(volFamilyName, false));
				

		     	return Response.ok(jsonobj.toJSONString(), MediaType.APPLICATION_JSON).build();
			}
			
			
		}catch (ParseException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		
}
   
	
}


