package org.apertereports.backbone.util;

import java.util.Map;
import org.apertereports.common.xml.config.XmlReportConfigLoader;
import org.apertereports.dao.ReportOrderDAO;
import org.apertereports.dao.utils.WHS;
import org.apertereports.model.ReportOrder;
import org.apertereports.model.ReportTemplate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class for building report orders
 */
public final class ReportOrderBuilder {

    private ReportOrderBuilder() {
    }

    /**
     * Builds a new report order with given parameters. Returns
     * <code>null</code> when the paramters cannot be converted to an XML
     * representation or another report order with equal configuration already
     * exists in database.
     *
     * @param report A Jasper report template
     * @param parameters Report paramters
     * @param format Output format
     * @param recipientEmail A recipient email address that should receive the
     * report generation result
     * @param username Order creator login
     * @param replyToJms Indicates whether reply to JMS in order to process
     * generated report
     * @return A configured report order
     */
    public static ReportOrder build(final ReportTemplate report, Map<String, Object> parameters, String format,
            String recipientEmail, String username, boolean replyToJms) {
        final ReportOrder reportOrder = new ReportOrder();
        reportOrder.setParametersXml(XmlReportConfigLoader.getInstance().mapAsXml(parameters));
        reportOrder.setOutputFormat(format);
        reportOrder.setRecipientEmail(recipientEmail);
        reportOrder.setUsername(username);
        reportOrder.setReport(report);
        reportOrder.setReplyToJms(replyToJms);
        Boolean alreadyExists = new WHS<Boolean>() {
            @Override
            public Boolean lambda() {
                Criteria c = sess.createCriteria(ReportOrder.class);
                c.add(Restrictions.eq("report", report));
                //oracle doesn't allow to compare LOBs columns
                //c.add(Restrictions.eq("parametersXml", reportOrder.getParametersXml()));
                c.add(Restrictions.eq("outputFormat", reportOrder.getOutputFormat()));
                c.add(Restrictions.eq("username", reportOrder.getUsername()));
                c.add(Restrictions.eq("recipientEmail", reportOrder.getRecipientEmail()));
                c.add(Restrictions.eq("replyToJms", reportOrder.getReplyToJms()));
                c.add(Restrictions.isNull("reportResult"));
                return !c.list().isEmpty();
            }
        }.p();
        if (alreadyExists) {
            return null;
        }
        Long id = ReportOrderDAO.saveOrUpdate(reportOrder);
        reportOrder.setId(id);

        return reportOrder;
    }
}
