package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * Created by Petr on 11.06.2016.
 */
public class Help extends MainMenuAction {

    private static String text = "Stepic plugin for code challenges on Java.\n" +
            "\n" +
            "To work with plugin you must to create a project of Stepic type.\n" +
            "You need to enter login and password your Stepic account.\n" +
            "And then input course URL or course number.\n" +
            "After twenty seconds course will be built.\n" +
            "\n" +
            "To send task click the left mouse button on file. In the shortcut menu choose \"Send step\".\n" +
            "In the same way you can to download last submissions and get the status of step.\n" +
            "\n" +
            "\"Who am I\" -- returns your user_name.\n" +
            "\"Update course\" -- download new lessons.\n" +
            "\n\n--------------------------------------\n\n" +
            "Stepic плагин для решения задач на программирование на языке Java.\n" +
            "\n" +
            "Для работы с плагином необходимо создать проект типа Stepic.\n" +
            "Ввести логин и пароль от аккаунта.\n" +
            "Затем ввести ссылку на курс или его номер.\n" +
            "Через двадцать секунд проект будет построен.\n" +
            "\n" +
            "Для отправки задачи кликните левой кнопкой мыши на файле. В контекстном меню выберете \"Send step\".\n" +
            "Тем же способом вы можете загрузить последний отправленный вариант или узнать статус задачи.\n" +
            "\n" +
            "\"Who am I\" -- возвращает user_name.\n" +
            "\"Update course\" -- загружает новые уроки.\n";
    @Override
    public void actionPerformed(AnActionEvent e) {

        Messages.showMessageDialog(e.getProject(), text, "Information", Messages.getInformationIcon());
    }
}
