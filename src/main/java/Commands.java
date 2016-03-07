import com.squareup.okhttp.*;
import org.json.JSONObject;

import java.io.IOException;

public class Commands {

    private static final String url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "http://stepic.org/api/";

//    return String Token
    public static String doAuth(OkHttpClient client, String clientId, String clientSecret)
            throws IOException{

        String credential = Credentials.basic(clientId, clientSecret);

        RequestBody body = new FormEncodingBuilder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", credential)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new AuthException();
        }
        String res = response.body().string();
        JSONObject js = new JSONObject(res);
//        System.out.println(js.get("access_token"));

        return (String) js.get("access_token");
    }

    public static JSONObject test(OkHttpClient client, String add_url, String token) throws IOException {

        Request request = new Request.Builder()
                .url(api_url + add_url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("enrolled" , "True")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }
}
