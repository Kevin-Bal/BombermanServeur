package ApiClient;

import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class ClientHistoriqueService {

    public void postHistorique(Historique histo) throws Exception {
        String       postUrl       = "http://localhost:8080/SiteBomberman/api/historique/1";
        Gson         gson          = new Gson();
        CloseableHttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(histo));
        post.setEntity(postingString);

        post.setHeader("Content-type", "application/json");
        HttpResponse  response = httpClient.execute(post);

    }
}

