package ru.softwerke.practice.app2019.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.testutil.TestUtils;
import ru.softwerke.practice.app2019.model.Color;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.query.DeviceQuery;
import ru.softwerke.practice.app2019.query.FilterOperator;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.storage.EntityStorage;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;

import javax.ws.rs.WebApplicationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeviceColorServiceTest extends EntityServiceImplTest {
    private static int DATA_SIZE = 1000;
    private static List<Device> devices = new ArrayList<>();
    private static EntityService<Device> entityService = new EntityServiceImpl<>(new EntityStorage<>());
    private static DeviceColorService deviceColorService = DeviceColorService.getInstance();
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
    void checkPresentDeviceColorFromAddingDeviceTest() {
        String colorName = TestUtils.getCustomDevice().getColorName();
        try {
            deviceColorService.checkDoesColorNameExist(colorName);
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @Test
    void checkPresentDeviceColorFromAddingDeviceColorTest() {
        Color newColor = new Color("color", 222222);
        deviceColorService.putColor(newColor);
        try {
            deviceColorService.checkDoesColorNameExist(newColor.getColorName());
            deviceColorService.checkDoesColorRGBExist(newColor.getColorRGB());
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @Test
    void checkNotExistingDeviceColorWithNameTest() {
        String notExistingColorName = "@#$" + TestUtils.getCustomDevice().getColorName() + "@#$";
        try {
            deviceColorService.checkDoesColorNameExist(notExistingColorName);
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @Test
    void checkNotExistingDeviceColorWithRGBTest() {
        int colorRGB = TestUtils.getCustomDevice().getColorRGB() + 1;
        try {
            deviceColorService.checkDoesColorRGBExist(colorRGB);
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @Test
    void checkSameNameDifferentRGBCodeDeviceAddingTest() {
        long price = 1111111;
        String type = TestUtils.deviceTypes[random.nextInt(TestUtils.deviceTypes.length)];
        LocalDate date = Instant.ofEpochMilli(121212).atZone(ZoneId.systemDefault()).toLocalDate();
        Color color = new Color(TestUtils.getCustomDevice().getColorName(), TestUtils.getCustomDevice().getColorRGB() + 1);
        String manufacturer = TestUtils.manufacturers[random.nextInt(TestUtils.manufacturers.length)];
        String modelName = "A-10";
        try {
            Device device = new Device(modelName, type, manufacturer, color.getColorName(), color.getColorRGB().toString(),
                    date.format(ParseFromStringParam.dateFormatter), Long.toString(price));
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void checkSameNameDifferentRGBCodeDeviceColorAddingTest() {
        try {
            deviceColorService.putColor(new Color(TestUtils.getCustomDevice().getColorName(),
                    TestUtils.getCustomDevice().getColorRGB() + 1));
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void getFilteredEqDeviceColorListTest() {
        String colorName = TestUtils.getCustomDevice().getColorName();
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getColorName,
                        FilterOperator.EQ,
                        colorName
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Device device : devices) {
                if (device.getColorName().equalsIgnoreCase(colorName)) {
                    assertTrue(filteredList.contains(device),
                            String.format("the resulting list does not have the device with '%s' colorName", colorName));
                } else {
                    assertFalse(filteredList.contains(device),
                            String.format("the resulting list has the device with non '%s' colorName", colorName));
                }
            }
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur", e);
        }
    }
    
    @Test
    void getFilteredEqNotExistingDeviceColorListTest() {
        String colorName = "@$" + TestUtils.getCustomDevice().getColorName() + "@$";
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getColorName,
                        FilterOperator.EQ,
                        colorName
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @Test
    void getFilteredEqDeviceColorRGBListTest() {
        int colorRGB = TestUtils.getCustomDevice().getColorRGB();
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getColorRGB,
                        FilterOperator.EQ,
                        colorRGB
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Device device : devices) {
                if (device.getColorRGB() == colorRGB) {
                    assertTrue(filteredList.contains(device),
                            String.format("the resulting list does not have the device with '%s' colorRGB", colorRGB));
                } else {
                    assertFalse(filteredList.contains(device),
                            String.format("the resulting list has the device with non '%s' colorRGB", colorRGB));
                }
            }
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur", e);
        }
    }
    
    @Test
    void getFilteredEqNotExistingDeviceColorRGBListTest() {
        int colorRGB = TestUtils.getCustomDevice().getColorRGB() + 1;
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getColorRGB,
                        FilterOperator.EQ,
                        colorRGB
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
}
