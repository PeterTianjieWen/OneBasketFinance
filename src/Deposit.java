/**
 * Created by Peter on 2/1/17.
 */
public class Deposit extends Asset {

    private double apr;

    public Deposit(String code, char type, String label, double apr) {
        super(code, type, label);
        this.apr = apr;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }
}
