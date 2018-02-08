package com.distributeddb.utils;

import com.distributeddb.errors.BadRequest;
import com.distributeddb.errors.EncryptionError;
import com.distributeddb.errors.FileNotFoundError;
import com.distributeddb.errors.JailCellServerError;
import com.distributeddb.reno.Reno;
import static com.distributeddb.utils.Constants.*;

public final class Dispatcher {
    
    //TODO: Have handler asynch do work and return, have followup response set after work is done
    public static DistributedDBResponse makeCall(final DistributedDBRequest request) throws BadRequest, 
                                                                                                EncryptionError,
                                                                                                FileNotFoundError,
                                                                                                JailCellServerError {
		final String method = request.getMethod();

		DistributedDBResponse response = new DistributedDBResponse();
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
