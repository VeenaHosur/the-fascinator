/* 
 * The Fascinator - Common Library
 * Copyright (C) 2008-2009 University of Southern Queensland
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package au.edu.usq.fascinator.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * Manages JSON configuration. Configuration values are read from a specified
 * JSON and if not found, the system-wide JSON will also be searched.
 * 
 * @author Oliver Lucido
 */
public class JsonConfig {

    /** Default configuration directory */
    private static final String CONFIG_DIR = System.getProperty("user.home")
            + File.separator + ".fascinator";

    /** Default system configuration file name */
    private static final String SYSTEM_CONFIG_FILE = "system-config.json";

    /** JSON system config */
    private JsonConfigHelper systemConfig;

    /** JSON user config */
    private JsonConfigHelper userConfig;

    /**
     * Creates a config with only the system settings
     * 
     * @throws IOException if these was an error parsing or reading the system
     *         JSON file
     */
    public JsonConfig() throws IOException {
        this((InputStream) null);
    }

    /**
     * Creates a JSON configuration from the specified file
     * 
     * @param jsonFile a JSON file
     * @throws IOException if there was an error parsing or reading the file
     */
    public JsonConfig(File jsonFile) throws IOException {
        this(new FileInputStream(jsonFile));
    }

    /**
     * Creates a JSON configuration from the specified input stream
     * 
     * @param jsonIn a JSON stream
     * @throws IOException if there was an error parsing or reading the stream
     */
    public JsonConfig(InputStream jsonIn) throws IOException {
        if (jsonIn == null) {
            userConfig = new JsonConfigHelper();
        } else {
            userConfig = new JsonConfigHelper(jsonIn);
        }
        systemConfig = new JsonConfigHelper(getSystemFile());
    }

    /**
     * Gets the value of the specified node
     * 
     * @param path XPath to node
     * @return node value or null if not found
     */
    public String get(String path) {
        return get(path, null);
    }

    /**
     * Gets the value of the specified node, with a specified default if the not
     * was not found
     * 
     * @param path XPath to node
     * @param defaultValue value to return if the node was not found
     * @return node value or defaultValue if not found
     */
    public String get(String path, String defaultValue) {
        String value = userConfig.get(path);
        if (value == null) {
            value = systemConfig.get(path, defaultValue);
        }
        return value;
    }

    /**
     * Gets values of the specified node as a list. Use this method for JSON
     * arrays.
     * 
     * @param path XPath to node
     * @return value list, possibly empty
     */
    public List<Object> getList(String path) {
        List<Object> valueList = userConfig.getList(path);
        if (valueList.isEmpty()) {
            valueList = systemConfig.getList(path);
        }
        return valueList;
    }

    /**
     * Gets a map of the child nodes of the specified node
     * 
     * @param path XPath to node
     * @return node map, possibly empty
     */
    public Map<String, Object> getMap(String path) {
        Map<String, Object> valueMap = userConfig.getMap(path);
        if (valueMap.isEmpty()) {
            valueMap = systemConfig.getMap(path);
        }
        return valueMap;
    }

    /**
     * Sets the value of the specified node. If the node doesn't exist it is
     * created. The value can be set in the system configuration if necessary.
     * 
     * @param path XPath to node
     * @param value value to set
     * @param system set the value in the system configuration
     */
    public void set(String path, String value, boolean system) {
        if (system) {
            systemConfig.set(path, value);
        } else {
            userConfig.set(path, value);
        }
    }

    /**
     * Serialises the current state of the JSON configuration to the specified
     * writer. By default this doesn't use a pretty printer.
     * 
     * @param writer a writer
     * @throws IOException if there was an error writing the configuration
     */
    public void store(Writer writer) throws IOException {
        store(writer, false);
    }

    /**
     * Serialises the current state of the JSON configuration to the specified
     * writer. The output can be set to be pretty printed if required.
     * 
     * @param writer a writer
     * @param pretty use pretty printer
     * @throws IOException if there was an error writing the configuration
     */
    public void store(Writer writer, boolean pretty) throws IOException {
        userConfig.store(writer, pretty);
	FileWriter sysWriter = new FileWriter(getSystemFile());
        systemConfig.store(sysWriter, pretty);
        sysWriter.close();
    }

    /**
     * Gets the system-wide configuration file from the default config dir. If
     * the file doesn't exist, a default is copied to the config dir.
     * 
     * @return the system JSON file
     * @throws IOException if there was an error reading or writing the system
     *         configuration file
     */
    public File getSystemFile() throws IOException {
        File configFile = new File(CONFIG_DIR, SYSTEM_CONFIG_FILE);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(configFile);
            IOUtils.copy(getClass().getResourceAsStream(
                    "/" + SYSTEM_CONFIG_FILE), out);
            out.close();
        }
        return configFile;
    }
}
