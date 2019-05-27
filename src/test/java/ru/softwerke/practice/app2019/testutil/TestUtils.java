package ru.softwerke.practice.app2019.testutil;

import org.apache.commons.lang3.RandomStringUtils;
import ru.softwerke.practice.app2019.model.*;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {
    private static final int BILL_ITEMS_SIZE = 999;
    private static final String[] names = new String[]{"Петр", "Петрушка", "Пет", "Петя", "Иван", "Илья", "John", "Jack"};
    private static final Color[] colors = new Color[]{
            new Color("черный", 0), new Color("белый", 16777215),
            new Color("золотистый", 16766720), new Color("синий", 255)};
    
    public static final String[] manufacturers = new String[]{"Samsung", "Nokia", "Apple", "HP"};
    public static final String[] deviceTypes = new String[]{"Laptop", "Smartphone", "Smart Watches", "Tablet"};
    
    public static long deviceId = -1;
    
    public static Device getRandomDevice(Random random) {
        deviceId++;
        long price = random.nextInt(Integer.MAX_VALUE);
        String type = deviceTypes[random.nextInt(deviceTypes.length)];
        LocalDate date = Instant.ofEpochMilli(random.nextInt(Integer.MAX_VALUE)).atZone(ZoneId.systemDefault()).toLocalDate();
        Color color = colors[random.nextInt(colors.length)];
        String manufacturer = manufacturers[random.nextInt(manufacturers.length)];
        String modelName = RandomStringUtils.random(10, true, true);
        return new Device(modelName, type, manufacturer, color.getColorName(), color.getColorRGB().toString(),
                date.format(ParseFromStringParam.dateFormatter), Long.toString(price));
    }
    
    public static Customer getRandomCustomer(Random random) {
        String firstName = names[random.nextInt(names.length)];
        String middleName = RandomStringUtils.random(10, true, false);
        
        String lastName = RandomStringUtils.random(10, true, false);
        
        LocalDate date = Instant.ofEpochMilli(random.nextInt(Integer.MAX_VALUE)).atZone(ZoneId.systemDefault()).toLocalDate();
        return new Customer(firstName, middleName, lastName, date.format(ParseFromStringParam.dateFormatter));
    }
    
    public static Bill getRandomBill(Random random) {
        long customerId = random.nextInt(Integer.MAX_VALUE);
        List<BillItem> billItems = new ArrayList<>();
        for (int i = 0; i < BILL_ITEMS_SIZE - 1; ++i) {
            billItems.add(getRandomBillItem(random));
        }
        // last bill item will be custom or random
        if (random.nextBoolean()) {
            billItems.add(TestUtils.getCustomBillItem());
        } else {
            billItems.add(getRandomBillItem(random));
        }
        return new Bill(Long.toString(customerId), billItems);
    }
    
    private static BillItem getRandomBillItem(Random random) {
        long deviceId = random.nextInt(Integer.MAX_VALUE - 1);
        long quantity = random.nextInt(Integer.MAX_VALUE - 1);
        long price = random.nextInt(Integer.MAX_VALUE - 1);
        return new BillItem(Long.toString(deviceId), Long.toString(quantity), Long.toString(price));
    }
    
    public static BillItem getCustomBillItem() {
        return new BillItem(Integer.toString(Integer.MAX_VALUE), Integer.toString(Integer.MAX_VALUE), Integer.toString(Integer.MAX_VALUE));
    }
    
    public static Device getCustomDevice() {
        deviceId++;
        long price = 1111111;
        String type = deviceTypes[0];
        LocalDate date = Instant.ofEpochMilli(121212).atZone(ZoneId.systemDefault()).toLocalDate();
        Color color = new Color("NewColor", 111111);
        String manufacturer = manufacturers[0];
        String modelName = "A-10";
        return new Device(modelName, type, manufacturer, color.getColorName(), color.getColorRGB().toString(),
                date.format(ParseFromStringParam.dateFormatter), Long.toString(price));
        
    }
}
