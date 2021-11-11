package com.deloitte.components;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationList {

    @Inject
    private String[] navigationEntry;

    @Inject
    private String[] dropdownEntry;

    @Inject String title;


    public List<String> getNavigationList() {
        return Arrays.asList(navigationEntry);
    }

    private List<String> navigationList;
}
