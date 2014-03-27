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

    private static final Integer[] cnt = new Integer[Response.Status.values().length];

    public static MonitoringResponseFilter getInstance(){
        return mf;
    }

    public Integer[] getCount() {
        return cnt;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response){

        if (response.getStatus() < (Response.Status.MOVED_PERMANENTLY.getStatusCode()-2) && response.getStatus() >= Response.Status.OK.getStatusCode()) {

            Response.Status[] responseStatus = Response.Status.values();
            for ( int x = 0; x < Response.Status.values().length ; x++)
                if (responseStatus[x].getStatusCode() == response.getStatus()) {
                    synchronized (cnt) {
                        if (cnt[x] == null) cnt[x] = 0;
                        cnt[x]++;
                    }
                    break;
                }
        }
        LOGGER.log(Level.FINEST, response.getStatusType().toString());
        return response;
    }
}