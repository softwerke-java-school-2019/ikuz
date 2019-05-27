package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.StringParam;

import javax.ws.rs.WebApplicationException;
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
    private final long id;
    
    public static final String ENTITY_TYPE_NAME = "customer";
    
    @JsonCreator
    public Customer(@JsonProperty(value = FIRST_NAME_FIELD, required = true) String firstName,
                    @JsonProperty(value = LAST_NAME_FIELD, required = true) String lastName,
                    @JsonProperty(value = MIDDLE_NAME, required = true) String middleName,
                    @JsonProperty(value = BIRTH_DATE_FIELD, required = true) String birthDate)
            throws WebApplicationException {
        StringParam firstNameParam = new StringParam(
                firstName,
                FIRST_NAME_FIELD
        );
        StringParam lastNameParam = new StringParam(
                lastName,
                LAST_NAME_FIELD
        );
        StringParam middleNameParam = new StringParam(
                middleName,
                MIDDLE_NAME
        );
        ParseFromStringParam<LocalDate> birthDateParam = new ParseFromStringParam<>(
                birthDate,
                BIRTH_DATE_FIELD,
                ParseFromStringParam.PARSE_DATE_FUN,
                ParseFromStringParam.DATE_FORMAT
        );
        
        this.firstName = firstNameParam.getValue();
        this.lastName = lastNameParam.getValue();
        this.middleName = middleNameParam.getValue();
        this.birthDate = birthDateParam.getParsedValue();
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
    
    public String getMiddleName() {
        return middleName;
    }
    
    @JsonIgnore
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    @JsonProperty(value = BIRTH_DATE_FIELD)
    public String getBirthDateString() {
        return birthDate.format(ParseFromStringParam.dateFormatter);
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
        return Objects.hash(firstName, lastName, middleName, birthDate, id);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", id=" + id +
                '}';
    }
}
