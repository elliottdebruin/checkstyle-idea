package org.infernus.idea.checkstyle.model;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.infernus.idea.checkstyle.util.CheckStyleRuleProvider;
import org.infernus.idea.checkstyle.util.ConfigReader;
import org.infernus.idea.checkstyle.util.ConfigWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ConfigGeneratorModel {
    /** The current state of the configuration */
    XMLConfig config;

    /**
     * The Map<String, ConfigRule> with the String being the name of a rule
     * mapping to the corresponding ConfigRule
     */
    private Map<String, ConfigRule> activeRules;

    /** XMLConfig representations of all the active rules for the config */
    private List<XMLConfig> xmlConfigs;

    /** The state of the user's project */
    private Project project;

    /**
     * A TreeMap<String, List<ConfigRule>> with names of all rule categories
     * mapping to lists of all available rules in each category
     */
    private TreeMap<String, List<ConfigRule>> possibleRules;

    /**
     * Creates a new ConfigGeneratorModel with a blank XML configuration, file name,
     * path to the file, and set of active rules
     */
    public ConfigGeneratorModel(Project project) {
        CheckStyleRuleProvider provider = new CheckStyleRuleProvider();
        this.possibleRules = new TreeMap<>(provider.getDefaultCategorizedRule());
        this.config = new XMLConfig("Checker");
        this.activeRules = new HashMap<>();
        this.xmlConfigs = new LinkedList<>();
        this.project = project;
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
        for (XMLConfig rule : xmlConfigs) {
            config.addChild(rule);
        }
        String filepath = project.getBasePath() + "/.idea/configs/" + fileName + ".xml";
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
        config = ConfigReader.readConfig(project.getBasePath() + "/.idea/configs/" + fileName + ".xml");
    }

    /**
     * Returns a set of the active rules in the current configuration
     *
     * @return a Collection<XMLConfig> of all the active rules in the current
     *         configuration
     */
    public List<XMLConfig> getActiveRules() {
        return new LinkedList<>(xmlConfigs);
    }

    /**
     * Returns the ConfigRule representation for the given XML rule configuration
     *
     * @param rule the rule to get the ConfigRule representation for
     * @return the ConfigRule representation for the given XML rule configuration
     */
    public ConfigRule getConfigRuleforXML(XMLConfig rule) {
        String ruleName = rule.getName();
        for (String cat : possibleRules.keySet()) {
            for (ConfigRule ruleDetails : possibleRules.get(cat)) {
                if (ruleName.equals(ruleDetails.getRuleName())) {
                    return ruleDetails;
                }
            }
        }
        return null;
    }

    /**
     * Returns information for all available rules that a user can
     * add to their configuration
     *
     * @return a Map<String, List<ConfigRule>> where the keys are Strings
     *         representing the rule categories, which map to Lists of
     *         ConfigRules, which contain all details for a given rule.
     */
    public TreeMap<String, List<ConfigRule>> getAvailableRules() {
        return new TreeMap<>(possibleRules);
    }

    /**
     * Returns a string representation of what the current XML configuration
     * will look like.
     *
     * @return a string representation of what the current XML configuration
     *         will look like.
     */
    public String getPreview() {
        XMLConfig preview = new XMLConfig("Checker");
        for (XMLConfig rule : xmlConfigs) {
            preview.addChild(rule);
        }
        return ConfigWriter.xmlPreview(preview);
    }

    /**
     * Returns the names of all the possible configuration files a user
     * can import.
     *
     * @return a Set<String> containing the names of all the possible configuration files a user
     *         can import.
     */
    public Set<String> getConfigNames() {
        Set<String> configFileNames = new HashSet<>();
        VirtualFile s = project.getBaseDir();
        VirtualFile idea = null;
        for (VirtualFile dir : s.getChildren()) {
            if (dir.getName().equals(".idea")) {
                idea = dir;
                break;
            }
        }
        if (idea != null) {
            for (VirtualFile dir : idea.getChildren()) {
                if (dir.getName().equals("configs")) {
                    for (VirtualFile config : dir.getChildren()) {
                        configFileNames.add(config.getName().replace(".xml", ""));
                    }
                }
            }
        }
        return configFileNames;
    }

    /**
     * Adds a new rule to the current configuration state
     *
     * @param rule the XMLConfig representation of the rule to be added
     *             to the configuration
     */
    public void addActiveRule(XMLConfig rule) {
        xmlConfigs.add(rule);
    }

    /**
     * Removes an active rule from the current configuration
     *
     * @param rule the rule to remove from the XML config
     */
    public void removeActiveRule(XMLConfig rule) {
        xmlConfigs.remove(rule);
    }
}
