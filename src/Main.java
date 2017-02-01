import com.google.gson.Gson;
import java.io.File;
import java.util.Scanner;
/**
 * Created by Peter on 1/29/17.
 */
public class Main {

    public static void main(String argv[]) {

        String filename = "data/Persons.dat";
        Scanner s;
        try{
            s = new Scanner(new File(filename));
        }catch(Exception e){
            throw new RuntimeException("Can't find \"Persons.dat\"");

        }
        int numOfRecord;
        numOfRecord = Integer.parseInt(s.nextLine());
        int n; //used to store total number of records
        boolean first = true;
        int counter = 0;


        while(s.hasNext()){
            String line = s.nextLine();
            String[] tempInfo = line.split(";");
            int j = 0;
            boolean isBroker = false;
            for (int i = 0; i < line.length(); i++) {
                if ((line.toCharArray()[i] == 'E' || line.toCharArray()[i] == 'J')
                        && line.toCharArray()[i+1] == ','){
                    isBroker = true;
                }
            }

            //TODO:record Person Code
            String identityCode = tempInfo[j++];
            //TODO:record Broker Data(excute based on value of "isBroker")
            char level;
            String secIdentifier;
            if (isBroker){
                String[] tmpSEC = tempInfo[j++].split(",");
                level = tmpSEC[0].toCharArray()[0];
                secIdentifier = tmpSEC[1];
            }
            //TODO:record Name: lastname, firstname
            String[] tmpName = tempInfo[j++].split(",");
            String lastName = tmpName[0];
            String firstName = tmpName[1];
            //TODO:record Address: STREET,CITY,STATE,ZIP,COUNTRY
            String[] tmpAddress = tempInfo[j++].split(",");
            String street = tmpAddress[0];
            String city = tmpAddress[1];
            String state = tmpAddress[2];
            String zip = tmpAddress[3];
            String country = tmpAddress[4];
            //TODO:record e-mail:optional
            String email;
            if (j == tempInfo.length){
                email = tempInfo[j];
            }else{
                email = null;
            }
        }
        Gson gson = new Gson();



    }
}
