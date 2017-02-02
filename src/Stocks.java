/**
 * Created by Peter on 2/1/17.
 */
public class Stocks extends InvestmentAsset {

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

    public double getBetaMeasure() {
        return betaMeasure;
    }

    public void setBetaMeasure(double betaMeasure) {
        this.betaMeasure = betaMeasure;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }
}
