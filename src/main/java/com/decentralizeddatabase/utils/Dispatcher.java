package com.decentralizeddatabase.utils;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.reno.Reno;
import static com.decentralizeddatabase.utils.Constants.*;

public final class Dispatcher {

    private final Reno reno;

    public Dispatcher() throws EncryptionError {
	this.reno = new Reno();
    }
    
    //TODO: Have handler asynch do work and return, have followup response set after work is done
    public DecentralizedDBResponse makeCall(final DecentralizedDBRequest request) throws BadRequest, EncryptionError {
	final String method = request.getMethod();

	DecentralizedDBResponse response = new DecentralizedDBResponse();
	response.setMethod(method);

	switch(method) {
	    case LIST_ALL:
		reno.listAll(request, response);
		break;
	    case READ:
		reno.read(request, response);
		break;
	    case WRITE:
		reno.write(request, response);
		break;
	    case DELETE:
		reno.delete(request, response);
		break;
	    default:
		throw new BadRequest(String.format("Invalid method '%s' requested", request.getMethod()));
	}

	return response;
    }
}
