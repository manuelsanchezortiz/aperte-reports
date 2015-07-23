package org.apertereports.common.xml.config;

import pl.net.bluesoft.util.lang.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apertereports.common.xml.XmlHelper;

public class XmlReportConfigLoader extends XmlHelper {
    private static final XmlReportConfigLoader instance = new XmlReportConfigLoader();

    protected Class[] getSupportedClasses() {
        return new Class[] {
                ReportConfigRoot.class,
                CyclicReportConfig.class,
                ReportConfig.class,
                ReportConfigParameter.class,
        };
    }

    public static XmlReportConfigLoader getInstance() {
        return instance;
    }

    /**
     * Converts a list of {@link ReportConfig} to a single string.
     * Basically reverts the result of {@link #stringAsReportConfigs(String)}.
     *
     * @param reportConfigs A list of {@link ReportConfig}
     * @return The XML representation converted to a string
     * @see ReportConfig
     */
    public String reportConfigsAsString(List<ReportConfig> reportConfigs) {
        return reportConfigs != null && !reportConfigs.isEmpty() ? marshall(new ReportConfigRoot(reportConfigs)) : "";
    }

    /**
     * Converts a string containing XML to a list of {@link ReportConfig}.
     * Basically reverts the result of {@link #reportConfigsAsString(java.util.List)}.
     *
     * @param input
     * @return A list of report configs
     */
    public List<ReportConfig> stringAsReportConfigs(String input) {
        ReportConfigRoot root = StringUtil.hasText(input) ? (ReportConfigRoot) unmarshall(input) : null;
        return root != null ? root.getReportConfigs() : new ArrayList<ReportConfig>();
    }

    /**
     * Converts a list of {@link ReportConfigParameter} to a single string.
     * Basically reverts the result of {@link #xmlAsParameters(String)}.
     *
     * @param parameters A list of {@link ReportConfigParameter}
     * @return The XML representation converted to a string
     * @see ReportConfigParameter
     */
    public String parametersAsXML(List<ReportConfigParameter> parameters) {
        return parameters != null && !parameters.isEmpty() ? marshall(new CyclicReportConfig(parameters)) : "";
    }

    /**
     * Converts a string containing XML to a list of {@link ReportConfigParameter}.
     * Basically reverts the result of {@link #parametersAsXML(java.util.List)}.
     *
     * @param xml A string with XML
     * @return A list of {@link ReportConfigParameter}
     * @see ReportConfigParameter
     */
    public List<ReportConfigParameter> xmlAsParameters(String xml) {
        CyclicReportConfig root = StringUtil.hasText(xml) ? (CyclicReportConfig) getInstance().unmarshall(xml) : null;
        return root != null ? root.getParameters() : new ArrayList<ReportConfigParameter>();
    }

    /**
     * Converts a string containing XML representation of {@link ReportConfigParameter} to a map.
     * The key of the map is the parameter's name. The value is {@link org.apertereports.common.xml.config.ReportConfigParameter#getValue()}.
     * Reverts the result of {@link #mapAsXml(java.util.Map)}.
     *
     * @param xml A string with XML
     * @return A map of parameters
     */
    public Map<String, Object> xmlAsMap(String xml) {
        return parameterListToMap(xmlAsParameters(xml));
    }

    /**
     * Converts a map of parameters to a marshaled string containing XML representation of a list of {@link ReportConfigParameter}.
     * Reverts the result of {@link #xmlAsMap(String)}.
     *
     * @param map A map of parameters
     * @return A string with XML
     */
    public String mapAsXml(Map<String, Object> map) {
        return parametersAsXML(mapToParameterList(map));
    }

    /**
     * Converts a map of parameters to a list of {@link ReportConfigParameter}.
     * Reverts the result of {@link #parameterListToMap(java.util.List)}.
     *
     * @param map A map of parameters
     * @return A list of {@link ReportConfigParameter}
     */
    public List<ReportConfigParameter> mapToParameterList(Map<String, Object> map) {
    	
    	//TODO Update map to use value as an Object so we can store groupId. 
    	//so this needs to be able to store more things to run fine
    	
        List<ReportConfigParameter> list = new ArrayList<ReportConfigParameter>(map.values().size());
        for (Entry<String, Object> e : map.entrySet()) {
            ReportConfigParameter p = new ReportConfigParameter();
            p.setName(e.getKey());
            p.setValue(e.getValue() == null ? null : e.getValue().toString() );
            list.add(p);
        }
        return list;
    }

    /**
     * Converts a list of {@link ReportConfigParameter} to a map.
     * Reverts the result of {@link #mapToParameterList(java.util.Map)}.
     *
     * @param params A list of parameters
     * @return A map of parameters
     */
    public Map<String, Object> parameterListToMap(List<ReportConfigParameter> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (params != null && !params.isEmpty()) {
            for (ReportConfigParameter config : params) {
            	//TODO value can be object in mapToParameterList see there, so
            	//here we need reconversion
                map.put(config.getName(), config.getValue());
            }
        }
        return map;
    }
}
