package main.stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.courseFormat.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    private static boolean isInstanceProperty = false;

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

    public static void initToken(Project project) throws UnirestException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        if (!isInstanceProperty) {
            setSSLProperty();
            isInstanceProperty = true;
        }
        setTokenGRP(project);
    }

    private static void setTokenGRP(Project project) throws UnirestException {
        StudentService ws = StudentService.getInstance(project);
        String user = ws.getLogin();
        String pass = ws.getPassword();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("username", user);
        parameters.put("password", pass);
        parameters.put("client_id", ws.getClientId());

        TokenInfo tokenInfo = postToStepicMapLinkReset(token_url, parameters, TokenInfo.class);

        ws.setToken(tokenInfo.access_token);
        LOG.warn("token = " + tokenInfo.access_token);
        ws.setRefresh_token(tokenInfo.refresh_token);
    }

    private static <T> T getFromStepic(String link, final Class<T> container, String token) throws UnirestException {

        HttpResponse<String> response;
        response = Unirest
                .get(api_url + link)
                .header("Authorization", "Bearer " + token)
                .asString();
        final String responseString = response.getBody();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseString, container);
    }

    private static <T> T getFromStepic(String link, Map<String, Object> queryMap, final Class<T> container, String token)
            throws UnirestException {

        HttpResponse<String> response;
        response = Unirest
                .get(api_url + link)
                .header("Authorization", "Bearer " + token)
                .queryString(queryMap)
                .asString();
        final String responseString = response.getBody();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseString, container);
    }

    private static <T> T postToStepic(String link, JSONObject jsonObject, final Class<T> container, String token) throws UnirestException {
        HttpResponse<JsonNode> response;
        response = Unirest
                .post(api_url + link)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(jsonObject)
                .asJson();
        final JSONObject responseJson = response.getBody().getObject();

        LOG.warn(responseJson.toString());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseJson.toString(), container);
    }

    private static <T> T postToStepicMapLinkReset(String link, Map<String, Object> parameters, final Class<T> container) throws UnirestException {
        HttpResponse<String> response;
        response = Unirest
                .post(link)
                .fields(parameters)
                .asString();
        final String responseString = response.getBody();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(responseString, container);
    }

    public static AuthorWrapper getCurrentUser( String token) {
        try {
            return getFromStepic("stepics/1", AuthorWrapper.class, token);
        } catch (UnirestException e) {
            LOG.error("Couldn't get author info");
        }
        return null;
    }

    public static String getUserName(String token) {
        AuthorWrapper aw = getCurrentUser(token);
        return aw.users.get(0).getName();
    }

    public static List<Course> getCourses(String courseId, String token) throws UnirestException {
        final String url = "courses/" + courseId;
        return getFromStepic(url, CoursesContainer.class,  token).courses;
    }

    public static List<Section> getSections(String idsQuery, String token) throws UnirestException {
        final String url = "sections" + idsQuery;
        return getFromStepic(url, SectionsContainer.class,  token).sections;
    }

    public static List<Unit> getUnits(String idsQuery, String token) throws UnirestException {
        final String url = "units" + idsQuery;
        return getFromStepic(url, UnitsContainer.class, token).units;
    }

    public static List<Lesson> getLessons(String idsQuery, String token) throws UnirestException {
        final String url = "lessons" + idsQuery;
        return getFromStepic(url, LessonsContainer.class,  token).lessons;
    }

    public static List<Submission> getStatus(String submissionID, String token) throws UnirestException {
        final String url = "submissions/" + submissionID;
        return getFromStepic(url, SubmissionsContainer.class, token).submissions;
    }

    public static List<Step> getSteps(String stepIdQuery, String token) throws UnirestException {
        final String url = "steps" + stepIdQuery;
        return getFromStepic(url, StepsContainer.class, token).steps;
    }

    public static String getAttemptId(String stepId, String token) throws UnirestException {
        String attempts = "attempts";
        JSONObject first = new JSONObject();
        JSONObject second = new JSONObject();

        first.put("step", stepId);
        second.put("attempt", first);

        int id = postToStepic(attempts, second, AttemptsContainer.class, token).attempts.get(0).id;
        return Integer.toString(id);
    }

    public static String sendFile(String file, String att_id, String token) throws UnirestException {

        JSONObject wrapper = new JSONObject();
        JSONObject submission = new JSONObject();
        JSONObject reply = new JSONObject();

        reply.put("language", "java8");
        reply.put("code", file);

        submission.put("attempt", att_id);
        submission.put("reply", reply);

        wrapper.put("submission", submission);

        int id = postToStepic("submissions", wrapper, SubmissionsContainer.class, token).submissions.get(0).id;
        return Integer.toString(id);
    }

    public static List<Submission> getStatusTask(String stepId, Pair<String, String> pair, String token) throws UnirestException {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("step", stepId);
        queryMap.put(pair.first, pair.second);

        return getFromStepic("submissions", queryMap, SubmissionsContainer.class, token).submissions;
    }

    public static class AuthorWrapper {
        public List<CourseInfo.Author> users;
    }

    public static class CoursesContainer {
        public List<Course> courses;
        public Map meta;
    }

    public static class SectionsContainer {
        public List<Section> sections;
        public Map meta;
    }

    public static class UnitsContainer {
        public List<Unit> units;
        public Map meta;
    }

    public static class LessonsContainer {
        public List<Lesson> lessons;
        public Map meta;
    }

    public static class StepsContainer {
        public List<Step> steps;
        public Map meta;
    }

    public static class SubmissionsContainer {
        public List<Submission> submissions;
        public Map meta;
    }

    public static class AttemptsContainer {
        public List<Attempt> attempts;
        public Map meta;
    }

    public static List<Submission> getSubmissions(String stepId, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("step", stepId);
        try {
            return getFromStepic("submissions", map, SubmissionsContainer.class, token).submissions;
        } catch (UnirestException e) {
            LOG.error("getSubmissions error " + e.getMessage());
            return new ArrayList<>();
        }
    }

}

