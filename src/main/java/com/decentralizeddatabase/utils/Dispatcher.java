package com.decentralizeddatabase.utils;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.reno.Reno;
import static com.decentralizeddatabase.utils.Constants.*;

public final class Dispatcher {

    private final Reno reno;

    public Dispatcher() {
	this.reno = new Reno();
    }
    
    //TODO: Have handler asynch do work and return, have followup response set after work is done
    public DecentralizedDBResponse makeCall(final DecentralizedDBRequest request) throws BadRequest {
	final String method = request.getMethod();

	final DecentralizedDBResponse response = new DecentralizedDBResponse();
	response.setMethod(method);

	switch(method) {
	    case LIST_ALL:
		response = reno.listAll(request, response);
		break;
	    case READ:
		response = reno.read(request, response);
		break;
	    case WRITE:
		response = reno.write(request, response);
		break;
	    case DELETE:
		response = reno.delete(request, response);
		break;
	    default:
		throw new BadRequest(400, String.format("Invalid method '%s' requested", request.getMethod()));
	}

	return response;
    }
}
