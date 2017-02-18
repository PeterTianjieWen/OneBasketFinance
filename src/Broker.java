import java.util.List;

/**
 * Created by Peter on 2/1/17.
 */
public class Broker extends Person {

    private char level;
    private String secNum;

    public Broker(String personCode, char level, String secNum, String firstName, String lastName, List<String> email,
                  Address address) {
        super(personCode, firstName, lastName, email, address);
        this.level = level;
        this.secNum = secNum;
    }
    //getter
    public char getLevel() {
        return level;
    }

    public String getSecNum() {
        return secNum;
    }

}
