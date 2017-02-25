package Asset;

/**
 * Created by Peter on 2/1/17.
 */
public class Deposit extends Asset {

    private double apr;
    private boolean first = true;

    public Deposit(String code, char type, String label, double apr) {
        super(code, type, label);
        this.apr = apr/100;
    }


    public double getRisk() {
        return 0;
    }

    @Override
    public double getValue(double value) {
        return value;
    }

    public double getReturn(double value) {
        double apy = Math.exp(apr)- 1;
        return value*apy;
    }
}
