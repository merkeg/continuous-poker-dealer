package org.continuouspoker.dealer.exceptionhandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.continuouspoker.dealer.exceptionhandling.exceptions.ObjectNotFoundException;

@Provider
public class ResponseExceptionMapper implements ExceptionMapper<ObjectNotFoundException> {

    @Override
    public Response toResponse(final ObjectNotFoundException onfe) {
        return Response.status(Response.Status.NOT_FOUND).entity(onfe.getMessage()).build();
    }

}
