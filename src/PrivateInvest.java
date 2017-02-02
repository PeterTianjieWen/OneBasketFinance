/**
 * Created by Peter on 2/1/17.
 */
public class PrivateInvest extends InvestmentAsset{

    private double omegaMeasure;
    private double totalvalue;

    public PrivateInvest(String code, char type, String label, double quarterlyDividend,
                         double baseRateOfReturn, double omegaMeasure, double totalvalue) {
        super(code, type, label, quarterlyDividend, baseRateOfReturn);
        this.omegaMeasure = omegaMeasure;
        this.totalvalue = totalvalue;
    }

    public double getOmegaMeasure() {
        return omegaMeasure;
    }

    public void setOmegaMeasure(double omegaMeasure) {
        this.omegaMeasure = omegaMeasure;
    }

    public double getTotalvalue() {
        return totalvalue;
    }

    public void setTotalvalue(double totalvalue) {
        this.totalvalue = totalvalue;
    }
}
