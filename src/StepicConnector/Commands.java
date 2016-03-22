package StepicConnector;

import Stepic.CourseInfo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Commands {

    private static final String url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "http://stepic.org/api/";
    private static final OkHttpClient client = new OkHttpClient();

    //    return String Token
    public static String getToken(String clientId, String clientSecret)
            throws IOException, JSONException {

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
//        if (response.code() != 200) {
//            throw new IOException();
//        }
        String res = response.body().string();
//        System.out.println(response.code());
        System.out.println(res);
        JSONObject js = new JSONObject(res);



        String token = (String) js.get("access_token");
        WorkerService.getInstance().setToken(token);

        return token;
    }

    public static JSONObject test(String add_url, String token) throws IOException, JSONException {

        Request request = createAndSetHeader(add_url, token);

        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    private static Request createAndSetHeader(String add_url, String token) {
        Request request =  new Request.Builder()
                .url(api_url + add_url)
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();
        System.out.println(request.headers());
        return request;
    }

    public static AuthorWrapper getCurrentUser() {
        try {
            return getFromStepic("stepics/1/", AuthorWrapper.class);
        }
        catch (IOException e) {
            System.err.print("Couldn't get author info");
        }
        return null;
    }

    private static <T> T getFromStepic(String link, final Class<T> container) throws IOException {

        final Request request = createAndSetHeader(link, WorkerService.getInstance().getToken());
//        OkHttpClient client2 = new OkHttpClient();
        Response response = client.newCall(request).execute();

//        OAuth20Service service = new ServiceBuilder()
//                .apiKey(WorkerService.getInstance().clientId)
//                .apiSecret(WorkerService.getInstance().clientSecret)
//                .callback(api_url+link)
//                .grantType("client_credentials")
//                .build(StepicAPI.instance());



        final String responseString = response.body() != null ? response.body().string() : "";
//        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
//            throw new IOException("Stepic returned non 200 status code " + responseString);
//        }
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(responseString, container);
    }


    static public class AuthorWrapper {
        List<CourseInfo.Author> users;
    }

}
