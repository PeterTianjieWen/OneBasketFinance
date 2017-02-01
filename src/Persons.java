/**
 * Created by Peter on 1/29/17.
 */
public class Persons {

    private String personCode;
    private class Broker{
        char level;
        String secNum;
    }
    private Broker broker = new Broker();
    //name
    private String firstName;
    private String lastName;
    //address
    private String address;

    private String email;

    public String getPersonCode() {
        return personCode;
    }

    public String getName(){
        return lastName + ", " + firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Broker getBroker() {
        return broker;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Persons(String personCode, Broker broker, String firstName, String lastName, String address, String email) {

        this.personCode = personCode;
        this.broker = broker;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
    }
}
