package ru.softwerke.practice.app2019.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeviceServiceImplTest extends EntityServiceImplTest {
    private static final int REPEAT_COUNT = 100;
    private static int DATA_SIZE = 1000;
    private static List<Device> devices = new ArrayList<>();
    private static EntityService<Device> entityService = new EntityServiceImpl<>(new EntityStorage<>());
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
    void getFilteredEqListTest() {
        String manufacturer = TestUtils.getCustomDevice().getManufacturer();
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getManufacturer,
                        FilterOperator.EQ,
                        manufacturer
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Device device : devices) {
                if (device.getManufacturer().equalsIgnoreCase(manufacturer)) {
                    assertTrue(filteredList.contains(device),
                            String.format("the resulting list does not have the device with '%s' manufacturer", manufacturer));
                } else {
                    assertFalse(filteredList.contains(device),
                            String.format("the resulting list has the device with non '%s' manufacturer", manufacturer));
                }
            }
        } catch (WebApplicationException e) {
            fail("That should be valid filtering but WebApplication exception occur");
        }
    }
    
    @Test
    void getFilteredLessOrEqListTest() {
        long price = random.nextInt(Integer.MAX_VALUE);
        holder.addFilterCondition(new QueryCondition<>(
                        Device::getPrice,
                        FilterOperator.LESS_OR_EQ,
                        price
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Device device : devices) {
                for (int i = 0; i < filteredList.size(); i++) {
                    if (device.getPrice() <= price) {
                        assertTrue(filteredList.contains(device),
                                String.format("the resulting list does not have the device with '%s' price", price));
                    } else {
                        assertFalse(filteredList.contains(device),
                                String.format("the resulting list has the device with non '%s' price", price));
                    }
                }
            }
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @Test
    void getOrderedListTest() {
        holder.setQueryComparator(Comparator.comparing(Device::getPrice));
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Device> filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (int i = 1; i < filteredList.size(); i++) {
                assertFalse(filteredList.get(i).getPrice() < filteredList.get(i - 1).getPrice(),
                        "the sorting by price param is incorrect");
            }
        } catch (WebApplicationException e) {
            fail("That should be valid adding and getting but WebApplication exception occur");
        }
    }
    
    @RepeatedTest(value = REPEAT_COUNT, name = "adding {currentRepetition} / {totalRepetitions}")
    void getListWithCustomPageAndCountTest() {
        int countParam = random.nextInt(DATA_SIZE);
        int pageParam = random.nextInt(DATA_SIZE);
        List<Device> nonPageFilteredList;
        List<Device> filteredList;
        try {
            holder.setCountOfElementsParam(DATA_SIZE);
            nonPageFilteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(nonPageFilteredList.isEmpty(), "Expected WebApplicationException did not occur");
            holder.setCountOfElementsParam(countParam);
            holder.setPageParam(pageParam);
            filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (int i = 0; i < filteredList.size(); i++) {
                assertEquals(filteredList.get(i), nonPageFilteredList.get(countParam * (pageParam - 1) + i),
                        "incorrect type of output");
            }
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
    
    @RepeatedTest(value = REPEAT_COUNT, name = "adding {currentRepetition} / {totalRepetitions}")
    void getListWithCustomOffsetAndCountTest() {
        int offsetParam = random.nextInt(DATA_SIZE);
        int countParam = random.nextInt(DATA_SIZE);
        List<Device> nonOffsetFilteredList;
        List<Device> filteredList;
        try {
            holder.setCountOfElementsParam(DATA_SIZE);
            nonOffsetFilteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(nonOffsetFilteredList.isEmpty(), "Expected WebApplicationException did not occur");
            holder.setCountOfElementsParam(countParam);
            holder.setOffsetParam(offsetParam);
            holder.changeDisplayToOffsetOption();
            filteredList = entityService.getRequestedListOfEntities(new DeviceQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (int i = 0; i < filteredList.size(); i++) {
                assertEquals(filteredList.get(i), nonOffsetFilteredList.get(offsetParam + i),
                        "incorrect type of output");
            }
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
}