package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.query.DeviceQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.EntityService;
import ru.softwerke.practice.app2019.util.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/device")
@Produces("application/json;charset=utf-8")
public class DeviceRestController extends BaseRestController<Device> {
    
    @Inject
    public DeviceRestController(EntityService<Device> deviceService) {
        this.service = deviceService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Device> getDevices(@Context UriInfo uriInfo) throws WebApplicationException {
        queryBuilder = new DeviceQuery.Builder();
        MultivaluedMap<String, String> queryParams = getCommonQueryProperty(uriInfo);
        DeviceQuery.Builder deviceQueryBuilder = (DeviceQuery.Builder) queryBuilder;
        
        for (String key : queryParams.keySet()) {
            switch (key) {
                case DeviceQuery.MODEL: {
                    deviceQueryBuilder.setModel(new StringParam(queryParams.getFirst(key), DeviceQuery.MODEL,
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.TYPE: {
                    deviceQueryBuilder.setType(new StringParam(queryParams.getFirst(key), DeviceQuery.TYPE,
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.PRODUCER: {
                    deviceQueryBuilder.setProducer(new StringParam(queryParams.getFirst(key), DeviceQuery.PRODUCER,
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.COLOR: {
                    deviceQueryBuilder.setColor(new ColorParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.DATE: {
                    deviceQueryBuilder.setDate(new DateParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.DATE_FROM: {
                    deviceQueryBuilder.setDateFrom(new DateParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.DATE_TO: {
                    deviceQueryBuilder.setDateTo(new DateParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE: {
                    deviceQueryBuilder.setPrice(new PriceParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE_FROM: {
                    deviceQueryBuilder.setPriceFrom(new PriceParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE_TO: {
                    deviceQueryBuilder.setPriceTo(new PriceParam(queryParams.getFirst(key),
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case Query.ORDER_TYPE: {
                    List<String> allOrderTypes = queryParams.get(key);
                    for (String type : allOrderTypes) {
                        if (DeviceQuery.getOrderParamsMap().containsKey(type)) {
                            if (entityComparator == null) {
                                entityComparator = DeviceQuery.getOrderParamsMap().get(type);
                            } else {
                                entityComparator = entityComparator.thenComparing(DeviceQuery.getOrderParamsMap().get(type));
                            }
                        } else {
                            Response response = QueryUtils.
                                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                                            "malformed query parameters",
                                            String.format("the value \"%s\" of query parameter %s is malformed",
                                                    type,
                                                    DeviceQuery.ORDER_TYPE));
                            throw new WebApplicationException(response);
                        }
                    }
                    break;
                }
                default: {
                    Response response = QueryUtils.
                            getResponseWithMessage(Response.Status.BAD_REQUEST,
                                    "malformed query parameters",
                                    String.format("the query parameter \"%s\" is malformed", key));
                    throw new WebApplicationException(response);
                }
            }
        }
        deviceQueryBuilder.setDeviceComparator(entityComparator);
        return getEntities(deviceQueryBuilder.build());
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device getDeviceById(@PathParam("id") String idByString) throws WebApplicationException {
        return getEntityById(idByString, Device.getName());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Device addNewDevice(Device newDevice) throws WebApplicationException {
        return addNewEntity(newDevice);
    }
}
