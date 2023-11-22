package org.freedombrowser;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
public class Config {
    private static INIConfiguration config;

    static {
        try {
            // Load the INI file
            Configurations configs = new Configurations();
            config = configs.ini("config.ini");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getConfigValue(String section, String property) {
        if (config == null) {
            return null; // or throw an exception, handle accordingly
        }
        SubnodeConfiguration sectionConfig = config.getSection(section);
        if (sectionConfig == null) {
            return null;
        }
        return sectionConfig.getString(property);
    }
}
