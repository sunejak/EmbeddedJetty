package com.ntnu.sune.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sune Jakobsson,
 * @version  Mar 2014
 */

@Path("usage_status")
@PermitAll

public class UsageStatus {

    private static final Logger LOGGER = Logger.getLogger(UsageStatus.class.getName());

    /**
     * Get status of server.
     *
     * @return Amount of free memory and time since 1970
     */
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getServerStatus( @Context UriInfo uriInfo) {

        LOGGER.log(Level.ALL, "Got Called with GET");
        MonitoringResponseFilter monitoringResponseFilter = MonitoringResponseFilter.getInstance();
        if(monitoringResponseFilter != null){
            return Response.status(Response.Status.OK).entity("Number of invocations: " + monitoringResponseFilter.getCount() + "\n").build();
        }
        return null;
    }
}
