package com.morrisons.base.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.morrisons.base.dropwizard.dao.CustomerDao;
import com.morrisons.base.dropwizard.model.CustomerTransaction;
import com.morrisons.base.dropwizard.model.CustomerTransactions;
import com.morrisons.base.dropwizard.services.CustomerService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

import static com.morrisons.base.dropwizard.resources.ApiConstants.*;
import static com.morrisons.base.dropwizard.resources.ParameterConstants.ACCOUNT_ID;

@Path("/customers")
@Api(value = "Resource", description = "Provides operations")
@SwaggerDefinition(basePath = BASE_PATH,
        info = @Info(title = APP_TITLE,
                description = APP_DESCRIPTION,
                version = APP_VERSION),
        securityDefinition = @SecurityDefinition(
                basicAuthDefinitions = {
                        @BasicAuthDefinition(key = BASIC_AUTH,
                                description = "HTTP Basic Authentication. Use either Basic Authentication or Bearer Authentication for resources that support both Authentication schemes")},
                apiKeyAuthDefinitions = {
                        @ApiKeyAuthDefinition(key = BEARER_AUTH,
                                name = "apikey",
                                in = ApiKeyAuthDefinition.ApiKeyLocation.QUERY,
                                description = "API key sent as a query parameter")}
        )
)
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

    private CustomerService customerService;

    @Inject
    public Resource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Timed
    @GET
    @Path("transactions/{accountId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets all transactions",
            response = CustomerTransactions.class,
            authorizations = {
                    @Authorization(value = BASIC_AUTH),
                    @Authorization(value = BEARER_AUTH)})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource ID not found"),
            @ApiResponse(code = 400, message = "Invalid resource spec")})
    public Response getAllCustomers(@ApiParam(value = "Account Id", required = true, name = "accountId") @PathParam("accountId") String accountId,
                                    @Context HttpHeaders headers) {

        return Response
                .ok(customerService.getCustomerTransactions(accountId))
                .build();
    }

    @Timed
    @POST
    @Path("transactions/{accountId}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Process an Instruction",
            authorizations = {
                    @Authorization(value = BASIC_AUTH),
                    @Authorization(value = BEARER_AUTH)})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = API_400),
            @ApiResponse(code = 404, message = API_404),
            @ApiResponse(code = 422, message = API_422),
            @ApiResponse(code = 500, message = API_500),
            @ApiResponse(code = 502, message = API_502)})
    public Response processInstruction(@ApiParam(value = "Account Id", required = true, name = ACCOUNT_ID) @PathParam(ACCOUNT_ID) String accountId,
                                       @ApiParam(value = "Input Data", required = true) CustomerTransaction transaction,
                                       @Context HttpHeaders headers) {

        customerService.addTransaction(transaction);
        return Response
                .accepted()
                .build();
    }


}
