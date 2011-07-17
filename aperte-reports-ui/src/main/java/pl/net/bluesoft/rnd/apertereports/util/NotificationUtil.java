package pl.net.bluesoft.rnd.apertereports.util;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import eu.livotov.tpt.i18n.TM;
import pl.net.bluesoft.rnd.apertereports.components.SimpleHorizontalLayout;
import pl.net.bluesoft.rnd.apertereports.exception.VriesException;

import static com.vaadin.ui.Window.Notification.*;

/**
 * @author MW
 */
public class NotificationUtil {
    public static void notImplementedYet(Window window) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("exception.not.implemented.title"),
                pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("exception.not.implemented.description"), TYPE_WARNING_MESSAGE);
    }

    public static void showValidationErrors(Window window, String message) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("notification.validation.errors.title"), "<br/>" + message, TYPE_ERROR_MESSAGE);
    }

    public static void showExceptionNotification(Window window, String prefix) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".title"), "<br/>" + pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".description"), TYPE_ERROR_MESSAGE);
    }

    public static void showExceptionNotification(Window window, String prefix, Exception e) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".title"),
                "<br/>" + pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".description") + ": " + e.getLocalizedMessage(), TYPE_ERROR_MESSAGE);
    }

    public static void showExceptionNotification(Window window, String prefix, Object... details) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".title"), "<br/>" + pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".description", details),
                TYPE_ERROR_MESSAGE);
    }

    public static void showExceptionNotification(Window window, String title, String message) {
        window.showNotification(title, "<br/>" + message, TYPE_ERROR_MESSAGE);
    }

    public static void showExceptionNotification(Window window, VriesException exception) {
        showExceptionNotification(window, exception.getTitle(), "<br/>" + exception.getLocalizedMessage());
    }

    public static void validationErrors(Window window) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("exception.validation.errors.title"), "<br/>" +
                pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("exception.validation.errors.description"), TYPE_WARNING_MESSAGE);
    }

    public static void validationErrors(Window window, String message) {
        window.showNotification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("exception.validation.errors.title"), "<br/><b>" + message + "</b>",
                TYPE_ERROR_MESSAGE);
    }

    public static void showSavedNotification(Window window) {
        showConfirmNotification(window, "notification.data.saved");
    }

    public static void showCancelledNotification(Window window) {
        showConfirmNotification(window, "notification.data.cancelled");
    }

    public static void showConfirmNotification(Window window, String prefix) {
        Notification notification = new Notification(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".title"), "<br/><b>" +
                pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue(prefix + ".desc") + "</b>", TYPE_HUMANIZED_MESSAGE);
        notification.setPosition(POSITION_CENTERED);
        notification.setDelayMsec(2000);
        window.showNotification(notification);
    }

    public static void showValuesChangedWindow(final Window parent, final ConfirmListener listener) {
        showConfirmWindow(parent, pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("report.table.valuesChanged.title"), pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("report.table.valuesChanged.content"), listener);
    }

    public static void showConfirmWindow(final Window parent, String windowTitle, String message, final ConfirmListener listener) {
        final Window window = new Window(windowTitle);
        window.setModal(true);
        window.setWidth(300, Sizeable.UNITS_PIXELS);
        window.setResizable(false);

        Button confirmButton = new Button(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("report.table.confirm"));
        confirmButton.setImmediate(true);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                listener.onConfirm();
                parent.removeWindow(window);
            }
        });

        Button cancelButton = new Button(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("report.table.notconfirm"));
        cancelButton.setImmediate(true);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                listener.onCancel();
                parent.removeWindow(window);
            }
        });

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new Label(message, Label.CONTENT_XHTML));
        mainLayout.addComponent(new SimpleHorizontalLayout(confirmButton, cancelButton));

        window.setContent(mainLayout);
        parent.addWindow(window);
    }

    public static interface ConfirmListener {
        void onConfirm();

        void onCancel();
    }
}
