package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.model.BillItem;
import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.query.BillQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.EntityService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/bill")
@Produces("application/json;charset=utf-8")
public class BillRestController {
    private final EntityService<Bill> billService;
    private final EntityService<Customer> customerService;
    private final EntityService<Device> deviceService;
    
    @Inject
    public BillRestController(EntityService<Bill> billService,
                              EntityService<Customer> customerService,
                              EntityService<Device> deviceService) {
        this.billService = billService;
        this.customerService = customerService;
        this.deviceService = deviceService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bill> getBills(@Context UriInfo uriInfo) throws WebApplicationException {
        Query<Bill> billQuery = new BillQuery(uriInfo);
        return billService.getRequestedListOfEntities(billQuery);
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bill getBillById(@PathParam("id") String idByString) throws WebApplicationException {
        return billService.getEntity(Bill.ENTITY_TYPE_NAME, idByString);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Bill addNewBill(Bill newBill) throws WebApplicationException {
        customerService.checkDoesContainEntityWithId(Customer.ENTITY_TYPE_NAME, newBill.getCustomerId());
        for (BillItem billItem : newBill.getItems()) {
            deviceService.checkDoesContainEntityWithId(Device.ENTITY_TYPE_NAME, billItem.getDeviceId());
        }
        return billService.putEntity(newBill);
    }
}