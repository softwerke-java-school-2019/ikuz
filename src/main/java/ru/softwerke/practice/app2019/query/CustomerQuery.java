package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.util.DateParam;
import ru.softwerke.practice.app2019.util.StringParam;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CustomerQuery extends Query<Customer> {
    private static Map<String, Comparator<Customer>> orderParamsMap = new HashMap<>();
    
    private static final String ORDER_BIRTH_DATE_TO = "-birthdate";
    
    public static final String FIRST_NAME = "firstName";
    public static final String FIRST_NAME_PREFIX = "firstNamePrefix";
    public static final String LAST_NAME = "lastName";
    public static final String LAST_NAME_PREFIX = "lastNamePrefix";
    public static final String MIDDLE_NAME = "middleName";
    public static final String MIDDLE_NAME_PREFIX = "middleNamePrefix";
    public static final String BIRTH_DATE = "birthdate";
    
    public static final String BIRTH_DATE_FROM = "birthdateFrom";
    public static final String BIRTH_DATE_TO = "birthdateTo";
    
    static {
        orderParamsMap.put(FIRST_NAME, Comparator.comparing(Customer::getFirstName));
        orderParamsMap.put(LAST_NAME, Comparator.comparing(Customer::getLastName));
        orderParamsMap.put(MIDDLE_NAME, Comparator.comparing(Customer::getMiddleName));
        orderParamsMap.put(BIRTH_DATE, Comparator.comparing(Customer::getBirthDate));
        orderParamsMap.put(ORDER_BIRTH_DATE_TO, Comparator.comparing(Customer::getBirthDate).reversed());
    }
    
    public static Map<String, Comparator<Customer>> getOrderParamsMap() {
        return orderParamsMap;
    }
    
    private CustomerQuery() {
        super();
    }
    
    public static class Builder extends Query.Builder<Customer> {
        
        public Builder() {
            queryToBuild = new CustomerQuery();
        }
        
        public Query<Customer> build() {
            Query<Customer> builtCustomerQuery = queryToBuild;
            queryToBuild = new CustomerQuery();
            
            return builtCustomerQuery;
        }
        
        public CustomerQuery.Builder setFirstName(StringParam firstName) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getFirstName, FilterOperator.EQ,
                    firstName.getValue()));
            return this;
        }
        
        public CustomerQuery.Builder setLastName(StringParam lastName) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getLastName, FilterOperator.EQ,
                    lastName.getValue()));
            return this;
        }
        
        public CustomerQuery.Builder setMiddleName(StringParam middleName) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getMiddleName, FilterOperator.EQ,
                    middleName.getValue()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDate(DateParam birthDate) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getBirthDate, FilterOperator.EQ,
                    birthDate.getDate()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDateFrom(DateParam birthDateFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getBirthDate, FilterOperator.GREATER_OR_EQ,
                    birthDateFrom.getDate()));
            return this;
        }
        
        public CustomerQuery.Builder setBirthDateTo(DateParam birthDateTo) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getBirthDate, FilterOperator.LESS_OR_EQ,
                    birthDateTo.getDate()));
            return this;
        }
        
        public Builder setFirstNamePrefix(StringParam firstNamePrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getFirstName, firstNamePrefix.getValue()));
            return this;
        }
        
        public Builder setMiddleNamePrefix(StringParam middleNamePrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getMiddleName, middleNamePrefix.getValue()));
            return this;
        }
        
        public Builder setLastNamePrefix(StringParam lastNamePrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Customer::getLastName, lastNamePrefix.getValue()));
            return this;
        }
    }
}
