package org.stepik.plugin.stepikConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.util.net.HttpConfigurable;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.stepik.plugin.storages.StudentStorage;
import org.stepik.plugin.utils.NotificationTemplates;
import org.stepik.plugin.utils.NotificationUtils;
import org.stepik.plugin.modules.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepikConnector {

    private static final String TOKEN_URL = "https://stepik.org/oauth2/token/";
    private static final String API_URL = "https://stepik.org/api/";
    private static final String CLIENT_ID = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";

    private static final Logger logger = Logger.getInstance(StepikConnector.class);
    private static boolean isInstanceProperty = false;
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static void setSSLProperty(Project project) {
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
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            NotificationUtils.initRuntimeException(NotificationTemplates.CERTIFICATE_ERROR, project);
        }

        HttpConfigurable instance = HttpConfigurable.getInstance();
        CloseableHttpClient httpClient;

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        if (instance.USE_HTTP_PROXY) {
            HttpHost host = new HttpHost(instance.PROXY_HOST, instance.PROXY_PORT);
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .setProxy(host)
                    .build();
        } else {
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build();
        }

        Unirest.setHttpClient(httpClient);
    }

    public static void initToken(Project project) {
        if (!isInstanceProperty) {
            setSSLProperty(project);
            isInstanceProperty = true;
        }

        setTokenGRP(project);
    }

    private static void setTokenGRP(Project project) {
        StudentStorage ws = StudentStorage.getInstance(project);
        String user = ws.getLogin();
        String pass = ws.getPassword();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("username", user);
        parameters.put("password", pass);
        parameters.put("client_id", CLIENT_ID);

        TokenInfo tokenInfo = postToStepikMapLinkReset(TOKEN_URL, parameters, TokenInfo.class, project);

        String token = tokenInfo.access_token;
        if (token != null && !token.isEmpty()) {
            ws.setToken(tokenInfo.access_token);
            ws.setRefresh_token(tokenInfo.refresh_token);
        }
    }

    private static <T> T getFromStepik(String link, final Class<T> container, Project project) {

        String token = getToken(project);
        HttpResponse<String> response = null;
        try {
            response = Unirest
                    .get(API_URL + link)
                    .header("Authorization", "Bearer " + token)
                    .asString();
        } catch (UnirestException e) {
            NotificationUtils.initRuntimeException(project, e.getMessage());
        }

        if (response.getStatus() != 200) {
            NotificationUtils.initRuntimeException(project, "Status " + Integer.toString(response.getStatus()));
        }
        final String responseString = response.getBody();

        return GSON.fromJson(responseString, container);
    }

    private static <T> T getFromStepik(String link, Map<String, Object> queryMap, final Class<T> container, Project project) {

        String token = getToken(project);
        HttpResponse<String> response = null;
        try {
            response = Unirest
                    .get(API_URL + link)
                    .header("Authorization", "Bearer " + token)
                    .queryString(queryMap)
                    .asString();
        } catch (UnirestException e) {
            NotificationUtils.initRuntimeException(project);
        }
        final String responseString = response.getBody();

        return GSON.fromJson(responseString, container);
    }

    private static <T> T postToStepik(String link, JSONObject jsonObject, final Class<T> container, Project project) {
        HttpResponse<JsonNode> response = null;
        String token = getToken(project);
        try {
            response = Unirest
                    .post(API_URL + link)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(jsonObject)
                    .asJson();
        } catch (UnirestException e) {
            NotificationUtils.initRuntimeException(project);
        }
        final JSONObject responseJson = response.getBody().getObject();

        return GSON.fromJson(responseJson.toString(), container);
    }

    private static <T> T postToStepikMapLinkReset(String link, Map<String, Object> parameters, final Class<T> container, Project project) {
        HttpResponse<String> response = null;
        try {
            response = Unirest
                    .post(link)
                    .fields(parameters)
                    .asString();
        } catch (UnirestException e) {
            NotificationUtils.initRuntimeException(project);
        }
        final String responseString = response.getBody();

        return GSON.fromJson(responseString, container);
    }

    public static AuthorWrapper getCurrentUser(Project project) {
        return getFromStepik("stepiks/1", AuthorWrapper.class, project);
    }

    public static String getUserName(Project project) {
        AuthorWrapper aw = getCurrentUser(project);
        return aw.users.get(0).getName();
    }

    public static Course getCourse(String courseId, Project project) {
        final String url = "courses/" + courseId;
        return getFromStepik(url, CoursesContainer.class, project).courses.get(0);
    }

    public static List<Section> getSections(String idsQuery, Project project) {
        final String url = "sections" + idsQuery;
        return getFromStepik(url, SectionsContainer.class, project).sections;
    }

    public static List<Unit> getUnits(String idsQuery, Project project) {
        final String url = "units" + idsQuery;
        return getFromStepik(url, UnitsContainer.class, project).units;
    }

    public static List<Lesson> getLessons(String idsQuery, Project project) {
        final String url = "lessons" + idsQuery;
        return getFromStepik(url, LessonsContainer.class, project).lessons;
    }

    public static List<Submission> getStatus(String submissionID, Project project) {
        final String url = "submissions/" + submissionID;
        return getFromStepik(url, SubmissionsContainer.class, project).submissions;
    }

    public static List<Step> getSteps(String stepIdQuery, Project project) {
        final String url = "steps" + stepIdQuery;
        return getFromStepik(url, StepsContainer.class, project).steps;
    }

    public static String getAttemptId(String stepId, Project project) {
        String attempts = "attempts";
        JSONObject first = new JSONObject();
        JSONObject second = new JSONObject();

        first.put("step", stepId);
        second.put("attempt", first);

        List<Attempt> attemptsL = postToStepik(attempts, second, AttemptsContainer.class, project).attempts;
        if (attemptsL == null) {
            NotificationUtils.initRuntimeException(project);
        }
        int id = attemptsL.get(0).id;
        return Integer.toString(id);
    }

    public static String sendFile(String file, String att_id, Project project) {

        JSONObject wrapper = new JSONObject();
        JSONObject submission = new JSONObject();
        JSONObject reply = new JSONObject();

        reply.put("language", "java8");
        reply.put("code", file);

        submission.put("attempt", att_id);
        submission.put("reply", reply);

        wrapper.put("submission", submission);

        int id = postToStepik("submissions", wrapper, SubmissionsContainer.class, project).submissions.get(0).id;
        return Integer.toString(id);
    }

    public static List<Submission> getStepStatus(String stepId, Pair<String, String> pair, Project project) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("step", stepId);
        queryMap.put(pair.first, pair.second);

        return getFromStepik("submissions", queryMap, SubmissionsContainer.class, project).submissions;
    }

    public static void setLoginAndPassword(String login, String password, Project project) {
        StudentStorage storage = StudentStorage.getInstance(project);
        storage.setLoginAndPassword(login, password);
    }

    public static String getLogin(Project project) {
        return StudentStorage.getInstance(project).getLogin();
    }

    public static void setLogin(String login, Project project) {
        StudentStorage.getInstance(project).setLogin(login);
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
        Map<String, Object> map = new HashMap<>();
        map.put("step", stepId);
        return getFromStepik("submissions", map, SubmissionsContainer.class, project).submissions;
    }

    public static String getToken(Project project) {
        StudentStorage storage = StudentStorage.getInstance(project);
        long baseTime = storage.getTokenTimeCreate();

        if (!timePassedLessThen(baseTime, new Date().getTime(), 32400L)) { // 9 hours in sec
            StepikConnector.initToken(project);
        }
        return storage.getToken();
    }

    private static boolean timePassedLessThen(long base, long current, long sec) {
        long delta = current - base;
        return delta - sec * 1000L < 0L;
    }

    public static boolean isPasswordSet(Project project) {
        StudentStorage storage = StudentStorage.getInstance(project);
        String password = storage.getPassword();
        return !(password == null || password.isEmpty());
    }
}

