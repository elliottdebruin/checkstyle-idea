package org.infernus.idea.checkstyle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.model.XMLConfig;
import org.infernus.idea.checkstyle.ui.AttributeSubmissionListener;
import org.infernus.idea.checkstyle.ui.CheckAttributesEditorDialog;
import org.infernus.idea.checkstyle.ui.ConfigurationEditorWindow;
// import org.infernus.idea.checkstyle.ui.XMLPreviewDialog;
import org.infernus.idea.checkstyle.util.CheckStyleRuleProvider;
import org.infernus.idea.checkstyle.util.ConfigurationListeners;

public class CSConfigController {
  private final ConfigurationEditorWindow configEditor = new ConfigurationEditorWindow();
  private final CheckAttributesEditorDialog attrEditor = new CheckAttributesEditorDialog();
  // private final XMLPreviewDialog previewDialog = new XMLPreviewDialog();

  private final CheckStyleRuleProvider provider = new CheckStyleRuleProvider();

  private final List<ConfigRule> configRules = new ArrayList<>();
  private final List<XMLConfig> xmlRules = new ArrayList<>();

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

  private class CategorySelectListener implements MouseListener {
    private ConfigurationEditorWindow configEditor;
    private CheckStyleRuleProvider provider;

    public CategorySelectListener(ConfigurationEditorWindow configEditor, CheckStyleRuleProvider provider) {
      this.configEditor = configEditor;
      this.provider = provider;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      String category = this.configEditor.getSelectedCategory();
      this.configEditor.setVisibleRules(category, this.provider.getDefaultRulesByCategory(category));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
  }

  private class VisibleRuleSelectListener implements MouseListener {
    private ConfigurationEditorWindow configEditor;
    private CheckAttributesEditorDialog attrEditor;

    public VisibleRuleSelectListener(ConfigurationEditorWindow configEditor, CheckAttributesEditorDialog attrEditor) {
      this.configEditor = configEditor;
      this.attrEditor = attrEditor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      this.attrEditor.displayForCheck(configEditor.getSelectedVisibleRule());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
  }

  private class ActiveRuleSelectListener implements MouseListener {
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
    public void mouseClicked(MouseEvent e) {
      ConfigRule selected = this.configEditor.getSelectedActiveRule();
      if (selected != null) {
        this.attrEditor.displayForCheck(this.configEditor.getSelectedActiveRule(),
            this.xmlRules.get(this.configEditor.getSelectedActiveIndex()));
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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