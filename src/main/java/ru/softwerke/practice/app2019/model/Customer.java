package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.util.DateParam;
import ru.softwerke.practice.app2019.util.StringParam;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Entity {
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String BIRTH_DATE_FIELD = "birthdate";
    
    private static AtomicInteger nextId = new AtomicInteger();
    
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final LocalDate birthDate;
    private final int id;
    
    public static String getName() {
        return "a customer";
    }
    
    @JsonCreator
    public Customer(@NotNull @JsonProperty(value = FIRST_NAME_FIELD) String firstName,
                    @NotNull @JsonProperty(value = LAST_NAME_FIELD) String lastName,
                    @NotNull @JsonProperty(value = MIDDLE_NAME) String middleName,
                    @NotNull @JsonProperty(value = BIRTH_DATE_FIELD) String birthDate) {
        StringParam firstNameParam = new StringParam(firstName, FIRST_NAME_FIELD, Query.POST_ENTITY + getName());
        StringParam lastNameParam = new StringParam(lastName, LAST_NAME_FIELD, Query.POST_ENTITY + getName());
        StringParam middleNameParam = new StringParam(middleName, MIDDLE_NAME, Query.POST_ENTITY + getName());
        DateParam birthDateParam = new DateParam(birthDate, BIRTH_DATE_FIELD, Query.POST_ENTITY + getName());
        this.firstName = firstNameParam.getValue();
        this.lastName = lastNameParam.getValue();
        this.middleName = middleNameParam.getValue();
        this.birthDate = birthDateParam.getDate();
        this.id = nextId.getAndIncrement();
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    @JsonIgnore
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    @JsonProperty(value = BIRTH_DATE_FIELD)
    public String getBirthDateString() {
        return birthDate.format(DateParam.formatter);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(middleName, customer.middleName) &&
                Objects.equals(birthDate, customer.birthDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, middleName, birthDate);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthdate=" + birthDate.toString() +
                '}';
    }
}
