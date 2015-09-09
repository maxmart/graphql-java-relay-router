package net.cupmanager.graphql;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

public class GraphQLServerStart {
	
	
	public static class HelloHandler extends AbstractHandler {
	    final String greeting;
	    final String body;
	 
	    public HelloHandler()
	    {
	        this("Hello World");
	    }
	 
	    public HelloHandler( String greeting )
	    {
	        this(greeting, null);
	    }
	 
	    public HelloHandler( String greeting, String body )
	    {
	        this.greeting = greeting;
	        this.body = body;
	    }
	 
	    public void handle( String target,
	                        Request baseRequest,
	                        HttpServletRequest request,
	                        HttpServletResponse response ) throws IOException,
	                                                      ServletException
	    {
	    	response.setHeader("Access-Control-Allow-Origin", "*");
	    	response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
	    	if (request.getMethod().equalsIgnoreCase("POST")) {
	    		// graphql
	    		String query = IOUtils.toString(request.getInputStream());
	    		System.out.println(query);
	    		Map<String,Object> q = new Gson().fromJson(query, Map.class);
	    		query = (String) q.get("query");
	    		
	    		Map<String, Object> variables = (Map<String, Object>) q.get("variables");
//	            ExecutionResult executionResult = graphql.execute(query, (Object) null, variables);
	    		try {
	    			Map<String, Object> result = GraphQLTest.exec(query, variables);
	    			
		    		
		    		Map<String,Object> ut = new HashMap<String,Object>();
		    		ut.put("data", result);
		    		String out = new Gson().toJson(ut);
		    		
		    		response.setContentType("application/json; charset=utf-8");
		    		response.setStatus(HttpServletResponse.SC_OK);
		    		
		    		
		    		try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    		
		    		PrintWriter pw = response.getWriter();
		    		pw.print(out);
		    		pw.flush();
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		
	    	} else {
	    		response.setContentType("text/html; charset=utf-8");
	    		response.setStatus(HttpServletResponse.SC_OK);
		        PrintWriter out = response.getWriter();
		        out.println("<h1>" + greeting + "</h1>");
		        if (body != null) {
		            out.println(body);
		        }
	    	}
	        baseRequest.setHandled(true);
	    }
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		server.setHandler(new HelloHandler());
		
		server.start();
		server.join();
	}
}
