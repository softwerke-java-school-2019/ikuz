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
import java.util.concurrent.atomic.AtomicLong;

public class Customer implements Entity {
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String PATRONYMIC_FIELD = "patronymic";
    private static final String BIRTH_DATE_FIELD = "birthDate";
    
    private static AtomicLong nextId = new AtomicLong();
    
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String patronymic;
    private final LocalDate birthDate;
    
    public static String getName() {
        return "customer";
    }
    
    @JsonCreator
    public Customer(@NotNull @JsonProperty(value = FIRST_NAME_FIELD, required = true) String firstName,
                    @NotNull @JsonProperty(value = LAST_NAME_FIELD, required = true) String lastName,
                    @NotNull @JsonProperty(value = PATRONYMIC_FIELD, required = true) String patronymic,
                    @NotNull @JsonProperty(value = BIRTH_DATE_FIELD, required = true) String birthDate) {
        StringParam firstNameParam = new StringParam(firstName, FIRST_NAME_FIELD, Query.ADD_ENTITY + getName());
        StringParam lastNameParam = new StringParam(lastName, LAST_NAME_FIELD, Query.ADD_ENTITY + getName());
        StringParam patronymicParam = new StringParam(patronymic, PATRONYMIC_FIELD, Query.ADD_ENTITY + getName());
        DateParam birthDateParam = new DateParam(birthDate, Query.ADD_ENTITY + getName());
        this.firstName = firstNameParam.getValue();
        this.lastName = lastNameParam.getValue();
        this.patronymic = patronymicParam.getValue();
        this.birthDate = birthDateParam.getDate();
        
        this.id = nextId.getAndIncrement();
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPatronymic() {
        return patronymic;
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
                Objects.equals(patronymic, customer.patronymic) &&
                Objects.equals(birthDate, customer.birthDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, birthDate);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthDate=" + birthDate.toString() +
                '}';
    }
}
