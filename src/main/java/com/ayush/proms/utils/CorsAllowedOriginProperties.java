package com.ayush.proms.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "allowed-origin")
public class CorsAllowedOriginProperties {

    private String frontendUrl;


    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public List<String> getAll(){
        List<String> allowedOrigins=new ArrayList<>();
        if(this.frontendUrl!=null)allowedOrigins.add(this.frontendUrl);
        return allowedOrigins;
    }
}
