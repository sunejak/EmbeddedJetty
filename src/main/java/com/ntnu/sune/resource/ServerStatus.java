package com.ntnu.sune.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by ttjsun on 24.03.14.
 */

@Path("healthz")
@PermitAll

public class ServerStatus {

    /**
     * Get status of server.
     *
     * @return Amount of free memory and time since 1970
     */
    @GET
    public Response getServerStatus() {

            long now = System.currentTimeMillis();
            long freeMem = Runtime.getRuntime().freeMemory();
            return Response.status(Response.Status.OK).entity("Free Memory: " + freeMem
                    + " bytes " + now + "\n").build();
    }
}
