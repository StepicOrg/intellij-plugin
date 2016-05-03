package main.stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.edu.stepic.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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
    private static final String api_url = "http://stepic.org/api/";

    private static final Logger LOG = Logger.getInstance("#main.stepicConnector.StepicConnector.java");
    private static boolean tokenInit = false;


    public static void init() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
// Create a trust manager that does not validate certificate for this connection
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
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
                init();
                tokenInit = true;
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException e) {
                LOG.error(e);
            }
        }
        WorkerService.getInstance().setToken(
                getToken(WorkerService.getInstance().clientId, WorkerService.getInstance().clientSecret));
    }

    public static String getToken(String user, String pass) {
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
        final String url = "courses/" +courseId;
        try {
            return getFromStepic(url, CoursesContainer.class).courses.get(0);
        } catch (UnirestException e) {
            LOG.error("getCourse error");
            return null;
        }
    }

    public static MySection getSection(String sectionId) {
        final String url = "sections/" + sectionId;
        try {
            return getFromStepic(url, SectionsContainer.class).sections.get(0);
        } catch (UnirestException e) {
            LOG.error("getSection error");
            return null;
        }
    }

    public static MyUnit getUnit(String sectionId) {
        final String url = "units/" + sectionId;
        try {
            return getFromStepic(url, UnitsContainer.class).units.get(0);
        } catch (UnirestException e) {
            LOG.error("getUnit error");
            return null;
        }
    }

    public static MyLesson getLesson(String lessonId)  {
        final String url = "lessons/" + lessonId;
        try {
            return getFromStepic(url, LessonsContainer.class).lessons.get(0);
        } catch (UnirestException e) {
            LOG.error("getLesson error");
            return null;
        }
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

}

