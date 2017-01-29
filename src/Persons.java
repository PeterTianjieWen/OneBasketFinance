/**
 * Created by Peter on 1/29/17.
 */
public class Persons {

    private String personalCode;
    private String brokerData[] = new String[2]; //First for E/J, Second for SEC indentifier
    //name
    private String firstName;
    private String lastName;
    //address
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    private String email;

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public void setBrokerData(String[] brokerData) {
        this.brokerData = brokerData;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
