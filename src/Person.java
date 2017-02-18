import java.util.*;

/**
 * Created by Peter on 1/29/17.
 */
public abstract class Person {

    private String personCode;
    private String firstName;
    private String lastName;
    private Address address = null;
    private List<String> email = new ArrayList<String>();
    private Map<Asset, Double> assetMap = new HashMap<>();
    private boolean first = true;


    public void setAssetMap(Map<Asset, Double> assetMap) {
        if (first){
            this.assetMap = assetMap;
        }else{
            throw new RuntimeException("2nd Setting!");
        }
    }

    //get total value
    public double getValue(){
        double sum = 0;
        for (Map.Entry<Asset, Double> entry : assetMap.entrySet()) {
            sum += entry.getKey().getValue(entry.getValue());
        }
        return sum;
    }

    //2 constructor
    public Person(String personCode, String firstName, String lastName, List<String> email, Address address) {
        this.personCode = personCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
    public Person(){
        this.personCode = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.address = null;
    }

    public Map<Asset, Double> getAssetMap() {
        return assetMap;
    }

    /*
       getter
    */
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

    public String getName(){
        return lastName + ", " + firstName;
    }




}
