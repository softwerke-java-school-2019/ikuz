package ru.softwerke.practice.app2019.controller.rest;


import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.query.CustomerQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.EntityService;
import ru.softwerke.practice.app2019.util.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/customer")
@Produces("application/json;charset=utf-8")
public class CustomerRestController extends BaseRestController<Customer> {
    
    @Inject
    public CustomerRestController(EntityService<Customer> deviceService) {
        this.service = deviceService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers(@Context UriInfo uriInfo) throws WebApplicationException {
        queryBuilder = new CustomerQuery.Builder();
        MultivaluedMap<String, String> queryParams = getCommonQueryProperty(uriInfo);
        CustomerQuery.Builder customerQueryBuilder = (CustomerQuery.Builder) queryBuilder;
        
        for (String key : queryParams.keySet()) {
            switch (key) {
                case CustomerQuery.FIRST_NAME: {
                    customerQueryBuilder.setFirstName(new StringParam(queryParams.getFirst(key), CustomerQuery.FIRST_NAME,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case CustomerQuery.LAST_NAME: {
                    customerQueryBuilder.setLastName(new StringParam(queryParams.getFirst(key), CustomerQuery.LAST_NAME,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case CustomerQuery.MIDDLE_NAME: {
                    customerQueryBuilder.setMiddleName(new StringParam(queryParams.getFirst(key), CustomerQuery.MIDDLE_NAME,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case CustomerQuery.FIRST_NAME_PREFIX: {
                    customerQueryBuilder.setFirstNamePrefix(new StringParam(queryParams.getFirst(key), CustomerQuery.FIRST_NAME_PREFIX,
                            Query.FILTER + Customer.getName()));
                }
                case CustomerQuery.MIDDLE_NAME_PREFIX: {
                    customerQueryBuilder.setMiddleNamePrefix(new StringParam(queryParams.getFirst(key), CustomerQuery.MIDDLE_NAME_PREFIX,
                            Query.FILTER + Customer.getName()));
                }
                case CustomerQuery.LAST_NAME_PREFIX: {
                    customerQueryBuilder.setLastNamePrefix(new StringParam(queryParams.getFirst(key), CustomerQuery.LAST_NAME_PREFIX,
                            Query.FILTER + Customer.getName()));
                }
                case CustomerQuery.BIRTH_DATE: {
                    customerQueryBuilder.setBirthDate(new DateParam(queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case CustomerQuery.BIRTH_DATE_FROM: {
                    customerQueryBuilder.setBirthDateFrom(new DateParam(queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case CustomerQuery.BIRTH_DATE_TO: {
                    customerQueryBuilder.setBirthDateTo(new DateParam(queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE,
                            Query.FILTER + Customer.getName()));
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.get(key), CustomerQuery.getOrderParamsMap(), Customer.getName());
                    break;
                }
                default: {
                    sendWrongParamsMessage(key, Customer.getName());
                }
            }
        }
        customerQueryBuilder.setComparator(entityComparator);
        return getEntities(customerQueryBuilder.build());
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getDeviceById(@PathParam("id") String idByString) throws WebApplicationException {
        return getEntityById(idByString, Customer.getName());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer addNewCustomer(Customer newCustomer) throws WebApplicationException {
        return addNewEntity(newCustomer);
    }
}

