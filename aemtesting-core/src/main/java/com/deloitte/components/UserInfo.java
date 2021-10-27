package com.deloitte.components;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserInfo {

    @Inject
    private String firstName;

    @Inject
    private String lastName;

    @Inject
    private String dateOfBirthFormat;

    @Inject Resource resource;

    public String getResourceType() {
        return resource.getResourceType();
    }

    private String resourceType;

    public String getDateOfBirth() {
        Date currentDate = new Date();
        return new SimpleDateFormat(dateOfBirthFormat).format(currentDate);
    }

    private String dateOfBirth;



    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getDateOfBirthFormat() {
        return dateOfBirthFormat;
    }


}
