package ApiClient;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class ClientUtilisateurService {

    public Utilisateur getUtilisateur(String email, String mdp) throws IOException {
        String userString = null;
        Gson gson=new Gson();
        URL obj = new URL("http://localhost:8080/SiteBomberman/api/users/"+email);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            userString = response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        JsonObject userJson = new JsonParser().parse(userString).getAsJsonObject();

        Utilisateur user = gson.fromJson(userJson,Utilisateur.class);

        return user;

    }

    public static void main(String[] args) throws IOException {
        ClientUtilisateurService user = new ClientUtilisateurService();
        Utilisateur utilisateur = user.getUtilisateur("roger@rog.fr","pute");
        System.out.println(utilisateur.getEmail()+" "+utilisateur.getPassWord()+" "+utilisateur.getUserName());
    }
}
