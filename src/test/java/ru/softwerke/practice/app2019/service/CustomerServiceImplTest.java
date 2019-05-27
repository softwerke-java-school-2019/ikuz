package ru.softwerke.practice.app2019.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.testutil.TestUtils;
import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.query.CustomerQuery;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.storage.EntityStorage;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest extends EntityServiceImplTest {
    private static int DATA_SIZE = 1000;
    private static List<Customer> customers = new ArrayList<>();
    private static EntityService<Customer> entityService = new EntityServiceImpl<>(new EntityStorage<>());
    private QueryConditionsHolder<Customer> holder;
    
    @BeforeAll
    static void init() {
        for (int i = 0; i < DATA_SIZE; i++) {
            Customer randomCustomer = TestUtils.getRandomCustomer(random);
            customers.add(randomCustomer);
            entityService.putEntity(randomCustomer);
        }
    }
    
    @BeforeEach
    void initHolder() {
        holder = new QueryConditionsHolder<>();
    }
    
    @Test
    void getFilteredWithPrefixListTest() {
        String prefix = "пЕт";
        holder.addFilterCondition(new QueryCondition<>(
                        Customer::getFirstName,
                        prefix
                )
        );
        holder.setCountOfElementsParam(Integer.MAX_VALUE);
        try {
            List<Customer> filteredList = entityService.getRequestedListOfEntities(new CustomerQuery(holder));
            assertFalse(filteredList.isEmpty(), "Expected WebApplicationException did not occur");
            for (Customer customer : customers) {
                if (customer.getFirstName().startsWith("Пет")) {
                    assertTrue(filteredList.contains(customer),
                            "the resulting list does not have the customer which starts with considered prefix 'Пет'");
                } else {
                    assertFalse(filteredList.contains(customer),
                            "the resulting list has the customer which starts with considered prefix 'Пет'");
                }
            }
        } catch (WebApplicationException e) {
            fail("That should be valid filtering but WebApplication exception occur");
        }
    }
}
