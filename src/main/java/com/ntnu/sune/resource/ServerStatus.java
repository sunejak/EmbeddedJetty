package com.ntnu.sune.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sune Jakobsson,
 * @version  Mar 2014
 */

@Path("mem_status")
@PermitAll

public class ServerStatus {

    private static final Logger LOGGER = Logger.getLogger(ServerStatus.class.getName());

    /**
     * Get status of server.
     *
     * @return Amount of free memory and time since 1970
     */
    @GET
    public Response getServerStatus( @Context UriInfo uriInfo) {

            long now = System.currentTimeMillis();
            long freeMem = Runtime.getRuntime().freeMemory();
        LOGGER.log(Level.ALL, "Got Called with GET");
            return Response.status(Response.Status.OK).entity("Free Memory: " + freeMem
                    + " bytes " + now + "\n").build();
    }
}
