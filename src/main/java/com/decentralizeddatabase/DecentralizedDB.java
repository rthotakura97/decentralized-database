package com.decentralizeddatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import com.decentralizeddatabase.errors.DecentralizedDBError;
import com.decentralizeddatabase.utils.Dispatcher;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DecentralizedDB extends AbstractHandler {
    
    private static final int PORT = 8080;

    private final Dispatcher dispatcher;

    public DecentralizedDB() {
	this.dispatcher = new Dispatcher();
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
	try {
	    final DecentralizedDBResponse renoResponse = dispatcher.makeCall(new DecentralizedDBRequest(request));
	} catch (DecentralizedDBError e) {
	    response.sendError(e.getErrorCode(), e.getMessage());
	    return;
	} catch (Exception e) {
	    response.sendError(500);
	    return;
	}

        response.setStatus(HttpServletResponse.SC_OK);

        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        final Server server = new Server(PORT);
        server.setHandler(new DecentralizedDB());

        server.start();
        server.join();
    }
}
