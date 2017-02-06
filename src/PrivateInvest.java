/**
 * Created by Peter on 2/1/17.
 */
public class PrivateInvest extends InvestmentAsset{

    private double omegaMeasure;
    private double totalValue;


    public PrivateInvest(String code, char type, String label, double quarterlyDividend, double baseRateOfReturn, double omegaMeasure, double totalValue) {
        super(code, type, label, quarterlyDividend, baseRateOfReturn);
        this.omegaMeasure = omegaMeasure;
        this.totalValue = totalValue;
    }


    public double getOmegaMeasure() {
        return omegaMeasure;
    }

    public void setOmegaMeasure(double omegaMeasure) {
        this.omegaMeasure = omegaMeasure;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
