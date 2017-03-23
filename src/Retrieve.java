import Asset.Asset;
import Person.Person;

import java.util.List;

/**
 * Created by Peter on 2/11/17.
 */
public final class Retrieve {

    private static List<Person> personL;
    private static List<Asset> assetL;


    public Retrieve(List<Person> personList, List<Asset> assetList) {

        personL = personList;
        assetL = assetList;
    }

    public static Person getPerson(String code) {
        Person p = null;
        for (Person tmp: personL){
            if (tmp.getPersonCode().startsWith(code)){ //assume no error at the beginning
                p = tmp;
                return p;
            }
        }
        //not found
        return null;
    }


    public List<Asset> getAssetList(){
        return assetL;
    }

    public List<Person> getPersonList(){
        return personL;
    }

    public static Asset getAsset(String assetCode) {
        Asset a = null;
        for (Asset tmp :
                assetL) {
            if (tmp.getCode().equals(assetCode)) {
                a = tmp;
                break;
            }
        }

        return a;
    }

}
