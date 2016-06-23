package org.stepic.plugin.stepicConnector;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;

public class NotificationTemplates {
    static final Notification CONNECTION_ERROR =
            new Notification("Connection.error", "Connection error", "Please check your internet configuration.", NotificationType.ERROR);

    static final Notification CERTIFICATE_ERROR =
            new Notification("Certificate.error", "Certificate error", "Please check your internet configuration.", NotificationType.ERROR);
}
