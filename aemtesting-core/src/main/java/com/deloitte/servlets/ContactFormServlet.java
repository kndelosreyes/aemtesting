package com.deloitte.servlets;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            JSONObject responseJson = new JSONObject();

            String responseFromServlet = getRequestInfo(request);
            if(responseFromServlet == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                responseJson.put("message", responseFromServlet);
                response.getWriter().write(responseJson.toString());
            }
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Error in Contact form Servlet:", e);
        }
    }

    /**
     * Create a JCR node
     * @param request
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param phone
     * @param city
     * @param birthDate
     * @param concern
     * @return
     * */
    private void createNode(SlingHttpServletRequest request, String firstName,
                            String lastName, String emailAddress, String phone,
                            String city, String birthDate, String concern) {

        try {
        ResourceResolver resourceResolver = request.getResourceResolver();

        Resource resource = resourceResolver.getResource("/content/first-page/first-editable-template-use");

        Node node = resource.adaptTo(Node.class);

        Node newNode = node.addNode("concerns", "nt:unstructured");

        newNode.setProperty("firstName", firstName);
        newNode.setProperty("lastName", lastName);
        newNode.setProperty("emailAddress", emailAddress);
        newNode.setProperty("phone", phone);
        newNode.setProperty("city", city);
        newNode.setProperty("birthDate", birthDate);
        newNode.setProperty("concern", concern);

        resourceResolver.commit();

        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();

        } catch (PersistenceException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Retrieve request information and create a message with it
     * @param request
     * @return String
     */
    private String getRequestInfo(SlingHttpServletRequest request) {

        String successRouteMessage = null;

        if(request != null) {

            // retrieve parameter values
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String emailAddress = request.getParameter("emailAddress");
            String phone = request.getParameter("phone");
            String city = request.getParameter("city");
            String birthDate = request.getParameter("birthDate");
            String concern = request.getParameter("concern");

            if(StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) &&
                StringUtils.isNotBlank(emailAddress) && StringUtils.isNotBlank(phone) &&
                StringUtils.isNotBlank(city) && StringUtils.isNotBlank(birthDate) &&
                StringUtils.isNotBlank(concern)) {

                successRouteMessage = "<br>" + "Thank you for your concern! We will get back to you shortly " + "<br>";

                successRouteMessage += "<br>" + "First Name: " + firstName;
                successRouteMessage += "<br>" + "Last Name: " + lastName;
                successRouteMessage += "<br>" + "Email Address: " + emailAddress;
                successRouteMessage += "<br>" + "Contact Number: " + phone;
                successRouteMessage += "<br>" + "City: " + city;
                successRouteMessage += "<br>" + "Date of Birth: " + birthDate;
                successRouteMessage += "<br>" + "Your concern: " + concern;

                // create a jcr node
                createNode(request, firstName, lastName, emailAddress, phone, city, birthDate,
                        concern);
            }
        }
        return successRouteMessage;
    }
}
