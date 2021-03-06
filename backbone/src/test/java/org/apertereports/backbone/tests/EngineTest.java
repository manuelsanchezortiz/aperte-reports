//package org.apertereports.backbone.tests;
//
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRExporter;
//import net.sf.jasperreports.engine.JRExporterParameter;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.export.JRHtmlExporter;
//import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
//import org.apache.commons.codec.binary.Base64;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.apertereports.common.ReportConstants;
//import org.apertereports.common.exception.ReportException;
//import org.apertereports.common.exception.SubreportNotFoundException;
//import org.apertereports.common.utils.ExceptionUtils;
//import org.apertereports.common.utils.TextUtils;
//import org.apertereports.dao.ReportTemplateDAO;
//import org.apertereports.engine.AperteReport;
//import org.apertereports.engine.EmptySubreportProvider;
//import org.apertereports.engine.ReportMaster;
//import org.apertereports.model.ReportTemplate;
//
//import javax.naming.NamingException;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//public class EngineTest {
//
//	@BeforeClass
//	public static void initDB() throws NamingException, IllegalStateException, IOException {
//		TestUtil.initDB();
//	}
//
//	private String outputPath = "/tmp/jasper/";
//	private String imagesPath = outputPath + "images/";
//
//	private void generateReport(ReportConstants.ReportType type, String path, boolean htmlChart) throws IOException,
//			JRException, ReportException, SubreportNotFoundException {
//		String reportText = TextUtils.readTestFileToString(getClass().getResourceAsStream(path));
//		String reportData = new String(Base64.encodeBase64(reportText.getBytes("UTF-8")));
//		AperteReport report = ReportMaster.compileReport(reportData, null, new EmptySubreportProvider());
//		ReportMaster rm = new ReportMaster(report);
//		Map<String, String> parameters = new HashMap<String, String>();
//		parameters.put("r_max_order_id", "'10300'");
//
//		File output = new File(imagesPath);
//		if (!output.exists()) {
//			output.mkdirs();
//		}
//		output = new File(outputPath);
//		if (!output.exists()) {
//			output.mkdirs();
//		}
//
//		JasperPrint print = rm.generateReport(new HashMap<String, Object>(parameters));
//		byte[] result;
//		if (htmlChart) {
//			result = exportReport(print);
//		} else {
//			result = ReportMaster.exportReport(print, type.name(), new HashMap<String, String>());
//		}
//
//		File file = new File(outputPath + "jasperprint_" + System.currentTimeMillis() + "." + type.name());
//		if (file.createNewFile()) {
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(result);
//			fos.close();
//		}
//		ExceptionUtils.logDebugMessage("File written: " + file.getPath());
//	}
//
//	private byte[] exportReport(JasperPrint jasperPrint) throws ReportException {
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			JRExporter exporter = new JRHtmlExporter();
//			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
//			exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
//			exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imagesPath);
//			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imagesPath);
//			exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
//
//			ArrayList<JasperPrint> list = new ArrayList<JasperPrint>();
//			list.add(jasperPrint);
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, list);
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
//			exporter.exportReport();
//
//			return bos.toByteArray();
//		} catch (JRException e) {
//			ExceptionUtils.logSevereException(e);
//			throw new ReportException(ReportConstants.ErrorCodes.JASPER_REPORTS_EXCEPTION, e.getMessage(), e);
//		}
//	}
//
//	@Test
//	public void testEngine() throws Exception {
//		Collection<ReportTemplate> reports = ReportTemplateDAO.fetchAllReports(true);
//
//        StringBuffer sb = new StringBuffer();
//        for (ReportTemplate rt : reports) {
//            try {
//                ReportMaster rm = new ReportMaster(rt.getContent(), rt.getId().toString(), new EmptySubreportProvider());
//                sb.append(new String(rm.generateAndExportReport(ReportConstants.ReportType.HTML.name(), new HashMap<String, Object>(),
//                        new HashMap<String, String>())));
//                if (!sb.toString().isEmpty()) {
//                    break;
//                }
//            }
//            catch (Exception e) {
//                ExceptionUtils.logSevereException(e);
//            }
//        }
//
//		ExceptionUtils.logDebugMessage(sb.toString());
//	}
//
//}
