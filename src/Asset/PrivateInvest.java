package Asset;

/**
 * Created by Peter on 2/1/17.
 */
public class PrivateInvest extends InvestmentAsset {

    private double omegaMeasure;
    private double totalValue;


    public PrivateInvest(String code, char type, String label, double quarterlyDividend, double baseRateOfReturn, double omegaMeasure, double totalValue) {
        super(code, type, label, quarterlyDividend, baseRateOfReturn);
        this.omegaMeasure = omegaMeasure;
        this.totalValue = totalValue;
    }

    public double getRisk() {
        return omegaMeasure + Math.exp(-100000/totalValue);
    }

    @Override
    public double getValue(double rate) {
        return (totalValue*rate);
    }

    @Override
    public double getReturn(double rate){
        return getValue(rate) * baseRateOfReturn + 4 * quarterlyDividend * rate;
    }

}
