package org.infernus.idea.checkstyle.model;

import com.intellij.openapi.project.Project;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;


public class ConfigGeneratorModelTest {
    private ConfigGeneratorModel model;

    @Before
    public void setUp() {
        model = new ConfigGeneratorModel(mock(Project.class));
    }

    @Test
    public void ConfigGeneratorRootIsCheckerTest() throws FileNotFoundException {
        //ConfigGeneratorModel model = new ConfigGeneratorModel(null);
       // assertEquals("Checker", model.config.getName());
        File xml = new File(".idea/configs/test.xml");
        Scanner scan = new Scanner(xml);
        while(scan.hasNextLine()) {
            System.out.println(scan.nextLine());
        }
        Project p = mock(Project.class);
         //p.
        //System.out.println(p.getBasePath());

    }

    @Test
    public void GenerateConfigTest() {

    }

    @Test
    public void ImportConfigTest() {

    }

    @Test
    public void GetActiveRulesTest() {

    }

    @Test
    public void GetConfigRuleForXMLTest() {

    }

    @Test
    public void GetAvailableRulesHasAllCategoriesTest() {
        Set<String> defaultCats = new TreeSet<>();
        defaultCats.add("Annotations");
        defaultCats.add("Block Checks");
        defaultCats.add("Checker");
        defaultCats.add("Class Design");
        defaultCats.add("Coding");
        defaultCats.add("Headers");
        defaultCats.add("Imports");
        defaultCats.add("Javadoc Comments");
        defaultCats.add("Metrics");
        defaultCats.add("Miscellaneous");
        defaultCats.add("Modifiers");
        defaultCats.add("Naming Conventions");
        defaultCats.add("Regexp");
        defaultCats.add("Size Violations");
        defaultCats.add("Whitespace");

        TreeMap<String, List<ConfigRule>> rules = model.getAvailableRules();
        assertEquals(defaultCats, rules.keySet());
    }

    @Test
    public void ConfigPreviewTest() {

        System.out.println(model.getPreview());
    }

    @Test
    public void GetConfigNamesTest() {

    }

    @Test
    public void AddActiveRuleTest() {
        XMLConfig xml = new XMLConfig("TestConfig");
        model.addActiveRule(xml);
        assertEquals(model.getActiveRules().size(), 1);
        Collection<XMLConfig> active = model.getActiveRules();
        assertTrue(active.contains(xml));
    }

    @Test
    public void RemoveActiveRuleTest() {
        XMLConfig xml = new XMLConfig("TestConfig");
        model.addActiveRule(xml);
        Collection<XMLConfig> active = model.getActiveRules();
        assertTrue(active.contains(xml));
        model.removeActiveRule(xml);
        assertTrue(!model.getActiveRules().contains(xml));
    }
}
