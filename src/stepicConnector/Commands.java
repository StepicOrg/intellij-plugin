package stepicConnector;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.squareup.okhttp.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import stepic.CourseInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Commands {

    private static final String url = "https://stepic.org/oauth2/token/";
    private static final String api_url = "http://stepic.org/api/";
    private static final OkHttpClient client = new OkHttpClient();

    private static CloseableHttpClient ourClient = HttpClientBuilder.create().build();
    private static final Logger LOG = Logger.getInstance(Commands.class.getName());

    //    return String Token
    // OkHttp is bad
    @Deprecated
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
        JSONObject js = new JSONObject(res);


        String token = (String) js.get("access_token");
        WorkerService.getInstance().setToken(token);
        return token;
    }

    @Deprecated
    private static Request createAndSetHeader(String add_url, String token) {

        Request request = new Request.Builder()
                .url(api_url + add_url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        System.out.println(api_url + add_url);
        System.out.println(request.headers());
        return request;
    }

    private static <T> T getFromStepic(String link, final Class<T> container) throws IOException {

//        final Request request = createAndSetHeader(link, WorkerService.getInstance().getToken());
//        Response response = client.newCall(request).execute();
//        final String responseString = response.body() != null ? response.body().string() : "";

        final HttpGet request = new HttpGet(api_url + link);
        setHeaders(request, WorkerService.getInstance().getToken());

        final CloseableHttpResponse response = ourClient.execute(request);
        final HttpEntity responseEntity = response.getEntity();
        final String responseString = responseEntity != null ? EntityUtils.toString(responseEntity) : "";

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(responseString, container);
    }

    private static void setHeaders(@NotNull final HttpRequestBase request, String token) {
        request.addHeader(new BasicHeader("Authorization", "Bearer " + token));
    }


    public static AuthorWrapper getCurrentUser() {
        try {
            return getFromStepic("stepics/1", AuthorWrapper.class);
        } catch (IOException e) {
            System.err.print("Couldn't get author info");
        }
        return null;
    }

    public static List<CourseInfo> getCourses() {
        try {
            List<CourseInfo> result = new ArrayList<CourseInfo>();
            int pageNumber = 1;
            while (addCoursesFromStepic(result, pageNumber)) {
                pageNumber += 1;
            }
            return result;
        } catch (IOException e) {
            LOG.error("Cannot load course list " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private static boolean addCoursesFromStepic(List<CourseInfo> result, int pageNumber) throws IOException {
        final String url = pageNumber == 0 ? "courses" : "courses?page=" + String.valueOf(pageNumber);
        final CoursesContainer coursesContainer = getFromStepic(url, CoursesContainer.class);
        final List<CourseInfo> courseInfos = coursesContainer.courses;
        for (CourseInfo info : courseInfos) {
//            final String courseType = info.getType();
//            if (StringUtil.isEmptyOrSpaces(courseType)) continue;
//            final List<String> typeLanguage = StringUtil.split(courseType, " ");
//            if (typeLanguage.size() == 2 && PYCHARM_PREFIX.equals(typeLanguage.get(0))) {

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

