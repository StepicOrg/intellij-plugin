//package test;

import javafx.util.Pair;
import main.stepicConnector.Commands;
import main.stepicConnector.WorkWithProperties;
import org.json.JSONException;

public class Client {

    public static void main(String[] args) {
        final String client_id;
        final String client_secret;
        String token = null;

        Pair<String, String> pair = WorkWithProperties.getProperties();
        client_id = pair.getKey();
        client_secret = pair.getValue();

//        System.out.println(client_id);
//        System.out.println(client_secret);

        try {
            token = Commands.getToken(client_id, client_secret);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(token);

//        Commands.AuthorWrapper aw = Commands.getCurrentUser();
//        for (CourseInfo.Author i : aw.users){
//            System.out.println(i.getName());
//        }
        System.out.println(Commands.getUserName());
        System.out.println(Commands.getUserName());

//        List<CourseInfo> list = Commands.getCourses();
//        for (CourseInfo i : list){
//            System.out.println("Name: "+i.getName());
//            System.out.print("Authors: ");
//            for (CourseInfo.Author ii : i.getAuthors()){
//                System.out.print("\t"+ii.getName());
//            }
//            System.out.println();
//            System.out.println("Description: "+i.getDescription());
//            System.out.println("Type: "+i.getType());
//            System.out.println();
//        }
    }

}
