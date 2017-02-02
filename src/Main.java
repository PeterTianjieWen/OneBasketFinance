import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Peter on 1/29/17.
 */
public class Main {

    public static void main(String argv[]) {

//        Gson test = new Gson();
//        Persons test1 = test.fromJson("{\"personCode\":\"944c\",\"firstName\":\" Starlin\",\"lastName\":\"Castro\"," +
//                        "\"address\":{\"street\":\"1060 West Addison Street\",\"city\":\"Chicago\"," +
//                        "\"state\":\"IL\",\"zip\":\"60613\",\"country\":\"USA\"}," +
//                        "\"email\":[\"scastro@cubs.com\",\"starlin_castro13@gmail.com\"]}",
//                        Persons.class);
//        System.out.println(test1.toString());
        String personsFile = "data/Persons.dat";
        String assetsFile = "data/Assets.dat";
        Scanner p;
        Scanner a;
        try{
            p = new Scanner(new File(personsFile));
            a = new Scanner(new File(assetsFile));
        }catch(Exception e){
            throw new RuntimeException("Can't find \".dat\" file");
        }

        int numOfRecord;
        numOfRecord = Integer.parseInt(p.nextLine());
        int n; //used to store total number of records
        boolean first = true;
        int counter = 0;

        ArrayList<Persons> personsList = new ArrayList<Persons>();
        //process Persons data
        while(p.hasNext()){
            String line = p.nextLine();

            String[] tempInfo = line.split(";");
//            for (String s :
//                    tempInfo) {
//                System.out.print(s+"    ");
//            }
//            System.out.println();
            int j = 0;
            boolean isBroker = (tempInfo[1].length() == 0)? false: true;
//            System.out.println(isBroker);


            //record Person Code
            String identityCode = tempInfo[j++];





            //record Broker Data(excute based on value of "isBroker")
            char level = ' ';
            String secIdentifier = null;
            if (isBroker){
                String[] tmpSEC = tempInfo[j++].split(",");
                level = tmpSEC[0].toCharArray()[0];
                secIdentifier = tmpSEC[1];
            }else {
                j++;
            }
            //record Name: lastname, firstname
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
            //record e-mail:optional
            List<String> email = new ArrayList<String>();
            //System.out.println(tempInfo.length);
            if (tempInfo.length < 5){ //4 stands for id, brokerInfo, name, address and e-mail
                email = null;
            }else{
                String tmpEmail[] = tempInfo[j].split(",");
                for (int i = 0; i < tmpEmail.length; i++) {
                    email.add(tmpEmail[i]);
                }
            }

            if (isBroker){
                //TODO:Broker operation
                Broker broker = new Broker(identityCode, level, secIdentifier, firstName, lastName, email, address);
                personsList.add(broker);
            }else
            {
                //TODO:Customer operation
                Customer customer = new Customer(identityCode, firstName, lastName, email, address);
                personsList.add(customer);
            }
        }
        WritePersonsFile(personsList);

        int totalRecord = Integer.parseInt(a.nextLine());
        //migrating Asset Infomation
        ArrayList<Asset> assetsList = new ArrayList<>();
        while(a.hasNext()){
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
            if(lengthOfTempInfo == 4){
                //Deposit Accounts
                Deposit deposit = new Deposit(code, type, label, Double.parseDouble(tempInfo[j++]));
                assetsList.add(deposit);
                continue;
            }


            double quarterlyDividend = Double.parseDouble(tempInfo[j++]);
            double baseRateOfReturn = Double.parseDouble(tempInfo[j++]);


            switch (lengthOfTempInfo) {
                //Stocks
                case(8):
                    double betaMeasure = Double.parseDouble(tempInfo[j++]);
                    String stockSymbol = tempInfo[j++];
                    double sharePrice = Double.parseDouble(tempInfo[j++]);
                    Stocks stocks = new Stocks(code, type, label, quarterlyDividend,
                            baseRateOfReturn, betaMeasure, stockSymbol, sharePrice);
                    assetsList.add(stocks);
                    break;
                //Private Investment
                case(7):
                    double omegaMeasure = Double.parseDouble(tempInfo[j++]);
                    double totalValue = Double.parseDouble(tempInfo[j++]);
                    PrivateInvest privateInvest = new PrivateInvest(code, type, label,
                            quarterlyDividend, baseRateOfReturn, omegaMeasure, totalValue);
                    assetsList.add(privateInvest);
                    break;

                    default:
                        throw new RuntimeException("Not a valid account");
            }
            WriteAssetsFile(assetsList);




        }

    }



    private static void WritePersonsFile(ArrayList<Persons> person){
        //TODO:use gson
        Gson gson = new Gson();
        String toWrite = gson.toJson(person);

        //Path file = Paths.get(URI.create("/Persons.json"));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Persons.json"), "utf-8"))) {
            writer.write(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }

       // System.out.println(builder.toString()+"\n Written.\n");

    }

    private static void WriteAssetsFile(ArrayList<Asset> asset){
        //TODO:use gson
        Gson gson = new Gson();
        String toWrite = gson.toJson(asset);

        //Path file = Paths.get(URI.create("/Persons.json"));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Asset.json"), "utf-8"))) {
            writer.write(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }

       // System.out.println(builder.toString()+"\n Written.\n");

    }



}
