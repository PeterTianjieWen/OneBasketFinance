import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 1/29/17.
 */
public class Persons {


    private String personCode;
    private String firstName;
    private String lastName;
    Address address = null;
    private List<String> email = new ArrayList<String>();


    public Persons(String personCode, String firstName, String lastName, List<String> email, Address address) {
        this.personCode = personCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    //SETTER BELOW


    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    //GETTER BELOW
    public Address getAddress() {
        return address;
    }

    public List<String> getEmail() {
        return email;
    }

    public String getPersonCode() {
        return personCode;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

//    public String getName(){
//        return lastName + ", " + firstName;
//    }


}
