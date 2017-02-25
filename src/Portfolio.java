import Asset.Asset;
import Person.Person;
import Person.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peter on 2/11/17.
 */
public class Portfolio {

    private String portfolioCode;
    private String ownerCode;
    private String managerCode;
    private String beneficiaryCode = null;
    Map<Asset, Double> assetMap = null;
    //use for building a comparator
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
}
