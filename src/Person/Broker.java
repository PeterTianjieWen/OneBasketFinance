package Person;

import java.util.List;

/**
 * Created by Peter on 2/1/17.
 */

public class Broker extends Person {

    private char level;
    private String secNum;

    public Broker(String personCode, char level, String secNum, String firstName,
                  String lastName, List<String> email,
                  Address address) {
        super(personCode, firstName, lastName, email, address);
        this.level = level;
        this.secNum = secNum;
    }
    //getter
    public char getLevel() {
        return level;
    }

    public double getFee(double assetAmount){
        double fee = 0;
        if (level == 'E'){
            fee = assetAmount * 10;
            return fee;
        }else if (level == 'J'){
            fee = assetAmount * 50;
        }
        return fee;
    }

    public double getCommission(double perTotalReturn){
        double commission = 0;
        if (level == 'E'){
            commission = 0.05 * perTotalReturn;
        }else if (level == 'J') {
            commission = 0.02 * perTotalReturn;
        }
        return commission;
    }

    public String getSecNum() {
        return secNum;
    }

}
