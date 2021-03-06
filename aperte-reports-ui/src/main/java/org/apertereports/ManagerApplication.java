package org.apertereports;

import com.vaadin.ui.Window;
import org.apertereports.common.users.User;
import org.apertereports.components.ReportManagerComponent;

/**
 * This is the main report administration portlet.
 * <p/>
 * A user can add new, modify or delete existing report templates from the
 * application. The reports are displayed in a table, providing information
 * about the name of the report, description and permissions whether to let
 * users generate in the background or directly. It is also possible to disable
 * a report so that is no longer available in the dashboards, but remains in the
 * database.
 * <p/>
 * To add a new report one needs to upload a JRXML Jasper template using the
 * panel on the right of the table. Once the report is uploaded one can
 * configure the permissions and generate a sample report for checking.
 */
public class ManagerApplication extends AbstractReportingApplication<ReportManagerComponent> {

    /**
     * Initializes the portlet GUI.
     */
    @Override
    public void portletInit() {
        mainPanel = new ReportManagerComponent();
        mainWindow = new Window("", mainPanel);
    }

    @Override
    protected void reinitUserData(User user) {
        mainPanel.initData(user);
    }
}
