package com.swach.webservice;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Created by swachtest on 01/05/15.
 */
public class TwitterResponseFilter implements ContainerResponseFilter{
    @Override
    public ContainerResponse filter(ContainerRequest req, ContainerResponse contResp) {

        ResponseBuilder resp = Response.fromResponse(contResp.getResponse());
                resp.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
               .header("Access-Control-Allow-Credentials", "true");
       System.out.println("filtering header added for CORS ");

        String reqHead = req.getHeaderValue("Access-Control-Request-Headers");

        if(null != reqHead && !reqHead.equals("")){
            resp.header("Access-Control-Allow-Headers", reqHead);
        }

        contResp.setResponse(resp.build());
        return contResp;
    }
}
