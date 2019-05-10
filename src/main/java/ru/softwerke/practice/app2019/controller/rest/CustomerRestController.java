package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.query.CustomerQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.EntityService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/customer")
@Produces("application/json;charset=utf-8")
public class CustomerRestController {
    private final EntityService<Customer> service;
    
    @Inject
    public CustomerRestController(EntityService<Customer> deviceService) {
        this.service = deviceService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers(@Context UriInfo uriInfo) throws WebApplicationException {
        Query<Customer> customerQuery = new CustomerQuery(uriInfo);
        return service.getRequestedListOfEntities(customerQuery);
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getDeviceById(@PathParam("id") String idByString) throws WebApplicationException {
        return service.getEntity(Customer.ENTITY_TYPE_NAME, idByString);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer addNewCustomer(Customer newCustomer) throws WebApplicationException {
        return service.putEntity(newCustomer);
    }
}

