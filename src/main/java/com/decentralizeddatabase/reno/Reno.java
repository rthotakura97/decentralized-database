package com.decentralizeddatabase.reno;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;

import java.util.ArrayList;
import java.util.List;

import static com.decentralizeddatabase.utils.Constants.*;

//TODO
public class Reno {

    public void listAll(final DecentralizedDBRequest request,
			final DecentralizedDBResponse response) throws BadRequest {

    }

    public void read(final DecentralizedDBRequest request,
		     final DecentralizedDBResponse response) throws BadRequest {

    }

    public void write(final DecentralizedDBRequest request,
		      final DecentralizedDBResponse response) throws BadRequest {

    }

    public void delete(final DecentralizedDBRequest request,
		       final DecentralizedDBResponse response) throws BadRequest {

        List<String> blockKeys = new ArrayList<String>();
        Hasher hashFunction = new Hasher();

        for(int i=0; i<4; i++)
            blockKeys.add(hashFunction.createBlockKey(request.getSecretKey(), request.getFilename(), i));

        //TODO: List of keys for blocks created, need to send to jailcell to retrieve blocks

    }
}
