package com.decentralizeddatabase.utils;

import com.decentralizeddatabase.reno.Reno;
import com.decentralizeddatabase.errors.BadRequest;

public class Dispatcher {

    private static final String LIST_ALL = "list-all";
    private static final String READ = "read";
    private static final String WRITE = "write";
    private static final String DELETE = "delete";

    private final Reno reno;

    public Dispatcher() {
	this.reno = new Reno();
    }
    
    //TODO: Have handler asynch do work and return, have followup response set after work is done
    public DecentralizedDBResponse makeCall(final DecentralizedDBRequest request) throws BadRequest {
	DecentralizedDBResponse response = null;
	switch(request.getMethod()) {
	    case LIST_ALL:
		response = reno.listAll(request);
		break;
	    case READ:
		response = reno.read(request);
		break;
	    case WRITE:
		response = reno.write(request);
		break;
	    case DELETE:
		response = reno.delete(request);
		break;
	    default:
		throw new BadRequest(400, String.format("Invalid method '%s' requested", request.getMethod()));
	}

	return response;
    }
}