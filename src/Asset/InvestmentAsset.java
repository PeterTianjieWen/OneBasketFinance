package Asset;

/**
 * Created by Peter on 2/1/17.
 */
public abstract class InvestmentAsset extends Asset {

    protected double quarterlyDividend;
    protected double baseRateOfReturn;

    public InvestmentAsset(String code, char type, String label, double quarterlyDividend,
                           double baseRateOfReturn) {
        super(code, type, label);
        this.quarterlyDividend = quarterlyDividend;
        this.baseRateOfReturn = baseRateOfReturn/100;
    }



}

