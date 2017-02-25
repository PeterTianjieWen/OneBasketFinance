package Asset;

/**
 * Created by Peter on 2/1/17.
 */
public abstract class Asset{

    private String code;
    private char type;
    private String label;

    public Asset(String code, char type, String label) {
        this.code = code;
        this.type = type;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public char getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public abstract double getRisk();

    public abstract double getReturn(double value);

    public abstract double getValue(double value);
}
