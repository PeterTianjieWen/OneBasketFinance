import Asset.Asset;
import Person.Broker;
import Person.Person;

import java.sql.*;
import java.util.*;

import static Info.DatabaseInfo.*;

/**
 * Created by Peter on 2/11/17.
 */
public class Portfolio {

    private String portfolioCode;
    private String ownerCode;
    private String managerCode;
    private String beneficiaryCode = null;
    private Map<Asset, Double> assetMap = null;

    //use for comparing
    private String lastName;
    private Person owner = null;
    private Person beneficiary = null;
    private Broker manager = null;

    public Portfolio(String portfolioCode, String ownerCode, String managerCode,
                     String beneficiaryCode, Map<String, Double> assetMap, List<Asset> assetList) {
        this.portfolioCode = portfolioCode;
        this.ownerCode = ownerCode;
        this.managerCode = managerCode;
        this.beneficiaryCode = beneficiaryCode;
        this.assetMap = getNewAssetMap(assetMap, assetList);
    }

    public static List<Portfolio> getPortfolioList(List<Asset> assetList){
        List<Portfolio> p = new ArrayList<>();

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Instantiation fail");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Class not found");
        }


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT p.personId, managerId, beneficiaryId, a.assetCode, byHowMuch, m.portfolioCode FROM Portfolio p " +
                "JOIN Asset a on a.assetId = p.assetId JOIN Person m on m.personId = p.personId ORDER BY personId";

        try{
            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            int lastId = -10;
            int personId ;
            int managerId;
            int beneficiaryId;
	        String ownerCode = null;
	        String managerCode = null;
	        String beneficiaryCode = null;
	        String portfolioCode = null;
	        boolean first = true;
	        Map<String, Double> assetMap = new HashMap<String, Double>();
            while(rs.next()){
	            personId = rs.getInt("p.personId");
	            if (first){
	            	lastId = personId;
	            	first = false;
	            }
	            if (personId != lastId){
	            	Portfolio tmp = new Portfolio(portfolioCode, ownerCode, managerCode, beneficiaryCode, assetMap, assetList);
	            	p.add(tmp);
	            	assetMap = new HashMap<>();
	            	lastId = personId;
	            }
                managerId = rs.getInt("p.managerId");
                beneficiaryId = rs.getInt("p.beneficiaryId");
                ownerCode = Person.getPersonCode(personId);
                managerCode = Person.getPersonCode(managerId);
                beneficiaryCode = null;
                if (beneficiaryId != 0){
                    beneficiaryCode = Person.getPersonCode(beneficiaryId);
                }
                portfolioCode = rs.getString("m.portfolioCode");
	            double value = rs.getDouble("p.byHowMuch");
                String assetCode = rs.getString("a.assetCode");
                //System.out.println(value+" "+assetCode);


	            assetMap.put(assetCode, value);



	            lastId = personId;
            }
        }catch(SQLException e){
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
            System.out.println("SQLException: fail in portfolio parse");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return p;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Broker getManager() {
        return manager;
    }

    public void setManager(Broker manager) {
        this.manager = manager;
    }

    public Map<Asset, Double> getAssetMap() {
        return assetMap;
    }

    public String getPortfolioCode() {
        return portfolioCode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public String getManagerCode() {
        return managerCode;
    }

    public String getBeneficiaryCode() {
        return beneficiaryCode;
    }

    //convert the
    public static Map<Asset, Double> getNewAssetMap(Map<String, Double> oldMap, List<Asset> assetList) {
        Map<Asset, Double> temp = new HashMap<>();
        for (Asset a: assetList){
            for (Map.Entry<String, Double> m : oldMap.entrySet()) {
                if (a.getCode().equals(m.getKey())){
                    temp.put(a, m.getValue());
                }
            }
        }
        return temp;
    }


    public static void main(String[] args){
        List<Portfolio> forTest = getPortfolioList(Asset.getAssetList());
        for (Portfolio p : forTest){
            System.out.println(p.getManagerCode());
        }
    }
}
