package main.stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;

public class StepicConnector {

    private static final String token_url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "https://stepic.org/api/";
    private static final String clientID = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";

    private static final Logger LOG = Logger.getInstance(StepicConnector.class);
    private static boolean isInstanceProperty = false;

    private static void setSSLProperty() {
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
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslcsf)
                .build();
        Unirest.setHttpClient(httpclient);
    }

    public static void initToken(Project project) throws UnirestException {
        if (!isInstanceProperty) {
            setSSLProperty();
            isInstanceProperty = true;
        }

//        try {
            setTokenGRP(project);
//        } catch (UnirestException e) {
//            Notification notification = new Notification("Init.token", "Stepic authorization error", "Please check your internet configuration.", NotificationType.WARNING);
//            notification.notify(project);
//        }
    }

    private static void setTokenGRP(Project project) throws UnirestException {
        StudentService ws = StudentService.getInstance(project);
        String user = ws.getLogin();
        String pass = ws.getPassword();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("username", user);
        parameters.put("password", pass);
        parameters.put("client_id", clientID);

        TokenInfo tokenInfo = postToStepicMapLinkReset(token_url, parameters, TokenInfo.class);

//<<<<<<< HEAD
//        if (tokenInfo.access_token != null) {
//            ws.setToken(tokenInfo.access_token);
//            ws.setRefresh_token(tokenInfo.refresh_token);
//            return true;
//        } else {
//            return false;
//=======
        String token = tokenInfo.access_token;
        if (token != null && !token.isEmpty()) {
            ws.setToken(tokenInfo.access_token);
            ws.setRefresh_token(tokenInfo.refresh_token);
        }
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

    public static AuthorWrapper getCurrentUser(String token) throws UnirestException {
        return getFromStepic("stepics/1", AuthorWrapper.class, token);
    }

    public static String getUserName(Project project) throws UnirestException {
        AuthorWrapper aw = getCurrentUser(getToken(project));
        return aw.users.get(0).getName();
    }

    public static List<Course> getCourses(String courseId, String token) throws UnirestException {
        final String url = "courses/" + courseId;
        return getFromStepic(url, CoursesContainer.class, token).courses;
    }

    public static List<Section> getSections(String idsQuery, String token) throws UnirestException {
        final String url = "sections" + idsQuery;
        return getFromStepic(url, SectionsContainer.class, token).sections;
    }

    public static List<Unit> getUnits(String idsQuery, String token) throws UnirestException {
        final String url = "units" + idsQuery;
        return getFromStepic(url, UnitsContainer.class, token).units;
    }

    public static List<Lesson> getLessons(String idsQuery, String token) throws UnirestException {
        final String url = "lessons" + idsQuery;
        return getFromStepic(url, LessonsContainer.class, token).lessons;
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

        List<Attempt> attemptsL = postToStepic(attempts, second, AttemptsContainer.class, token).attempts;
        if (attemptsL == null)
            throw new UnirestException("");
        int id = attemptsL.get(0).id;
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

    public static void setLoginAndPassword(String login, String password, Project project) {
        StudentService storage = StudentService.getInstance(project);
        storage.setLoginAndPassword(login, password);
    }

    public static String getLogin(Project project) {
        return StudentService.getInstance(project).getLogin();
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

    public static List<Submission> getSubmissions(String stepId, Project project) {
        StudentService storage = StudentService.getInstance(project);
        Map<String, Object> map = new HashMap<>();
        map.put("step", stepId);
        try {
            return getFromStepic("submissions", map, SubmissionsContainer.class, storage.getToken()).submissions;
        } catch (UnirestException e) {
//            LOG.error("getSubmissions error " + e.getMessage());
            initConnectionError(project);
            return new ArrayList<>();
        }
    }

    public static String getToken(Project project) {
        StudentService storage = StudentService.getInstance(project);
        long baseTime = storage.getTokenTimeCreate();

        if (!timePassedLessThen(baseTime, new Date().getTime(), 32400L)) { // 9 hours in sec
            try {
                StepicConnector.initToken(project);
            } catch (UnirestException e) {
                StepicConnector.initConnectionError(project);
            }
        }
        return storage.getToken();
    }

    private static boolean timePassedLessThen(long base, long current, long sec) {
        long delta = current - base;
        return delta - sec * 1000L < 0L;
    }

    public static void initConnectionError(Project project){
        Notification notification = new Notification("Unirest.exception", "Stepic authorization error", "Please check your internet configuration.", NotificationType.WARNING);
        notification.notify(project);
    }
}

