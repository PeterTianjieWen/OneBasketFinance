import java.io.File;
import java.util.*;

/**
 * Created by Peter on 1/29/17.
 */
public class Main {

    public static void main(String argv[]) {
        List<Person> p = null;
        List<Asset> a = null;

        try {
             p = personParse();
             a = assetParse();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Retrieve retrieve = new Retrieve(p, a);
        List<Portfolio> portfolioList = portfolioParse(p, a, retrieve);
        sortList(portfolioList, p);
        printPortfolioSummary(portfolioList, retrieve);
        printPortfolioDetail(portfolioList, retrieve);

    }


    public static List<Portfolio> portfolioParse(List<Person> p, List<Asset> a, Retrieve retrieve) {

        Scanner po;
        try {
            po = new Scanner(new File("./data/Portfolios.dat"));
        } catch (Exception e) {
            throw new RuntimeException("Portfolio.dat read fails");
        }

        List<Portfolio> portfolioList = new ArrayList<>();
        int numberOfRecord = Integer.parseInt(po.nextLine());
        while (po.hasNext()) {
            Map<String, Double> assetMap = new HashMap<>();
            String line = po.nextLine();
            String tempForInfo[] = line.split(";");

            int j = 0;//use as tracker
            String portfolioCode = tempForInfo[j++];
            String ownerCode = tempForInfo[j++];
            String managerCode = tempForInfo[j++];
            String beneficiaryCode = null;

            if (tempForInfo.length > 3 && tempForInfo[j].length() != 0) {
                beneficiaryCode = tempForInfo[j++];
            } else {
                j++;
            }

            if (tempForInfo.length > 4) {
                String assetList[] = tempForInfo[j].split(";");
                for (String tmp : assetList) {
                    String codeAndValue[] = tmp.split(",");
                    for (int i = 0; i < codeAndValue.length; i++) {
                        String temp[] = codeAndValue[i].split(":");
                        assetMap.put(temp[0], Double.parseDouble(temp[1]));
                    }

                }
            }

            Portfolio portfolio = new Portfolio(portfolioCode, ownerCode, managerCode,
                    beneficiaryCode, assetMap, a);

            portfolioList.add(portfolio);
        }
        //close scanner
        po.close();
        return portfolioList;
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
                Person owner;
                Broker manager;

                owner = retrieve.getPerson(p.getOwnerCode());
                String ownerName = owner.getName();

                manager = (Broker) retrieve.getPerson(p.getManagerCode());
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

                    double fee = 0;
                    double commission;
                    int assetAmount = p.getAssetMap().size();
                    if (manager.getLevel() == 'J') {
                        //Junior
                        fee = assetAmount * 50;
                        commission = 0.02 * perTotalReturn;
                    } else {
                        //Senior
                        fee = assetAmount * 10;
                        commission = 0.05 * perTotalReturn;
                    }
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
                    returnTotal += returnTotal;
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

    private static List<Person> personParse() {
        String personFile = "./data/Persons.dat";
        Scanner p;
        try {
            p = new Scanner(new File(personFile));
        } catch (Exception e) {
            throw new RuntimeException("Can't find \".dat\" file");
        }


        int numOfRecord;
        numOfRecord = Integer.parseInt(p.nextLine());
        int test = 0;
        ArrayList<Person> personList = new ArrayList<Person>();
        //process Person data
        while (p.hasNext()) {
            String line = p.nextLine();
            String[] tempInfo = line.split(";");
            //j works as an counter to iterate over the whole tempInfo[]
            int j = 0;
            boolean isBroker = (tempInfo[1].length() == 0) ? false : true;
            //In this parse, we put all data into temporary variables, then we use
            //the class built-in initializer to record all information
            String identityCode = tempInfo[j++];

            //record Broker Data(executes based on value of "isBroker")
            char level = ' ';
            String secIdentifier = null;
            if (isBroker) {
                //record broker data
                String[] tmpSEC = tempInfo[j++].split(",");
                level = tmpSEC[0].toCharArray()[0];
                secIdentifier = tmpSEC[1];
            } else {
                //omit this section and go on
                j++;
            }
            //record Name: lastName, firstName
            String[] tmpName = tempInfo[j++].split(",");
            String lastName = tmpName[0];
            String firstName = tmpName[1];
            //record Address: STREET,CITY,STATE,ZIP,COUNTRY
            String[] tmpAddress = tempInfo[j++].split(",");
            String street = tmpAddress[0];
            String city = tmpAddress[1];
            String state = tmpAddress[2];
            String zip = tmpAddress[3];
            String country = tmpAddress[4];
            Address address = new Address(street, city, state, zip, country);
            //record e-mail address(es):optional
            List<String> email = new ArrayList<>();
            //5 stands for id, brokerInfo, name, address and e-mail
            //if less than 5, then there is no e-mail so set it to null
            if (tempInfo.length < 5) {
                email = null;
            } else {
                String tmpEmail[] = tempInfo[j].split(",");
                for (int i = 0; i < tmpEmail.length; i++) {
                    email.add(tmpEmail[i]);
                }
            }

            if (isBroker) {
                //Broker operation
                Broker broker = new Broker(identityCode, level, secIdentifier, firstName, lastName, email, address);
                personList.add(broker);
                ++test;
            } else {
                //Customer operation
                Customer customer = new Customer(identityCode, firstName, lastName, email, address);
                personList.add(customer);
                ++test;
            }
        }

        p.close();
        if (test != numOfRecord) {
            throw new RuntimeException("Wrong output");
        }

        return personList;
    }

    private static List<Asset> assetParse() {
        String assetsFile = "./data/Assets.dat";
        Scanner a;

        try {
            a = new Scanner(new File(assetsFile));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


        int test = 0;
        int totalRecord = Integer.parseInt(a.nextLine());
        //migrating Asset Information
        List<Asset> assetsList = new ArrayList<Asset>();
        while (a.hasNext()) {
            String line = a.nextLine();
            String tempInfo[] = line.split(";");
            int j = 0;
            //code
            String code = tempInfo[j++];
            //type
            char type = tempInfo[j++].toCharArray()[0];
            //label
            String label = tempInfo[j++];
            int lengthOfTempInfo = tempInfo.length;
            if (lengthOfTempInfo == 4) {
                //Deposit Accounts
                Deposit deposit = new Deposit(code, type, label, Double.parseDouble(tempInfo[j++]));
                assetsList.add(deposit);
                ++test;
                continue;
            }


            double quarterlyDividend = Double.parseDouble(tempInfo[j++]);
            //convert percentage to decimal
            double baseRateOfReturn = Double.parseDouble(tempInfo[j++]);
            baseRateOfReturn /= 100;

            switch (lengthOfTempInfo) {
                //Stocks
                case (8):
                    double betaMeasure = Double.parseDouble(tempInfo[j++]);
                    String stockSymbol = tempInfo[j++];
                    double sharePrice = Double.parseDouble(tempInfo[j++]);
                    Stocks stocks = new Stocks(code, type, label, quarterlyDividend,
                            baseRateOfReturn, betaMeasure, stockSymbol, sharePrice);
                    assetsList.add(stocks);
                    ++test;
                    break;
                //Private Investment
                case (7):
                    double omegaMeasure = Double.parseDouble(tempInfo[j++]);
                    double totalValue = Double.parseDouble(tempInfo[j++]);
                    PrivateInvest privateInvest = new PrivateInvest(code, type, label,
                            quarterlyDividend/100, baseRateOfReturn, omegaMeasure, totalValue);
                    assetsList.add(privateInvest);
                    ++test;
                    break;

                default:
                    throw new RuntimeException("Not a valid account");
            }
        }
        //close file "Assets.dat"
        a.close();
        //check if out put all
        if (test != totalRecord) {
            throw new RuntimeException("Wrong Output, test = " + test + " totalRecord = " + totalRecord);
        }

        return assetsList;
    }

    private static List<Portfolio> sortList(List<Portfolio> portfolioList, List<Person> personList){
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
