package StepicConnector;

import Stepic.CourseInfo;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONException;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        final String client_id = "XeDsIRtCBYdvEnShTtMw6quDO3aVlEgnvy27Tgeg";
        final String client_secret = "661DX7ctAyraWeJNQhbVrMgeSpZb40Omzee4e" +
                "TETRwFI7BzsfKOIdHFHRcQtIEL6wOnncqw4ztNvwjtoe1KX2JkC7TYbF1zPPxRezt0D9fZLd1FaAgf2NdK1VJoMiuPp";
        String token = null;

        OkHttpClient client = new OkHttpClient();

        try {
            token = Commands.getToken(client_id, client_secret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(token);

//        JSONObject js = null;
//        try {
//            js = Commands.test("courses/", token);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(js.toString());

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Commands.AuthorWrapper aw = Commands.getCurrentUser();
        for (CourseInfo.Author i : aw.users){
            System.out.println(i.getName());
        }
//        System.out.println(aw);

    }

}
