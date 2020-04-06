package ApiClient;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class ClientUtilisateurService {

    private static final String ALGO_CHIFFREMENT = "SHA-256";

    public Utilisateur getUtilisateur(String email, String mdp) throws Exception {
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
            JsonObject userJson = new JsonParser().parse(userString).getAsJsonObject();

            Utilisateur user = gson.fromJson(userJson,Utilisateur.class);
            if(validationMotsDePasse(mdp, user.getPassWord())){
                System.out.println("Mot de passe bon ");
                return user;
            }
            else return null;

        } else {
            System.out.println("GET request not worked");
            return null;
        }


    }

    private boolean validationMotsDePasse( String motDePasse, String motDePasseDB) throws Exception {
        if ( motDePasse.length() > 3 ) {
            //Verification du mot de passe
            ConfigurablePasswordEncryptor passwordCheck = new ConfigurablePasswordEncryptor();
            passwordCheck.setAlgorithm( ALGO_CHIFFREMENT );
            passwordCheck.setPlainDigest( false );
            if(passwordCheck.checkPassword(motDePasse, motDePasseDB))
                return true;
            else return false;

        } else {
            return false;
        }
    }


}
