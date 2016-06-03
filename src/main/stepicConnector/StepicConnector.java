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
import java.util.List;
import java.util.Map;

public class StepicConnector {

    private static final String token_url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "https://stepic.org/api/";

    private static final Logger LOG = Logger.getInstance(StepicConnector.class);
    private static boolean tokenInit = false;

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
//        WorkerService.getInstance().setToken(
//                getToken(WorkerService.getInstance().getClientId(), WorkerService.getInstance().getClientSecret()));
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
        WorkerService ws = WorkerService.getInstance();
        String user = ws.getUsername();
        String pass = ws.getPassword();
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest
                    .post(token_url)
                    .field("grant_type", "password")
                    .field("username", user)
                    .field("password", pass)
                    .field("client_id", WorkerService.getInstance().getClientId())
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

        HttpResponse<String> response = null;
        response = Unirest
                .get(api_url + link)
                .header("Authorization", "Bearer " + WorkerService.getInstance().getToken())
                .asString();
        final String responseString = response.getBody();

//        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
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

//    public static List<CourseInfo> getCourses() {
//        try {
//            List<CourseInfo> result = new ArrayList<CourseInfo>();
//            int pageNumber = 1;
//            while (addCoursesFromStepic(result, pageNumber)) {
//                pageNumber += 1;
//            }
//            return result;
//        } catch (UnirestException e) {
//            LOG.error("Cannot load course list " + e.getMessage());
//        }
//        return Collections.emptyList();
//    }

//    private static boolean addCoursesFromStepic(List<CourseInfo> result, int pageNumber) throws UnirestException {
//        final String url = pageNumber == 0 ? "courses" : "courses?page=" + String.valueOf(pageNumber);
//        final CoursesContainer coursesContainer = getFromStepic(url, CoursesContainer.class);
//        final List<CourseInfo> courseInfos = coursesContainer.courses;
//        for (CourseInfo info : courseInfos) {
//
//            for (Integer instructor : info.instructors) {
//                final CourseInfo.Author author = getFromStepic("users/" + String.valueOf(instructor), AuthorWrapper.class).users.get(0);
//                info.addAuthor(author);
//            }
//
//            result.add(info);
//        }
//        return coursesContainer.meta.containsKey("has_next") && coursesContainer.meta.get("has_next") == Boolean.TRUE;
//    }

    public static MyCourse getCourse(String courseId) {
        final String url = "courses/" + courseId;
        try {
            return getFromStepic(url, CoursesContainer.class).courses.get(0);
        } catch (UnirestException e) {
            LOG.error("getCourse error " + e.getMessage());
            return null;
        }
    }

    public static MySection getSection(String sectionId) {
        final String url = "sections/" + sectionId;
        try {
            return getFromStepic(url, SectionsContainer.class).sections.get(0);
        } catch (UnirestException e) {
            LOG.error("getSection error " + e.getMessage());
            return null;
        }
    }

    public static MyUnit getUnit(String sectionId) {
        final String url = "units/" + sectionId;
        try {
            return getFromStepic(url, UnitsContainer.class).units.get(0);
        } catch (UnirestException e) {
            LOG.error("getUnit error " + e.getMessage());
            return null;
        }
    }

    public static MyLesson getLesson(String lessonId) {
        final String url = "lessons/" + lessonId;
        try {
            return getFromStepic(url, LessonsContainer.class).lessons.get(0);
        } catch (UnirestException e) {
            LOG.error("getLesson error " + e.getMessage());
            return null;
        }
    }

    public static MyStep getStep(String stepId) {
        final String url = "steps/" + stepId;
        try {
            return getFromStepic(url, StepsContainer.class).steps.get(0);
        } catch (UnirestException e) {
            LOG.error("getStep error " + e.getMessage());
            return null;
        }
    }

    public static String getAttemptId(String stepId) {
        WorkerService ws = WorkerService.getInstance();
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

    public static String sendFile(String file, String att_id) {
        WorkerService ws = WorkerService.getInstance();

        JSONObject waper = new JSONObject();
        JSONObject submission = new JSONObject();
        JSONObject reply = new JSONObject();

        reply.put("language", "java8");
        reply.put("code", file);

        submission.put("attempt", att_id);
        submission.put("reply", reply);

        waper.put("submission", submission);

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .post(api_url + "submissions")
                    .header("Authorization", "Bearer " + ws.getToken())
                    .header("Content-Type", "application/json")
                    .body(waper)
                    .asJson();
        } catch (UnirestException e) {
            LOG.error("Send File error\n" + e.getMessage());
        }
        JSONObject tmp = response.getBody().getObject();
        JSONArray oo = (JSONArray) tmp.get("submissions");
        return Integer.toString(oo.getJSONObject(0).getInt("id"));
    }

    public static String getStatusTask(String submissionId) {
        WorkerService ws = WorkerService.getInstance();
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
//        System.out.println(response.getStatus() + response.getStatusText());
    }

    public static Submissions getStatusTask(String stepId, Pair<String, String> pair) {
        WorkerService ws = WorkerService.getInstance();
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
        return gson.fromJson(response.getBody().getObject().toString(), Submissions.class);
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

    public static class Submissions {
        public List<SubmissionsNode> submissions;
        public Map meta;
    }

    public static Submissions getSubmissions(String stepId) {
        WorkerService ws = WorkerService.getInstance();
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .get(api_url + "submissions")
                    .header("Authorization", "Bearer " + ws.getToken())
                    .queryString("step", stepId)
                    .asJson();
        } catch (UnirestException e) {
            LOG.error("get Submissions error\n" + e.getMessage());
        }


        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(response.getBody().getObject().toString(), Submissions.class);
    }

}

