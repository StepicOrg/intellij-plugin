package main.projectWizard;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Petr on 14.05.2016.
 */
public class YaTranslator {
    final static private String api_url = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    final static private String app_key = "trnsl.1.1.20160514T085113Z.7ee4b2a3baf18163.933b409b8a7ee466edcb2682f2cec46eadb65f5e";
    final static private String TEXT = "text";
    final static private String KEY = "key";
    final static private String LANG = "lang";
    final static private String EN_RU = "en-ru";
    final static private String RU_EN = "ru-en";

    public static String translateRuToEn(String text) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .post(api_url)
                    .field(KEY, app_key)
                    .field(TEXT, text)
                    .field(LANG, RU_EN)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return (String) response.getBody().getObject().getJSONArray(TEXT).get(0);
    }
}
