package com.deloitte.components;

//import com.adobe.cq.sightly.WCMUsePojo;
//import org.apache.sling.api.resource.ValueMap;

public class ChildMapperWcmUse  {

    private String pageTitle;

//    @Override
//    public void activate() throws Exception {
////        ValueMap properties = this.getResource().getValueMap();
//        if(properties != null){
//            pageTitle = properties.get("jcr:Title").toString();
//        }
//    }

    public String getPageTitle() {
        return pageTitle;
    }

}
