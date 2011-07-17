package pl.net.bluesoft.rnd.apertereports;

import com.vaadin.ui.Window;
import eu.livotov.tpt.i18n.TM;
import pl.net.bluesoft.rnd.apertereports.dashboard.cyclic.CyclicReportsPanel;
import pl.net.bluesoft.rnd.apertereports.scheduler.CyclicReportOrderScheduler;

/**
 * This portlet provides the tabular view of the cyclic reports.
 * <p/>A user can add new, modify, delete and browse existing cyclic reports.
 * Each cyclic report is configured on top of an existing report template.
 * The moment it is generated is specified by a cron expression. Every change in the table
 * should be confirmed by pushing the "Save" button.
 */
public class CyclicReportsApplication extends AbstractReportingApplication {
    /**
     * Initializes the portlet GUI.
     */
    @Override
    public void portletInit() {
        CyclicReportsPanel panel = new CyclicReportsPanel();

        Window mainWindow = new Window(pl.net.bluesoft.rnd.apertereports.util.VaadinUtil.getValue("dashboard.edit.cyclicReports"), panel);

        setMainWindow(mainWindow);
    }

    /**
     * This method is used to make sure the cyclic reports scanner and scheduler is initialized at startup.
     */
    @Override
    public void firstApplicationStartup() {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                CyclicReportOrderScheduler.scanForCyclicReportOrders();
            }
        });
    }
}
