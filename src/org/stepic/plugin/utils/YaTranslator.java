package org.stepic.plugin.utils;

import com.intellij.openapi.project.Project;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class YaTranslator {
    final static private String API_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    final static private String APP_KEY = "trnsl.1.1.20160514T085113Z.7ee4b2a3baf18163.933b409b8a7ee466edcb2682f2cec46eadb65f5e";
    final static private String TEXT = "text";
    final static private String KEY = "key";
    final static private String LANG = "lang";
    final static private String EN_RU = "en-ru";
    final static private String RU_EN = "ru-en";

    public static String translateRuToEn(String text) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .post(API_URL)
                    .field(KEY, APP_KEY)
                    .field(LANG, RU_EN)
                    .field(TEXT, text)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return (String) response.getBody().getObject().getJSONArray(TEXT).get(0);
    }

    public static JSONArray translateRuToEn(List<String> list) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .post(API_URL)
                    .field(KEY, APP_KEY)
                    .field(LANG, RU_EN)
                    .field(TEXT, list)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return response.getBody().getObject().getJSONArray(TEXT);
    }

    public static List<String> translateNames(List<String> names, String level, Project project) {
        CourseDefinitionStorage ws = CourseDefinitionStorage.getInstance(project);
        List<String> ans = new ArrayList<>();
        if (ws.isTranslate()) {
            JSONArray arr = YaTranslator.translateRuToEn(names);
            for (int i = 0; i < names.size(); i++) {
                ans.add("_" + (i + 1) + "_" + Utils.normalize(arr.getString(i)));
            }
        } else {
            for (int i = 0; i < names.size(); i++) {
                ans.add(level + (i + 1));
            }
        }
        return ans;
    }
}
