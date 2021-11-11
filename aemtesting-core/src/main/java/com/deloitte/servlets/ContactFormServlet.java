package com.deloitte.servlets;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;

@SlingServlet( methods = "POST", paths = "/bin/anlene/contactform")
public class ContactFormServlet extends SlingAllMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(ContactFormServlet.class);
    private String idEmail;

    protected void activate(ComponentContext componentContext) {
        configure(componentContext.getProperties());
    }

    protected void configure(Dictionary<?, ?> properties) {
        this.idEmail = PropertiesUtil.toString(properties.get("ID_EMAIL"), StringUtils.EMPTY);
    }

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Record Submitted Successfully");
            response.getWriter().write(responseJson.toString());
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            logger.error("Error in Contact form Servlet:", e);
        }
    }


    private String formatEmailBody(JSONObject requestJson) throws JSONException {
        String body = "You have a new contact form submission:";

        body += "firstname: " + requestJson.get("first_name") + ", ";
        body += "lastname: " + requestJson.get("last_name") + ", ";
        body += "email: " + requestJson.get("email") + ", ";
        body += "description: " + requestJson.get("description") + ", ";
        body += "phone: " + requestJson.get("phone_no") + ", ";
        body += "type: " + requestJson.get("kebutuhan") + ", ";
        body += "is user?: " + requestJson.get("anda_pengguna_produk_anlene");
        return body;
    }
}
