package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Color;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.query.DeviceQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.DeviceColorService;
import ru.softwerke.practice.app2019.service.DeviceTypeService;
import ru.softwerke.practice.app2019.service.EntityService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/device")
@Produces("application/json;charset=utf-8")
public class DeviceRestController {
    private final EntityService<Device> deviceService;
    private final DeviceColorService colorService;
    private final DeviceTypeService deviceTypeService;
    
    @Inject
    public DeviceRestController(EntityService<Device> deviceService, DeviceColorService colorService, DeviceTypeService deviceTypeService) {
        this.deviceService = deviceService;
        this.colorService = colorService;
        this.deviceTypeService = deviceTypeService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Device> getDevices(@Context UriInfo uriInfo) throws WebApplicationException {
        Query<Device> deviceQuery = new DeviceQuery(uriInfo);
        return deviceService.getRequestedListOfEntities(deviceQuery);
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device getDeviceById(@PathParam("id") String idByString) throws WebApplicationException {
        return deviceService.getEntity(Device.ENTITY_TYPE_NAME, idByString);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Device addNewDevice(Device newDevice) throws WebApplicationException {
        return deviceService.putEntity(newDevice);
    }
    
    @GET
    @Path("/color")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Color> getAvailableColors() throws WebApplicationException {
        return colorService.getColorList();
    }
    
    @POST
    @Path("/color")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Color addNewColor(Color color) {
        return colorService.putColor(color);
    }
    
    @GET
    @Path("/type")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAvailableDeviceTypes() throws WebApplicationException {
        return deviceTypeService.getDeviceTypeList();
    }
    
    @POST
    @Path("/type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addNewDeviceType(String deviceType) {
        return deviceTypeService.putDeviceType(deviceType);
    }
}
