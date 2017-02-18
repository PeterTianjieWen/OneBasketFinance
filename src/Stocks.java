/**
 * Created by Peter on 2/1/17.
 */
public class Stocks extends InvestmentAsset{

    private double betaMeasure;
    private String stockSymbol;
    private double sharePrice;

    public Stocks(String code, char type, String label, double quarterlyDividend, double baseRateOfReturn,
                  double betaMeasure, String stockSymbol, double sharePrice) {
        super(code, type, label, quarterlyDividend, baseRateOfReturn);
        this.betaMeasure = betaMeasure;
        this.stockSymbol = stockSymbol;
        this.sharePrice = sharePrice;
    }

    public double getRisk() {
        return betaMeasure;
    }

    public double getValue(double value) {
        return sharePrice*value;
    }

    @Override
    public double getReturn(double rate){
        return baseRateOfReturn * getValue(rate) + 4 * rate * quarterlyDividend;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getSharePrice() {
        return sharePrice;
    }
}
