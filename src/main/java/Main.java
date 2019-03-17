import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Main{

    public static void main(String[] args) throws Exception {

        try {
            int choiceMenu;
            do {
                System.out.println("What do you want to do ?\n1...Play\n2...See analiticZZ\n3...Quit");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                choiceMenu = Integer.parseInt(reader.readLine());
                Client client = Client.create();
                WebResource webResource;
                ClientResponse res;
                JSONParser jsonParser = new JSONParser();
                if (choiceMenu == 1) {
                    System.out.println("Please enter the ceiling number of this game :");
                    int choiceCeil = Integer.parseInt(reader.readLine());
                    if (choiceCeil > 0) {
                        webResource = client.resource("http://localhost:8080/api/getNumberId/" + choiceCeil);
                        res = webResource.type("application/x-www-form-urlencoded").get(ClientResponse.class);
                        JSONObject jsonRes = (JSONObject) jsonParser.parse(res.getEntity(String.class));
                        int playId = Integer.parseInt((String) jsonRes.get("numberId"));
                        boolean found = false;
                        while (!found) {
                            System.out.println("Propose a value :");
                            Integer choiceValue = Integer.parseInt(reader.readLine());
                            webResource = client.resource("http://localhost:8080/api/proposeValue");
                            MultivaluedMap formData = new MultivaluedMapImpl();
                            formData.add("id", Integer.toString(playId));
                            formData.add("proposal", Integer.toString(choiceValue));
                            res = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
                            jsonRes = (JSONObject) jsonParser.parse(res.getEntity(String.class));
                            System.out.println(jsonRes.get("result"));
                            if (((String) jsonRes.get("result")).matches("You win !")) {
                                found = true;
                                System.out.println("");
                            }
                        }
                    }
                } else if (choiceMenu == 2) {
                    System.out.println("What statistic do you want to consult ?\n1...Number of game played\n2...Average number of attempts to win");
                    int choiceAnalytic = Integer.parseInt(reader.readLine());
                    if(choiceAnalytic == 1){
                        webResource = client.resource("http://localhost:8042/api/getNumberOfPlays");
                        res = webResource.type("application/x-www-form-urlencoded").get(ClientResponse.class);
                        JSONObject jsonRes = (JSONObject) jsonParser.parse(res.getEntity(String.class));
                        int numberPlayed = Integer.parseInt((String) jsonRes.get("numberOfPlays"));
                        System.out.println("Number of game played : " + numberPlayed);
                    }else if(choiceAnalytic == 2){
                        webResource = client.resource("http://localhost:8042/api/getAvgNumberOfAttempts");
                        res = webResource.type("application/x-www-form-urlencoded").get(ClientResponse.class);
                        JSONObject jsonRes = (JSONObject) jsonParser.parse(res.getEntity(String.class));
                        int numberOfAttempts = (int)(Float.parseFloat((String) jsonRes.get("numberOfAttempts")));
                        System.out.println("Average number of attempts to win a game : " + numberOfAttempts);
                    }
                }
            }while(choiceMenu != 3);
        } catch(NumberFormatException e){
            System.out.println("You have to enter a number");
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

}