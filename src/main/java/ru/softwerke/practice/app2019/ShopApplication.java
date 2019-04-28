package ru.softwerke.practice.app2019;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.service.EntityService;
import ru.softwerke.practice.app2019.service.EntityServiceImpl;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.ApplicationPath;
import java.util.LinkedHashMap;

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
                bind(new EntityServiceImpl<>(new EntityStorage<Bill>())).to(new TypeLiteral<EntityService<Bill>>(){});
            }
        });
        setProperties(new LinkedHashMap<String, Object>() {{
            put(org.glassfish.jersey.server.ServerProperties.PROCESSING_RESPONSE_ERRORS_ENABLED, true);
        }});
    }
}
