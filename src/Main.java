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
        WriteFile(personsList);

    }



    private static void WriteFile(ArrayList<Persons> person){
        //TODO:use gson
        Gson gson = new Gson();
        StringBuilder builder = new StringBuilder();
        for (Persons p :
                person) {
            builder.append(gson.toJson(p));
        }
        //Path file = Paths.get(URI.create("/Persons.json"));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Persons.json"), "utf-8"))) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

        System.out.println(builder.toString()+"\n Written.\n");

    }



}
