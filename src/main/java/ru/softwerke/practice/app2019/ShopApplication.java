package ru.softwerke.practice.app2019;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.model.Color;
import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.service.DeviceColorService;
import ru.softwerke.practice.app2019.service.DeviceTypeService;
import ru.softwerke.practice.app2019.service.EntityService;
import ru.softwerke.practice.app2019.service.EntityServiceImpl;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ShopApplication extends ResourceConfig {
    public ShopApplication() {
        packages("ru.softwerke.practice.app2019;com.fasterxml.jackson.jaxrs");
        
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new EntityServiceImpl<>(new EntityStorage<Device>())).to(new TypeLiteral<EntityService<Device>>() {
                });
                bind(new EntityServiceImpl<>(new EntityStorage<Customer>())).to(new TypeLiteral<EntityService<Customer>>() {
                });
                bind(new EntityServiceImpl<>(new EntityStorage<Bill>())).to(new TypeLiteral<EntityService<Bill>>() {
                });
                bind(colorService()).to(DeviceColorService.class);
                bind(deviceTypeService()).to(DeviceTypeService.class);
            }
        });
    }
    
    private DeviceTypeService deviceTypeService() {
        DeviceTypeService deviceTypeService = DeviceTypeService.getInstance();
        deviceTypeService.putDeviceType("Smartphone");
        deviceTypeService.putDeviceType("Laptop");
        deviceTypeService.putDeviceType("Smart Watches");
        deviceTypeService.putDeviceType("Tablet");
        return deviceTypeService;
    }
    
    private DeviceColorService colorService() {
        DeviceColorService deviceColorService = DeviceColorService.getInstance();
        deviceColorService.putColor(new Color("черный", 0));
        deviceColorService.putColor(new Color("серый", 8421504));
        deviceColorService.putColor(new Color("красный", 16711680));
        deviceColorService.putColor(new Color("золотистый", 16766720));
        deviceColorService.putColor(new Color("синий", 255));
        deviceColorService.putColor(new Color("серебристый", 12632256));
        deviceColorService.putColor(new Color("белый", 16777215));
        deviceColorService.putColor(new Color("коричневый", 9849600));
        deviceColorService.putColor(new Color("оранжевый", 16753920));
        deviceColorService.putColor(new Color("бежевый", 16119260));
        deviceColorService.putColor(new Color("желтый", 16776960));
        deviceColorService.putColor(new Color("зеленый", 32768));
        deviceColorService.putColor(new Color("голубой", 3975935));
        deviceColorService.putColor(new Color("фиолетовый", 9109759));
        deviceColorService.putColor(new Color("розовый", 16519104));
        return deviceColorService;
    }
}