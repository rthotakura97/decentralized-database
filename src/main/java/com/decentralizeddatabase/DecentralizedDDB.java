package com.decentralizeddatabase;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;

import com.decentralizeddatabase.reno.CryptoBlock;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.security.Key;
import java.security.NoSuchAlgorithmException;


import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class DecentralizedDDB extends AbstractHandler {

	public String val;
    private static final int PORT = 8080;

    DecentralizedDDB(String val){
		this.val = val;
	}

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(val);
    }

    public static void main(String[] args) throws Exception {
        final Server server = new Server(PORT);
        DecentralizedDDB ddb = new DecentralizedDDB("Hello");
        server.setHandler(ddb);

        server.start();
        server.join();
    }
}

