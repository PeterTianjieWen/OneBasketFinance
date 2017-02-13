/**
 * Created by Peter on 2/1/17.
 */
public abstract class Asset implements ReturnInterface{

    private String code;
    private char type;
    private String label;
    double reTurn;

    public Asset(String code, char type, String label) {
        this.code = code;
        this.type = type;
        this.label = label;
    }

    public abstract double getRisk();

    public abstract double getReturn(double value);

    public abstract double getValue();

    public String getCode() {
        return code;
    }

    public char getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }
}
