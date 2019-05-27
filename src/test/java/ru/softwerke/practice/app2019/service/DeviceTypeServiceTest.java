package ru.softwerke.practice.app2019.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.testutil.TestUtils;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.query.DeviceQuery;
import ru.softwerke.practice.app2019.query.FilterOperator;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTypeServiceTest extends EntityServiceImplTest {
    private static int DATA_SIZE = 1000;
    private static List<Device> devices = new ArrayList<>();
    private static EntityService<Device> entityService = new EntityServiceImpl<>(new EntityStorage<>());
    private static DeviceTypeService deviceTypeService = DeviceTypeService.getInstance();
    private QueryConditionsHolder<Device> holder;
    
    
    @BeforeAll
    static void init() {
        for (int i = 0; i < DATA_SIZE - 1; i++) {
            Device randomDevice = TestUtils.getRandomDevice(random);
            devices.add(randomDevice);
            entityService.putEntity(randomDevice);
        }
        Device customDevice = TestUtils.getCustomDevice();
        
        entityService.putEntity(customDevice);
        devices.add(customDevice);
    }
    
    @BeforeEach
    void initHolder() {
        holder = new QueryConditionsHolder<>();
    }
    
    @Test
    void checkPresentDeviceTypeFromAddingDeviceTest() {
        String type = TestUtils.deviceTypes[random.nextInt(TestUtils.deviceTypes.length)];
        try {
            deviceTypeService.checkDoesDeviceTypeExist(type);
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @Test
    void checkPresentDeviceTypeFromAddingDeviceTypeTest() {
        String newType = "NoteBook";
        deviceTypeService.putDeviceType(newType);
        try {
            deviceTypeService.checkDoesDeviceTypeExist(newType);
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @Test
    void checkNotExistingDeviceTypeTest() {
        String type = "$@s" + TestUtils.deviceTypes[random.nextInt(TestUtils.deviceTypes.length)] + "s$@";
        try {
            deviceTypeService.checkDoesDeviceTypeExist(type);
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @Test
    void getFilteredEqDeviceTypeListTest() {
        String type = TestUtils.getCustomDevice().getDeviceType();
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getDeviceType,
                        FilterOperator.EQ,
                        type
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Device device : devices) {
                if (device.getDeviceType().equalsIgnoreCase(type)) {
                    assertTrue(filteredList.contains(device),
                            String.format("the resulting list does not have the device with '%s' type", type));
                } else {
                    assertFalse(filteredList.contains(device),
                            String.format("the resulting list has the device with non '%s' type", type));
                }
            }
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @Test
    void getFilteredEqNotExistingDeviceTypeListTest() {
        String type = "$@" + TestUtils.deviceTypes[random.nextInt(TestUtils.deviceTypes.length)] + "$@";
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getDeviceType,
                        FilterOperator.EQ,
                        type
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
}