package org.infernus.idea.checkstyle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.model.PropertyMetadata;
import org.infernus.idea.checkstyle.model.XMLConfig;
import org.infernus.idea.checkstyle.ui.AttributeSubmissionListener;
import org.infernus.idea.checkstyle.ui.CheckAttributesEditorDialog;
import org.infernus.idea.checkstyle.ui.ConfigurationEditorWindow;
import org.infernus.idea.checkstyle.ui.XMLPreviewDialog;
import org.infernus.idea.checkstyle.util.CheckStyleRuleProvider;
import org.infernus.idea.checkstyle.util.ConfigWriter;
import org.infernus.idea.checkstyle.util.ConfigurationListeners;

public class CSConfigController {
  private final ConfigurationEditorWindow configEditor = new ConfigurationEditorWindow();
  private final CheckAttributesEditorDialog attrEditor = new CheckAttributesEditorDialog();
  private final XMLPreviewDialog previewDialog = new XMLPreviewDialog();

  private final CheckStyleRuleProvider provider = new CheckStyleRuleProvider();

  private final List<ConfigRule> configRules = new ArrayList<>();
  private final List<XMLConfig> xmlRules = new ArrayList<>();

  private XMLConfig rule = new XMLConfig("");

  public CSConfigController() {
    setCategories();
    addListeners();
  }

  protected void setCategories() {
    Map<String, List<ConfigRule>> categorizedRules = this.provider.getDefaultCategorizedRules();
    this.configEditor.setCategories(categorizedRules.keySet());
  }

  protected void addListeners() {
    this.configEditor.addSelectionListener(new CategorySelectListener(this.configEditor, this.provider),
        ConfigurationListeners.CATEGORY_SELECT_LISTENER);
    this.configEditor.addSelectionListener(new VisibleRuleSelectListener(this.configEditor, this.attrEditor),
        ConfigurationListeners.VISIBLE_RULES_SELECT_LISTENER);
    this.configEditor.addSelectionListener(
        new ActiveRuleSelectListener(this.configEditor, this.attrEditor, this.xmlRules),
        ConfigurationListeners.ACTIVE_RULES_SELECT_LISTENER);

    this.attrEditor.addSubmitListener(new AttributeSubmitListener(this.configEditor, this.configRules, this.xmlRules));
  }

  public void displayConfigEditor() {
    configEditor.setVisible(true);
  }

  private class CategorySelectListener implements ListSelectionListener {
    private ConfigurationEditorWindow configEditor;
    private CheckStyleRuleProvider provider;

    public CategorySelectListener(ConfigurationEditorWindow configEditor, CheckStyleRuleProvider provider) {
      this.configEditor = configEditor;
      this.provider = provider;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      String category = this.configEditor.getSelectedCategory();
      this.configEditor.setVisibleRules(category, this.provider.getDefaultRulesByCategory(category));
    }
  }

  private class VisibleRuleSelectListener implements ListSelectionListener {
    private ConfigurationEditorWindow configEditor;
    private CheckAttributesEditorDialog attrEditor;

    public VisibleRuleSelectListener(ConfigurationEditorWindow configEditor, CheckAttributesEditorDialog attrEditor) {
      this.configEditor = configEditor;
      this.attrEditor = attrEditor;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      this.attrEditor.displayForCheck(configEditor.getSelectedVisibleRule());
    }
  }

  private class ActiveRuleSelectListener implements ListSelectionListener {
    private ConfigurationEditorWindow configEditor;
    private CheckAttributesEditorDialog attrEditor;
    private List<XMLConfig> xmlRules;

    public ActiveRuleSelectListener(ConfigurationEditorWindow configEditor, CheckAttributesEditorDialog attrEditor,
        List<XMLConfig> xmlRules) {
      this.configEditor = configEditor;
      this.attrEditor = attrEditor;
      this.xmlRules = xmlRules;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      ConfigRule selected = this.configEditor.getSelectedActiveRule();
      if (selected != null) {
        this.attrEditor.displayForCheck(this.configEditor.getSelectedActiveRule(),
            this.xmlRules.get(this.configEditor.getSelectedActiveIndex()));
      }
    }
  }

  private class AttributeSubmitListener implements AttributeSubmissionListener {
    ConfigurationEditorWindow configEditor;
    List<ConfigRule> configRules;
    List<XMLConfig> xmlRules;

    public AttributeSubmitListener(ConfigurationEditorWindow configEditor, List<ConfigRule> configRules,
        List<XMLConfig> xmlRules) {
      this.configEditor = configEditor;
      this.configRules = configRules;
      this.xmlRules = xmlRules;
    }

    @Override
    public void attributeSubmitted(ConfigRule configRule, XMLConfig xmlRule, boolean isNewRule) {
      if (isNewRule) {
        this.configRules.add(configRule);
        this.xmlRules.add(xmlRule);
        this.configEditor.setActiveRules(this.configRules);
      }
    }

    @Override
    public void attributeCancelled(ConfigRule configRule, XMLConfig xmlRule, boolean isNewRule) {
      if (!isNewRule) {
        this.configRules.remove(configRule);
        this.xmlRules.remove(xmlRule);
        this.configEditor.setActiveRules(this.configRules);
      }
    }
  }
}