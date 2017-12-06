package com.decentralizeddatabase.utils;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.errors.FileNotFoundError;
import com.decentralizeddatabase.reno.Reno;
import static com.decentralizeddatabase.utils.Constants.*;

public final class Dispatcher {
    
    //TODO: Have handler asynch do work and return, have followup response set after work is done
    public static DecentralizedDBResponse makeCall(final DecentralizedDBRequest request) throws BadRequest, 
																						 EncryptionError,
																						 FileNotFoundError {
		final String method = request.getMethod();

		DecentralizedDBResponse response = new DecentralizedDBResponse();
		response.setMethod(method);

		switch(method) {
			case LIST_ALL:
				Reno.listAll(request, response);
				break;
			case READ:
				Reno.read(request, response);
				break;
			case WRITE:
				Reno.write(request, response);
				break;
			case DELETE:
				Reno.delete(request, response);
				break;
			default:
				throw new BadRequest(String.format("Invalid method '%s' requested", request.getMethod()));
		}

		return response;
    }
}
