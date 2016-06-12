package main.stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.edu.stepic.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepicConnector {

    private static final String token_url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "https://stepic.org/api/";

    private static final Logger LOG = Logger.getInstance(StepicConnector.class);
    private static boolean tokenInit = false;
    private static ApplicationService ws = ApplicationService.getInstance();

    private static void setSSLProperty() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
// Create a trust manager that does not validate certificate for this connection
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        Unirest.setHttpClient(httpclient);
    }

    public static void initToken() {
        if (!tokenInit) {
            try {
                setSSLProperty();
                tokenInit = true;
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException e) {
                LOG.error(e);
            }
        }
//        ApplicationService.getInstance().setToken(
//                getToken(ApplicationService.getInstance().getClientId(), ApplicationService.getInstance().getClientSecret()));
        setTokenGRP();
    }

    private static String getToken(String user, String pass) {
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest
                    .post(token_url)
                    .basicAuth(user, pass)
                    .field("grant_type", "client_credentials")
                    .asJson();
        } catch (UnirestException e) {
            LOG.error(e);
        }
        return (String) jsonResponse.getBody().getObject().get("access_token");
    }

    private static void setTokenGRP() {
        String user = ws.getLogin();
        String pass = ws.getPassword();
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest
                    .post(token_url)
                    .field("grant_type", "password")
                    .field("username", user)
                    .field("password", pass)
                    .field("client_id", ApplicationService.getInstance().getClientId())
                    .asJson();
        } catch (UnirestException e) {
            LOG.error(e);
        }

        try {
            ws.setToken(jsonResponse.getBody().getObject().getString("access_token"));
            ws.setRefresh_token(jsonResponse.getBody().getObject().getString("refresh_token"));
        } catch (JSONException e) {
            LOG.error("Authorization error. Please sign in");
            ws.setToken(null);
        }
    }

    private static <T> T getFromStepic(String link, final Class<T> container) throws UnirestException {

        HttpResponse<String> response;
        response = Unirest
                .get(api_url + link)
                .header("Authorization", "Bearer " + ApplicationService.getInstance().getToken())
                .asString();
        final String responseString = response.getBody();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseString, container);
    }

    private static <T> T getFromStepic(String link, Map<String, Object> queryMap, final Class<T> container)
            throws UnirestException {

        HttpResponse<String> response;
        response = Unirest
                .get(api_url + link)
                .header("Authorization", "Bearer " + ApplicationService.getInstance().getToken())
                .queryString(queryMap)
                .asString();
        final String responseString = response.getBody();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseString, container);
    }

    public static AuthorWrapper getCurrentUser() {
        try {
            return getFromStepic("stepics/1", AuthorWrapper.class);
        } catch (UnirestException e) {
            LOG.error("Couldn't get author info");
        }
        return null;
    }

    public static String getUserName() {
        AuthorWrapper aw = getCurrentUser();
        return aw.users.get(0).getName();
    }

    public static List<MyCourse> getCourses(String courseId) {
        final String url = "courses/" + courseId;
        try {
            return getFromStepic(url, CoursesContainer.class).courses;
        } catch (UnirestException e) {
            LOG.error("getCourses error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<MySection> getSections(String idsQuery) {
        final String url = "sections" + idsQuery;
        try {
            return getFromStepic(url, SectionsContainer.class).sections;
        } catch (UnirestException e) {
            LOG.error("getSection error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<MyUnit> getUnits(String idsQuery) {
        final String url = "units" + idsQuery;
        try {
            return getFromStepic(url, UnitsContainer.class).units;
        } catch (UnirestException e) {
            LOG.error("getUnit error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<MyLesson> getLessons(String idsQuery) {
        final String url = "lessons" + idsQuery;
        try {
            return getFromStepic(url, LessonsContainer.class).lessons;
        } catch (UnirestException e) {
            LOG.error("getLesson error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<MyStep> getSteps(String stepIdQuery) {
        final String url = "steps" + stepIdQuery;
        try {
            return getFromStepic(url, StepsContainer.class).steps;
        } catch (UnirestException e) {
            LOG.error("getStep error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static String getAttemptId(String stepId) {
        String attempts = "attempts";
        JSONObject first = new JSONObject();
        JSONObject second = new JSONObject();

        first.put("step", stepId);
        second.put("attempt", first);

        HttpResponse<JsonNode> response = null;
        LOG.warn(second.toString());
        LOG.warn(ws.getToken());
        try {
            response = Unirest
                    .post(api_url + attempts)
                    .header("Authorization", "Bearer " + ws.getToken())
                    .header("Content-Type", "application/json")
                    .body(second)
                    .asJson();

        } catch (UnirestException e) {
            LOG.error("Get Attempt Id error\n" + e.getMessage());
        }

        JSONObject tmp = response.getBody().getObject();
        JSONArray oo = (JSONArray) tmp.get("attempts");
        LOG.warn(Integer.toString(oo.getJSONObject(0).getInt("id")));
        return Integer.toString(oo.getJSONObject(0).getInt("id"));
    }

    public static String sendFile(String file, String att_id) throws UnirestException {

        JSONObject wrapper = new JSONObject();
        JSONObject submission = new JSONObject();
        JSONObject reply = new JSONObject();

        reply.put("language", "java8");
        reply.put("code", file);

        submission.put("attempt", att_id);
        submission.put("reply", reply);

        wrapper.put("submission", submission);

        HttpResponse<JsonNode> response = null;
//        try {
            response = Unirest
                    .post(api_url + "submissions")
                    .header("Authorization", "Bearer " + ws.getToken())
                    .header("Content-Type", "application/json")
                    .body(wrapper)
                    .asJson();
//        } catch (UnirestException e) {
//            LOG.error("Send File error\n" + e.getMessage());
//        }
        JSONObject tmp = response.getBody().getObject();
        JSONArray oo = (JSONArray) tmp.get("submissions");
        return Integer.toString(oo.getJSONObject(0).getInt("id"));
    }

    public static String getStatusTask(String submissionId) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .get(api_url + "submissions/" + submissionId)
                    .header("Authorization", "Bearer " + ws.getToken())
                    .asJson();
        } catch (UnirestException e) {
            LOG.error("get Task status error\n" + e.getMessage());
        }

        JSONObject tmp = response.getBody().getObject();
        JSONArray oo = (JSONArray) tmp.get("submissions");
        return oo.getJSONObject(0).getString("status");
    }

    public static List<SubmissionsNode> getStatusTask(String stepId, Pair<String, String> pair) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .get(api_url + "submissions")
                    .header("Authorization", "Bearer " + ws.getToken())
                    .queryString("step", stepId)
                    .queryString(pair.first, pair.second)
                    .asJson();
        } catch (UnirestException e) {
            LOG.error("get Task status error\n" + e.getMessage());
        }


        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(response.getBody().getObject().toString(), SubmissionsContainer.class).submissions;
    }

    public static class AuthorWrapper {
        public List<CourseInfo.Author> users;
    }

    public static class CoursesContainer {
        public List<MyCourse> courses;
        public Map meta;
    }

    public static class SectionsContainer {
        public List<MySection> sections;
        public Map meta;
    }

    public static class UnitsContainer {
        public List<MyUnit> units;
        public Map meta;
    }

    public static class LessonsContainer {
        public List<MyLesson> lessons;
        public Map meta;
    }

    public static class StepsContainer {
        public List<MyStep> steps;
        public Map meta;
    }

    public static class SubmissionsContainer {
        public List<SubmissionsNode> submissions;
        public Map meta;
    }



    public static List<SubmissionsNode> getSubmissions(String stepId) {
        Map<String, Object> map = new HashMap<>();
        map.put("step", stepId);
        try {
            return getFromStepic("submissions", map, SubmissionsContainer.class).submissions;
        } catch (UnirestException e) {
            LOG.error("getSubmissions error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static String getIdQuery(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Integer id : list) {
            sb.append("ids[]=" + id + "&");
        }
        return sb.toString();
    }

}

