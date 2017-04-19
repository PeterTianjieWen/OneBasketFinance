import Asset.Asset;
import Person.Broker;
import Person.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Info.DatabaseInfo.*;

/**
 * Created by Peter on 2/11/17.
 */
public class Portfolio implements Comparable<Portfolio> {

    private String portfolioCode;
    private String ownerCode;
    private String managerCode;
    private String beneficiaryCode = null;
    private Map<Asset, Double> assetMap = null;
    private double totalValue = 0;

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    //use for comparing
    private String lastName;
    private Person owner = null;
    private Person beneficiary = null;
    private Broker manager = null;

    private Portfolio(String portfolioCode, String ownerCode, String managerCode,
                         String beneficiaryCode){
        this.portfolioCode = portfolioCode;
        this.ownerCode = ownerCode;
        this.managerCode = managerCode;
        this.beneficiaryCode = beneficiaryCode;
    }

    public Portfolio(String portfolioCode, String ownerCode, String managerCode,
                     String beneficiaryCode, Map<String, Double> assetMap, List<Asset> assetList) {
        this.portfolioCode = portfolioCode;
        this.ownerCode = ownerCode;
        this.managerCode = managerCode;
        this.beneficiaryCode = beneficiaryCode;
        this.assetMap = getNewAssetMap(assetMap, assetList);
    }

    public double getTotalValue() {
        return totalValue;
    }

    public Person getBeneficiary() {
        return beneficiary;
    }

    public static MyList<Portfolio> getPortfolioList(List<Asset> assetList){
        MyList<Portfolio> p = new MyList<>();

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
        String query = "SELECT p.personId, managerId, beneficiaryId, p.portfolioCode FROM Portfolio p " +
                "JOIN Person m on m.personId = p.personId " +
                "ORDER BY personId";

        try{
            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

	        List<Portfolio> portfolioList = new ArrayList<>();
            while(rs.next()) {
                int personId ;
                int managerId;
                int beneficiaryId;
                String ownerCode = null;
                String managerCode = null;
                String beneficiaryCode = null;
                String portfolioCode = null;
                personId = rs.getInt("p.personId");
                managerId = rs.getInt("p.managerId");
                beneficiaryId = rs.getInt("p.beneficiaryId");
                //things that we need
                ownerCode = Person.getPersonCode(personId);
                Person owner = Retrieve.getPerson(ownerCode);
                managerCode = Person.getPersonCode(managerId);
                Person manager = Retrieve.getPerson(managerCode);
                beneficiaryCode = null;
                Person beneficiary = null;
                if (beneficiaryId != 0) {
                    beneficiaryCode = Person.getPersonCode(beneficiaryId);
                   beneficiary = Retrieve.getPerson(beneficiaryCode);
                }
                portfolioCode = rs.getString("p.portfolioCode");
                Portfolio tmp = new Portfolio(portfolioCode, ownerCode,managerCode,
                        beneficiaryCode);
                tmp.setOwner(owner);
                tmp.setManager((Broker)manager);
                if (beneficiary != null){
                    tmp.setBeneficiary(beneficiary);
                }

                portfolioList.add(tmp);
            }
            rs.close();

            for (Portfolio portfolio : portfolioList) {
                Map<String, Double> assetMap = new HashMap<String, Double>();
                query = "SELECT al.assetValue, assetCode, p.portfolioCode FROM AssetList al " +
                        "JOIN Asset a on al.assetId = a.assetId " +
                        "JOIN Portfolio p on al.portfolioId = p.portfolioId " +
                        "WHERE p.portfolioCode = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1,portfolio.portfolioCode);
                rs = ps.executeQuery();
                while(rs.next()){
                    assetMap.put(rs.getString("assetCode"), rs.getDouble("assetValue"));
                }
                portfolio.setAssetMap(getNewAssetMap(assetMap, assetList));
                p.add(portfolio);
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

    public void setBeneficiary(Person beneficiary) {
        this.beneficiary = beneficiary;
    }

    private void setAssetMap(Map<Asset, Double> assetMap) {
        this.assetMap = assetMap;
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
        MyList<Portfolio> forTest = getPortfolioList(Asset.getAssetList());
        for (Portfolio p : forTest){
            System.out.println(p.getManagerCode());
        }
    }

    @Override
    public int compareTo(Portfolio o) {
        return owner.getLastName().compareTo(o.getOwner().getLastName());
    }
}
