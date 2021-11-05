package com.deloitte.components;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChildMapperWcmUse extends WCMUsePojo  {

    private String parentPath;
    private List<Page> childPages;
    private NavigationList navigation;
    private List<String> customers;

    @Override
    public void activate() throws Exception {
        ValueMap properties = this.getResource().getValueMap();
        if(properties != null){
            parentPath = properties.get("parentPath", String.class);
            Resource parentResource = this.getResourceResolver().getResource(parentPath);
            if(parentResource != null){
                Page parentPage = parentResource.adaptTo(Page.class);
                Iterator<Page> childPageList = parentPage.listChildren();
                childPages = new ArrayList<>();
                while(childPageList.hasNext()){
                    childPages.add(childPageList.next());
                }
            }

            // get list from multifield component
            Resource holderResource = this.getResourceResolver().getResource(parentPath + "/jcr:content/par");
            if(holderResource != null){
                Iterator<Resource> childResource = holderResource.getChildren().iterator();
                while (childResource.hasNext()){
                    Resource resourceEntry = childResource.next();
                    if(resourceEntry.isResourceType("aemtesting/components/content/multifield-comp")){
//                        navigation = resourceEntry.adaptTo(NavigationList.class);
                        ValueMap multifieldProp =  resourceEntry.getValueMap();
                        if(multifieldProp != null){
                            customers = Arrays.asList(multifieldProp.get("navigationEntry", String[].class));
                        }
                    }
                }
            }
        }
    }

    public String getParentPath() {
        return parentPath;
    }
    public List<Page> getChildPages() {
        return childPages;
    }

    public NavigationList getNavigation() {
        return navigation;
    }
    public List<String> getCustomers() {
        return customers;
    }
}
