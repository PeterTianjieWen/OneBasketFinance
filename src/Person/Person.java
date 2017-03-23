package Person;

import Asset.Asset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Info.DatabaseInfo.password;
import static Info.DatabaseInfo.url;
import static Info.DatabaseInfo.username;

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

    public Person(String personCode, String firstName, String lastName,
                  List<String> email,Address address) {
        this.personCode = personCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
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

    public static List<Person> getPersonList() {

        ArrayList<Person> personList = new ArrayList<Person>();



        Boolean isBroker = false;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Illegal Access");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Instantiation failed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Class not found");
        }

        String getPersonQuery = "SELECT a.street, a.city, a.state, a.zip, a.country," +
                "p.personCode, p.firstName, p.lastName,b.secNum, b.level from Person p " +
                "join Address a on a.addressId = p.addressId " +
                "left join Broker b on b.personId = p.personId";
        String getEmailQuery = "SELECT e.email FROM Email e join Person p on p.personId = e.personId " +
                "where p.personCode = ?";
        PreparedStatement ps = null;
        PreparedStatement psForEmail = null;
        ResultSet rs = null;
        ResultSet rsForEmail = null;
        Connection conn = null;



        try{

            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement(getPersonQuery);
            rs = ps.executeQuery();
            while (rs.next()){
                String street = rs.getString("a.street");
                String city = rs.getString("a.city");
                String state = rs.getString("a.state");
                String country = rs.getString("a.country");
                String zip = rs.getString("a.zip");
                Address address = new Address(street, city, state, country, zip);
                String personCode = rs.getString("p.personCode");
                String firstName = rs.getString("p.firstName");
                String lastName = rs.getString("p.lastName");
                String secNum = rs.getString("b.secNum");
                isBroker = secNum != null;
                char level = ' ';
                if (isBroker){
                    level = (rs.getString("b.level")).toCharArray()[0];
                }

                List<String> emailList = new ArrayList<>();
                psForEmail = conn.prepareStatement(getEmailQuery);
                psForEmail.setString(1,personCode);
                rsForEmail = psForEmail.executeQuery();
                while (rsForEmail.next()){
                    emailList.add(rsForEmail.getString("email"));
                }
                if (isBroker) {
                    //Person.Broker operation
                    Broker broker = new Broker(personCode, level, secNum, firstName, lastName, emailList, address);
                    personList.add(broker);

                } else {
                    //Person.Customer operation
                    Customer customer = new Customer(personCode, firstName, lastName, emailList, address);
                    personList.add(customer);
                }

            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


        try {
            if(rsForEmail != null && !rsForEmail.isClosed())
                rsForEmail.close();
            if(psForEmail != null && !psForEmail.isClosed())
                psForEmail.close();
            if(rs != null && !rs.isClosed())
                rs.close();
            if(ps != null && !ps.isClosed())
                ps.close();
            if(conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personList;
    }

    public static String getPersonCode(int personId){
        String personCode = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT personCode FROM Person where personId = ?";
        try{
            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement(query);
            ps.setInt(1,personId);
            rs = ps.executeQuery();
            if (rs.next()){
                personCode = rs.getString("personCode");
            }else{
                throw new RuntimeException("No record\n");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(rs != null && !rs.isClosed())
                rs.close();
            if(ps != null && !ps.isClosed())
                ps.close();
            if(conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personCode;
    }

    public static void main(String[] args){

        List<Person> forTest = Person.getPersonList();
        for (Person p : forTest){
            System.out.println(p.getName());
        }

    }

}
