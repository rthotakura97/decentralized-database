package com.decentralizeddatabase.utils;

import javax.servlet.http.HttpServletRequest;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

import static com.decentralizeddatabase.utils.Constants.*;

public class DecentralizedDBRequest {

    private final String method;
    private final String user;
    private final String secretKey;
    private final String filename;
    private final String file;

    public DecentralizedDBRequest(final HttpServletRequest request) {
	this.method = request.getParameter(METHOD);
	this.user = request.getParameter(USER);
	this.secretKey = request.getParameter(SECRET);
	this.filename = request.getParameter(FILENAME);
	this.file = request.getParameter(FILE);
    }

    @VisibleForTesting
    DecentralizedDBRequest(final String method,
			   final String user,
			   final String secretKey,
			   final String filename,
			   final String file) {
	this.method = method;
	this.user = user;
	this.secretKey = secretKey;
	this.filename = filename;
	this.file = file;
    }

    public String getMethod() {
	return this.method;
    }

    public String getUser() {
	return this.user;
    }

    public String getSecretKey() {
	return this.secretKey;
    }

    public String getFilename() {
	return this.filename;
    }

    public byte[] getFile() {
	if(Strings.isNullOrEmpty(file)) {
	    return null;
	}

	return this.file.getBytes();
    }
}
