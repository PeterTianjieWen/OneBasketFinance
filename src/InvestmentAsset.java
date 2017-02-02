/**
 * Created by Peter on 2/1/17.
 */
public class InvestmentAsset extends Asset {

    private double quarterlyDividend;
    private double baseRateOfReturn;


    public InvestmentAsset(String code, char type, String label,
                           double quarterlyDividend, double baseRateOfReturn) {
        super(code, type, label);
        this.quarterlyDividend = quarterlyDividend;
        this.baseRateOfReturn = baseRateOfReturn;
    }

    public double getQuarterlyDividend() {
        return quarterlyDividend;
    }

    public void setQuarterlyDividend(double quarterlyDividend) {
        this.quarterlyDividend = quarterlyDividend;
    }

    public double getBaseRateOfReturn() {
        return baseRateOfReturn;
    }

    public void setBaseRateOfReturn(double baseRateOfReturn) {
        this.baseRateOfReturn = baseRateOfReturn;
    }


}
