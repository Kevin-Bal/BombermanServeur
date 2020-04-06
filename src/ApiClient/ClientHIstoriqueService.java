package ApiClient;

import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class ClientHIstoriqueService {

    public static void postHistorique(Historique histo) throws Exception {
        String       postUrl       = "http://localhost:8080/SiteBomberman/api/historique/1";
        Gson         gson          = new Gson();
        CloseableHttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(histo));
        post.setEntity(postingString);

        post.setHeader("Content-type", "application/json");
        HttpResponse  response = httpClient.execute(post);

    }

    public static void main(String[] args) throws Exception {
        Historique histo = new Historique();

        histo.setEmailJoueur("kevin@kev.fr");
        histo.setMapName("DropTheBeat");
        histo.setModeJeu("PVP");
        histo.setNbJoueur(2);
        histo.setScore(4000);
        histo.setVictoire("Faux");
        histo.setUsernameJoueur("Kevil");

        Gson         gson          = new Gson();
        gson.toJson(histo);

        //System.out.println(gson.toString());

        postHistorique(histo);
    }
}

