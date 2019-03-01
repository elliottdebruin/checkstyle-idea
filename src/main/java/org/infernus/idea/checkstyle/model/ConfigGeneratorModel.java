package org.infernus.idea.checkstyle.model;

import org.infernus.idea.checkstyle.util.ConfigReader;
import org.infernus.idea.checkstyle.util.ConfigWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigGeneratorModel {
    /** The current state of the configuration */
    XMLConfig config;

    /** The path the the config will be saved to when it is generated */
    private String path;

    /** The name of the configuration file */
    String configName;

    /** The set of rules that are currently in the configuration */
    private Set<ConfigRule> activeRules;

    /** The set of possible rules a user can add to the configuration */
    private Set<XMLConfig> rules;

    /**
     * Creates a new ConfigGeneratorModel with a blank XML configuration, file name,
     * path to the file, and set of active rules
     */
    public ConfigGeneratorModel() {
        this.path = "";
        this.configName = "";
        this.config = new XMLConfig("Checker");
        this.activeRules = new HashSet<>();
        this.rules = new HashSet<>();
    }

    /**
     * Adds a new rule to the current configuration state
     *
     * @param rule the XMLConfig representation of the rule to be added
     *             to the configuration
     */
    public void addActiveRule(XMLConfig rule) {

    }

    /**
     * Generates and saves the user-defined config to the given path
     *
     * @param fileName the name to save the generated configuration file as
     * @throws IllegalArgumentException - When the root module is not name "Checker"
     * @throws IllegalArgumentException - When the path is not saving to XML
     * @throws IllegalArgumentException - When the parent directory doesn't exist
     * @throws IOException - When file could not be created with the path
     */
    public void generateConfig(String fileName) throws IOException {
        String filepath = path + configName;
        ConfigWriter.saveConfig(filepath, config);
    }

    /**
     * Imports the state of an existing configuration file
     *
     * @param fileName the name of the XML configuration file to import
     * @throws FileNotFoundException - When the passed in file doesn’t exist.
     * @throws IllegalArgumentException - When the passed in file is not XML,
     *         or doesn’t have “Checker” as root module.
     * @throws ParserConfigurationException - DocumentFactory config error, please
     *         report when this error is thrown
     */
    public void importConfig(String fileName) throws ParserConfigurationException, SAXException, IOException {
        path = fileName.substring(0, fileName.lastIndexOf('/') + 1);
        configName = fileName.substring(fileName.lastIndexOf('/') + 1);
        config = ConfigReader.readConfig(path + fileName);
    }

    /**
     * Returns a set of the active rules in the current configuration
     *
     * @return a Set<ConfigRule> of all the active rules in the current
     *         configuration
     */
    public Set<ConfigRule> getActiveRules() {
        return new HashSet<>(activeRules);
    }

    /**
     * Returns the XML configuration representation for the given rule
     *
     * @param rule the rule to get the XML representation for
     * @return the XML format for the given rule
     */
    public XMLConfig getXMLforConfigRule(ConfigRule rule) {
        return null;
    }

    /**
     * Removes an active rule from the current configuration
     *
     * @param rule the rule to remove from the XML config
     */
    public void removeActiveRule(XMLConfig rule) {

    }

    /**
     * Returns information for all available rules that a user can
     * add to their configuration
     *
     * @return a Map<String, List<ConfigRule>> where the keys are Strings
     *         representing the rule categories, which map to Lists of
     *         ConfigRules, which contain all details for a given rule.
     */
    public Map<String, List<ConfigRule>> getAvailableRules() {
        return null;
    }

    /**
     * Returns a string representation of what the current XML configuration
     * will look like.
     *
     * @return a string representation of what the current XML configuration
     *         will look like.
     */
    public String getPreview() {
        return "";
    }
}
