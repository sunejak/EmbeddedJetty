package com.ntnu.sune.resource;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sune Jakobsson,
 * @version  Mar 2014
 */
public class MonitoringResponseFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(MonitoringResponseFilter.class.getName());
    private static final MonitoringResponseFilter mf = new MonitoringResponseFilter();

    private static Integer cnt = 0;

    public static MonitoringResponseFilter getInstance(){
        return mf;
    }

    int getCount() {
        return cnt;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response){

        if (response.getStatus() < (Response.Status.MOVED_PERMANENTLY.getStatusCode()-2) && response.getStatus() >= Response.Status.OK.getStatusCode()) {

            synchronized (cnt){
                cnt++;
            }
            LOGGER.log(Level.ALL, cnt.toString());
        }
        return response;
    }
}
