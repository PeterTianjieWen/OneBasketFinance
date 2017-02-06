import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Peter on 1/29/17.
 */
public class Main {

    public static void main(String argv[]) throws IOException {

        String personsFile = "./data/Persons.dat";
        String assetsFile = "./data/Assets.dat";
        Scanner p;
        Scanner a;
        try{
            p = new Scanner(new File(personsFile));
            a = new Scanner(new File(assetsFile));
        }catch(Exception e){
            throw new RuntimeException("Can't find \".dat\" file");
        }
        //create Persons.json
        File personFile = new File("./data/Persons.json");
        personFile.createNewFile();
        //create Assets.json
        File assetFile = new File("./data/Assets.json");
        assetFile.createNewFile();



        int numOfRecord;
        numOfRecord = Integer.parseInt(p.nextLine());
        int test = 0;
        ArrayList<Persons> personsList = new ArrayList<Persons>();
        //process Persons data
        while(p.hasNext()){
            String line = p.nextLine();
            String[] tempInfo = line.split(";");
            //j works as an counter to iterate over the whole tempInfo[]
            int j = 0;
            boolean isBroker = (tempInfo[1].length() == 0)? false: true;
            //In this parse, we put all data into temporary variables, then we use
            //the class built-in initializer to record all information
            String identityCode = tempInfo[j++];

            //record Broker Data(executes based on value of "isBroker")
            char level = ' ';
            String secIdentifier = null;
            if (isBroker){
                //record broker data
                String[] tmpSEC = tempInfo[j++].split(",");
                level = tmpSEC[0].toCharArray()[0];
                secIdentifier = tmpSEC[1];
            }else {
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
            if (tempInfo.length < 5){
                email = null;
            }else{
                String tmpEmail[] = tempInfo[j].split(",");
                for (int i = 0; i < tmpEmail.length; i++) {
                    email.add(tmpEmail[i]);
                }
            }

            if (isBroker){
                //Broker operation
                Broker broker = new Broker(identityCode, level, secIdentifier, firstName, lastName, email, address);
                personsList.add(broker);
                ++test;
            }else
            {
                //Customer operation
                Customer customer = new Customer(identityCode, firstName, lastName, email, address);
                personsList.add(customer);
                ++test;
            }
        }
        WritePersonsFile(personsList, personFile);
        p.close();
        if (test != numOfRecord){
            throw new RuntimeException("Wrong output");
        }
        //reset the counter
        test = 0;

        int totalRecord = Integer.parseInt(a.nextLine());
        //migrating Asset Information
        ArrayList<Asset> assetsList = new ArrayList<Asset>();
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
                ++test;
                continue;
            }


            double quarterlyDividend = Double.parseDouble(tempInfo[j++]);
            //convert percentage to decimal
            double baseRateOfReturn = Double.parseDouble(tempInfo[j++]);
            baseRateOfReturn /= 100;

            switch (lengthOfTempInfo) {
                //Stocks
                case(8):
                    double betaMeasure = Double.parseDouble(tempInfo[j++]);
                    String stockSymbol = tempInfo[j++];
                    double sharePrice = Double.parseDouble(tempInfo[j++]);
                    Stocks stocks = new Stocks(code, type, label, quarterlyDividend,
                            baseRateOfReturn, betaMeasure, stockSymbol, sharePrice);
                    assetsList.add(stocks);
                    ++test;
                    break;
                //Private Investment
                case(7):
                    double omegaMeasure = Double.parseDouble(tempInfo[j++]);
                    double  totalValue = Double.parseDouble(tempInfo[j++]);
                    PrivateInvest privateInvest = new PrivateInvest(code, type, label,
                            quarterlyDividend, baseRateOfReturn, omegaMeasure, totalValue);
                    assetsList.add(privateInvest);
                    ++test;
                    break;

                    default:
                        throw new RuntimeException("Not a valid account");


            }
        }
        WriteAssetsFile(assetsList, assetFile);
        //close file "Assets.dat"
        a.close();
        //check if out put all
        if (test != totalRecord){
            throw new RuntimeException("Wrong Output, test = "+test+" totalRecord = "+totalRecord);
        }
    }


    private static void WritePersonsFile(ArrayList<Persons> person,File file) throws IOException{

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        String toWrite = gson.toJson(person);
        jo.addProperty("Persons", toWrite);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(toWrite);
        fileWriter.close();
    }

    private static void WriteAssetsFile(ArrayList<Asset> asset, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(asset);
        JsonObject jo = new JsonObject();
        jo.add("Assets", je);
        String toWrite = gson.toJson(jo);
        //write file
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(toWrite);
        fileWriter.close();

    }
}
