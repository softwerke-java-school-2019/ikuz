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
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.MODEL_PREFIX: {
                    deviceQueryBuilder.setModelPrefix(new StringParam(queryParams.getFirst(key), DeviceQuery.MODEL_PREFIX,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.TYPE: {
                    deviceQueryBuilder.setType(new StringParam(queryParams.getFirst(key), DeviceQuery.TYPE,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.TYPE_PREFIX: {
                    deviceQueryBuilder.setTypePrefix(new StringParam(queryParams.getFirst(key), DeviceQuery.TYPE_PREFIX,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.MANUFACTURER: {
                    deviceQueryBuilder.setProducer(new StringParam(queryParams.getFirst(key), DeviceQuery.MANUFACTURER,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.MANUFACTURER_PREFIX: {
                    deviceQueryBuilder.setManufacturerPrefix(new StringParam(queryParams.getFirst(key), DeviceQuery.MANUFACTURER_PREFIX,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.COLOR: {
                    deviceQueryBuilder.setColorName(new StringParam(queryParams.getFirst(key),
                            DeviceQuery.COLOR,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.COLOR_RGB: {
                    deviceQueryBuilder.setColorRGB(new IntegerParam(queryParams.getFirst(key),
                            DeviceQuery.COLOR_RGB,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE: {
                    deviceQueryBuilder.setDate(new DateParam(queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE,
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE_FROM: {
                    deviceQueryBuilder.setDateFrom(new DateParam(queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE_TO: {
                    deviceQueryBuilder.setDateTo(new DateParam(queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE: {
                    deviceQueryBuilder.setPrice(new IntegerParam(queryParams.getFirst(key),
                            DeviceQuery.PRICE,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE_FROM: {
                    deviceQueryBuilder.setPriceFrom(new IntegerParam(queryParams.getFirst(key),
                            DeviceQuery.PRICE,
                            Query.FILTER + Device.getName()));
                    break;
                }
                case DeviceQuery.PRICE_TO: {
                    deviceQueryBuilder.setPriceTo(new IntegerParam(queryParams.getFirst(key),
                            DeviceQuery.PRICE,
                            Query.FILTER + " " + Device.getName()));
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.get(key), DeviceQuery.getOrderParamsMap(), Device.getName());
                    break;
                }
                default: {
                    sendWrongParamsMessage(key, Device.getName());
                }
            }
        }
        deviceQueryBuilder.setComparator(entityComparator);
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
