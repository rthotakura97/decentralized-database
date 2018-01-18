package com.decentralizeddatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.decentralizeddatabase.errors.DecentralizedDBError;
import com.decentralizeddatabase.utils.Dispatcher;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;
import static com.decentralizeddatabase.utils.Constants.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.JSONObject;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecentralizedDB extends AbstractHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DecentralizedDB.class);

    public void handle(String target, 
                       Request baseRequest, 
                       HttpServletRequest request, 
                       HttpServletResponse response) throws IOException, ServletException {
        LOGGER.info("Received package");
        response.setContentType("application/json;charset=utf-8");
        DecentralizedDBResponse renoResponse;
        try {
            renoResponse = Dispatcher.makeCall(new DecentralizedDBRequest(request));
        } catch (DecentralizedDBError e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(e.getErrorCode(), e.getMessage());
            return;
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(500);
            return;
        }

        final JSONObject json = renoResponse.buildJSON();
		json.write(response.getWriter());

		response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        LOGGER.info("Request handled");
    }

    private static void setProperties() {
        InputStream in = null;
        try {
            final Properties prop = new Properties(System.getProperties());
            in = new FileInputStream("properties/sqlaccess.properties");
            prop.load(in);
            System.setProperties(prop);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        System.setProperty(DATE_PROPERTY, simpleDateFormat.format(new Date()));

        // TODO: This only works on my machine. Make this easy to deploy anywhere
        PropertyConfigurator.configure("properties/log4j.properties");
    }

    public static void main(String[] args) throws Exception {
        setProperties();
        LOGGER.info("Booting up server");

        final Server server = new Server(PORT);
        server.setHandler(new DecentralizedDB());

        server.start();
        server.join();
    }
}
