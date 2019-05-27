package ru.softwerke.practice.app2019.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.testutil.TestUtils;
import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.model.BillItem;
import ru.softwerke.practice.app2019.query.BillQuery;
import ru.softwerke.practice.app2019.query.FilterOperator;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillServiceImplTest extends EntityServiceImplTest {
    private static int DATA_SIZE = 1000;
    private static List<Bill> bills = new ArrayList<>();
    private static EntityService<Bill> entityService = new EntityServiceImpl<>(new EntityStorage<>());
    private QueryConditionsHolder<Bill> holder;
    
    @BeforeAll
    static void init() {
        for (int i = 0; i < DATA_SIZE; i++) {
            Bill randomBill = TestUtils.getRandomBill(random);
            bills.add(randomBill);
            entityService.putEntity(randomBill);
        }
    }
    
    @BeforeEach
    void initHolder() {
        holder = new QueryConditionsHolder<>();
    }
    
    @Test
    void getFilteredDoesContainListTest() {
        long price = TestUtils.getCustomBillItem().getPrice();
        holder.addFilterCondition(new QueryCondition<>(
                        bill -> bill.containsPrice(price),
                        FilterOperator.CONTAINS
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Bill> filteredList = entityService.getRequestedListOfEntities(new BillQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Bill bill : bills) {
                boolean flag = false;
                for (BillItem billItem : bill.getItems()) {
                    flag = billItem.getPrice() == price;
                }
                if (flag) {
                    assertTrue(filteredList.contains(bill),
                            "the resulting list does not have the bill which has item with price=" + price);
                } else {
                    assertFalse(filteredList.contains(bill),
                            "the resulting list has the bill which has item with price=" + price);
                }
            }
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }
}
