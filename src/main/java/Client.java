import com.squareup.okhttp.OkHttpClient;
import org.json.JSONObject;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        final String client_id = "XeDsIRtCBYdvEnShTtMw6quDO3aVlEgnvy27Tgeg";
        final String client_secret = "661DX7ctAyraWeJNQhbVrMgeSpZb40Omzee4e" +
                "TETRwFI7BzsfKOIdHFHRcQtIEL6wOnncqw4ztNvwjtoe1KX2JkC7TYbF1zPPxRezt0D9fZLd1FaAgf2NdK1VJoMiuPp";
        String token = null;

        OkHttpClient client = new OkHttpClient();

        try {
            token = Commands.doAuth(client, client_id, client_secret);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(token);

        JSONObject js = null;
        try {
            js = Commands.test(client, "courses/", token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(js.toString());

    }

}
