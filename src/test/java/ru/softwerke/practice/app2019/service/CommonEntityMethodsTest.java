package ru.softwerke.practice.app2019.service;

import org.junit.Assert;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.softwerke.practice.app2019.testutil.TestUtils;
import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.WebApplicationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Common entity's methods tests for {@code Device} taken as a sample
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommonEntityMethodsTest extends EntityServiceImplTest {
    private static final int REPEAT_COUNT = 100;
    private static EntityService<Device> entityService = new EntityServiceImpl<>(new EntityStorage<>());
    
    
    @RepeatedTest(value = REPEAT_COUNT, name = "adding {currentRepetition} / {totalRepetitions}")
    void putRandomEntity() {
        long curId = TestUtils.deviceId + 1;
        Device device = TestUtils.getRandomDevice(random);
        Device addedDevice = entityService.putEntity(device);
        assertNotNull(addedDevice, "returned device is null");
        assertEquals(addedDevice.getId(), curId, "added device id is invalid");
        assertEquals(addedDevice, device, "returned device is not the same as added device");
        assertEquals(entityService.getEntity(Device.ENTITY_TYPE_NAME, String.valueOf(addedDevice.getId())), device,
                "method getEntityById does not work correct");
    }
    
    @Test
    void putNullEntity() {
        try {
            entityService.putEntity(null);
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void getEntityByIdInRandomOrder() {
        Device[] devices = new Device[1000];
        List<Long> identifiers = new ArrayList<>();
        Map<Long, Integer> identifiersToIndexes = new HashMap<>();
        for (int i = 0; i < devices.length; ++i) {
            devices[i] = TestUtils.getRandomDevice(random);
            Device addDevice = entityService.putEntity(devices[i]);
            identifiers.add(addDevice.getId());
            identifiersToIndexes.put(addDevice.getId(), i);
        }
        Collections.shuffle(identifiers);
        for (int i = 0; i < devices.length; ++i) {
            assertEquals(devices[identifiersToIndexes.get(identifiers.get(i))],
                    entityService.getEntity(Device.ENTITY_TYPE_NAME, String.valueOf(identifiers.get(i))),
                    "method getEntityById does not work correct");
        }
    }
    
    @Test
    void checkEntityWithNotExistingId() {
        try {
            entityService.checkDoesContainEntityWithId(Device.ENTITY_TYPE_NAME, Integer.MAX_VALUE);
            fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
}
