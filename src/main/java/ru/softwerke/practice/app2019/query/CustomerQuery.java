package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.util.ColorParam;
import ru.softwerke.practice.app2019.util.DateParam;
import ru.softwerke.practice.app2019.util.PriceParam;
import ru.softwerke.practice.app2019.util.StringParam;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CustomerQuery extends Query<Customer>{
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PATRONYMIC = "patronymic";
    public static final String BIRTH_DATE = "birthDate";;
    public static final String BIRTH_DATE_FROM = "birthDateFrom";
    public static final String BIRTH_DATE_TO = "birthDateTo";
    
    private static Map<String, Comparator<Customer>> orderParamsMap = new HashMap<>();
    
    static {
        orderParamsMap.put(FIRST_NAME, Comparator.comparing(Customer::getFirstName));
        orderParamsMap.put(LAST_NAME, Comparator.comparing(Customer::getLastName));
        orderParamsMap.put(PATRONYMIC, Comparator.comparing(Customer::getPatronymic));
        orderParamsMap.put(BIRTH_DATE, Comparator.comparing(Customer::getBirthDate));
    }
    
    public static Map<String, Comparator<Customer>> getOrderParamsMap() {
        return orderParamsMap;
    }
    
    private CustomerQuery() {
        super();
    }
    
    public static class Builder extends Query.Builder<Customer> {
        private CustomerQuery customerQueryToBuild;
        
        public Builder() {
            customerQueryToBuild = new CustomerQuery();
        }
        
        public CustomerQuery.Builder setFirstName(StringParam firstName) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getFirstName, FilterOperator.EQ),
                    firstName.getValue()));
            return this;
        }
    
        public CustomerQuery.Builder setLastName(StringParam lastName) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getLastName, FilterOperator.EQ),
                    lastName.getValue()));
            return this;
        }
    
        public CustomerQuery.Builder setPatronymic(StringParam patronymic) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getPatronymic, FilterOperator.EQ),
                    patronymic.getValue()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDate(DateParam birthDate) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getBirthDate, FilterOperator.EQ),
                    birthDate.getDate()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDateFrom(DateParam birthDateFrom) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getBirthDate, FilterOperator.GREATER_OR_EQ),
                    birthDateFrom.getDate()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDateTo(DateParam birthDateTo) {
            this.customerQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Customer::getBirthDate, FilterOperator.LESS_OR_EQ),
                    birthDateTo.getDate()));
            return this;
        }
    
        public Query.Builder setCustomerComparator(Comparator<Customer> comparator) {
            this.customerQueryToBuild.holder.setQueryComparator(comparator);
            return this;
        }
        
        public CustomerQuery build() {
            CustomerQuery builtCustomerQuery = customerQueryToBuild;
            customerQueryToBuild = new CustomerQuery();
            
            return builtCustomerQuery;
        }
    }
}
