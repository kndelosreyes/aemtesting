package com.deloitte.components;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactInfo {

    @Inject
    private String inputField1;

    @Inject
    private String inputField2;

    public String getInputField1() {
        return inputField1;
    }

    public String getInputField2() {
        return inputField2;
    }
}
