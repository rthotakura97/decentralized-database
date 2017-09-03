package com.decentralizeddatabase;

import javax.servlet.http.HttpServletRequest;

public class DecentralizedDBRequest {

    private static final METHOD = "method";
    private static final USER = "decentralized-db-user";
    private static final SECRET = "secret-key";
    private static final FILENAME = "filename";
    private static final FILE = "file";

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
	if(file) {
	    return this.file.getBytes();
	}

	return null;
    }
}
