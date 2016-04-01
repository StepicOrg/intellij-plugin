package main.stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.edu.stepic.CourseInfo;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Commands {

    private static final String token_url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "http://stepic.org/api/";

    private static final Logger LOG = Logger.getInstance(Commands.class.getName());

    public static void initToken() {
        try {
            WorkerService.getInstance().setToken(
                    getToken(WorkerService.getInstance().clientId, WorkerService.getInstance().clientSecret));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getToken(String user, String pass) throws JSONException {
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest
                    .post(token_url)
                    .basicAuth(user, pass)
                    .field("grant_type", "client_credentials")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
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

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(responseString, container);
    }

    public static AuthorWrapper getCurrentUser() {
        try {
            return getFromStepic("stepics/1", AuthorWrapper.class);
        } catch (UnirestException e) {
            System.err.print("Couldn't get author info");
        }
        return null;
    }

    public static String getUserName() {
        AuthorWrapper aw = getCurrentUser();
        return aw.users.get(0).getName();
    }

    public static List<CourseInfo> getCourses() {
        try {
            List<CourseInfo> result = new ArrayList<CourseInfo>();
            int pageNumber = 1;
            while (addCoursesFromStepic(result, pageNumber)) {
                pageNumber += 1;
            }
            return result;
        } catch (UnirestException e) {
            LOG.error("Cannot load course list " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private static boolean addCoursesFromStepic(List<CourseInfo> result, int pageNumber) throws UnirestException {
        final String url = pageNumber == 0 ? "courses" : "courses?page=" + String.valueOf(pageNumber);
        final CoursesContainer coursesContainer = getFromStepic(url, CoursesContainer.class);
        final List<CourseInfo> courseInfos = coursesContainer.courses;
        for (CourseInfo info : courseInfos) {

            for (Integer instructor : info.instructors) {
                final CourseInfo.Author author = getFromStepic("users/" + String.valueOf(instructor), AuthorWrapper.class).users.get(0);
                info.addAuthor(author);
            }

            result.add(info);
//            }
        }
//        return false;
        return coursesContainer.meta.containsKey("has_next") && coursesContainer.meta.get("has_next") == Boolean.TRUE;
    }

    public static class AuthorWrapper {
        public List<CourseInfo.Author> users;
    }

    private static class CoursesContainer {
        public List<CourseInfo> courses;
        public Map meta;
    }

}

