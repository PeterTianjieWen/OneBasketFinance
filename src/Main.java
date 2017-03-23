import Asset.Asset;
import Person.Broker;
import Person.Person;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Peter.
 */
public class Main {

    public static void main(String argv[]) {
        List<Person> p = Person.getPersonList();
        List<Asset> a = Asset.getAssetList();
        Retrieve retrieve = new Retrieve(p, a);
        List<Portfolio> portfolioList = Portfolio.getPortfolioList(a);
//        System.out.println(portfolioList);
        sortPortfolioList(portfolioList, p);
        printPortfolioSummary(portfolioList, retrieve);
        printPortfolioDetail(portfolioList, retrieve);

    }


    private static void printPortfolioSummary(List<Portfolio> portfolioList, Retrieve retrieve) {
            System.out.println("Portfolio Summary Report");
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < 137; i++) {
                b.append('=');
            }
            System.out.println(b.toString());//new line
            System.out.printf("%-10s %-20s %-20s %16s %16s %16s %16s %16s\n",
                    "Portfolio", "Owner", "Manager", "Fees", "Commissions",
                    "Weighted Risk", "Return", "Total");

        /*-------------------------------------------------------*/
            double totalReturn = 0;//all portfolio return
            double total = 0;//all portfolio value
            double totalFee = 0;
            double totalCommission = 0;
            //loop over every portfolio
            for (Portfolio p : portfolioList) {
                //Portfolio code
                String code = p.getPortfolioCode();
                //name
                Person owner = retrieve.getPerson(p.getOwnerCode());;
                String ownerName = owner.getName();

                Broker manager = (Broker) retrieve.getPerson(p.getManagerCode());
                String managerName = manager.getName();

                if (p.getAssetMap() != null) {
                    //total and return and fee and commission
                    owner.setAssetMap(p.getAssetMap());     //set the assetMap of individual
                    double perTotal = owner.getValue();      //per portfolio value
                    total += perTotal;

                    double perTotalReturn = 0;//per portfolio return
                    double risk = 0.0;
                    for (Map.Entry<Asset, Double> e : owner.getAssetMap().entrySet()) {
                        Asset tmpAsset = e.getKey();
                        Double tmpValue = e.getValue();
                        //total return of this owner
                        perTotalReturn += tmpAsset.getReturn(e.getValue());
                        //risk
                        risk += (tmpAsset.getRisk() * tmpAsset.getValue(tmpValue))/perTotal;
                    }

                    int assetAmount = p.getAssetMap().size();
                    double fee = manager.getFee(assetAmount);
                    double commission = manager.getCommission(perTotalReturn);
                    totalFee += fee;
                    totalCommission += commission;

                    System.out.printf("%-10s %-20s %-20s $%15.2f $%15.2f %16.4f $%15.2f $%15.2f\n",
                            code, ownerName, managerName, fee, commission, risk, perTotalReturn, perTotal);

                    totalReturn += perTotalReturn;
                } else {
                    double zero = 0.0;
                    System.out.printf("%-10s %-20s %-20s $%15.2f $%15.2f %16.4f $%15.2f $%15.2f\n",
                            code, ownerName, managerName, zero,zero,zero,zero,zero);
                }
                //with asset map
                p.setOwner(owner);
                p.setManager(manager);
            }
            b.setLength(0);
            System.out.print(b.toString());
            for (int i = 0; i < 137; i++) {
                if (i < 53){
                    b.append(' ');
                }else{
                    b.append('-');
                }
            }
            System.out.println(b.toString());
            System.out.printf("%-10s %-20s %20s $%15.2f $%15.2f %16s $%15.2f $%15.2f\n",
                    " ", " ", "Total",totalFee, totalCommission, " ", totalReturn, total);
            System.out.print("\n\n\n");


    }

    private static void printPortfolioDetail(List<Portfolio> portfolioList, Retrieve retrieve){

        System.out.println("Portfolio Details");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 130; i++) {
            builder.append('=');
        }
        System.out.println(builder.toString());
        builder.setLength(0);

        for (int i = 0; i < 67; i++) {
            builder.append('-');
        }
        String latter = builder.toString();
        builder.setLength(46);
        String former = builder.toString();
        for (Portfolio p: portfolioList){
            System.out.printf("Portfolio %5s\n", p.getPortfolioCode());
            System.out.println(former);
            System.out.printf("%-20s%-20s\n", "Owner", p.getOwner().getName());
            System.out.printf("%-20s%-20s\n", "Manager", p.getManager().getName());
            String beneficiaryCode = p.getBeneficiaryCode();
            if (beneficiaryCode == null){
                System.out.printf("%-20s%-20s\n", "Beneficiary", "None");
            }else{
                Person tmp = retrieve.getPerson(beneficiaryCode);
                System.out.printf("%-20s%-20s\n", "Beneficiary", tmp.getName());
            }
            System.out.println("Assets");
            System.out.printf("%-15s %-40s %15s %15s %20s %20s\n","code", "Asset", "Return Rate",
                    "Risk", "Annual Return", "Value");
            double total = 0;
            double returnTotal = 0;
            double aggregateRisk = 0;
            if (p.getAssetMap() != null) {
                Map<Asset, Double> assetMap = p.getOwner().getAssetMap();
                for (Map.Entry<Asset, Double> a : assetMap.entrySet()) {
                    Asset tmp = a.getKey();
                    String aCode = tmp.getCode();
                    String label = tmp.getLabel();
                    double value = tmp.getValue(a.getValue());
                    double annualReturn = tmp.getReturn(a.getValue());
                    double risk = tmp.getRisk();
                    double returnRate = annualReturn / value;
                    System.out.printf("%-15s %-40s %14.2f%s %15.2f $%19.2f $%19.2f\n",
                            aCode, label, returnRate*100, "%", risk, annualReturn, value);
                    total += value;
                    returnTotal += annualReturn;
                    aggregateRisk += risk * value;
                }
                if (total != 0) {
                    aggregateRisk /= total;
                }
            }
            System.out.printf("%62s %57s\n", " ", latter);
            builder.setLength(0);
            for (int i = 0; i < 66; i++) {
                builder.append(' ');
            }
            System.out.printf("%s%s %15.4f $%19.2f $%19.2f\n", builder.toString(), "Total:",
                    aggregateRisk, returnTotal, total);
            System.out.print("\n\n");
        }

    }

    private static List<Portfolio> sortPortfolioList(List<Portfolio> portfolioList, List<Person> personList){
        for (Portfolio p : portfolioList) {
            for (Person person : personList) {
                if (p.getOwnerCode().equals(person.getPersonCode())) {
                    p.setLastName(person.getLastName());
                }
            }
        }

        class MyComprator implements Comparator<Portfolio>{
            @Override
            public int compare(Portfolio o1, Portfolio o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        }
        Collections.sort(portfolioList, new MyComprator());
        return portfolioList;
    }

}
