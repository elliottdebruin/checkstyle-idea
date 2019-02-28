package org.infernus.idea.checkstyle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.model.XMLConfig;
import org.infernus.idea.checkstyle.ui.AttributeSubmissionListener;
import org.infernus.idea.checkstyle.ui.CheckAttributesEditorDialog;
import org.infernus.idea.checkstyle.ui.ConfigurationEditorWindow;
import org.infernus.idea.checkstyle.ui.XMLPreviewDialog;
import org.infernus.idea.checkstyle.util.ConfigWriter;
import org.infernus.idea.checkstyle.util.ConfigurationListeners;

public class CSConfigController {
  private final ConfigurationEditorWindow configEditor = new ConfigurationEditorWindow();
  private final CheckAttributesEditorDialog attrEditor = new CheckAttributesEditorDialog();
  private final XMLPreviewDialog previewDialog = new XMLPreviewDialog();

  private XMLConfig rule = new XMLConfig("");

  public CSConfigController() {
    this.configEditor.setCategories(Arrays.asList("Annotation", "Blocks", "Coding", "Design", "Header", "Indentation",
        "Javadoc", "Metrics", "Modifier", "Naming", "Sizes", "Whitespace", "Other"));
    this.configEditor.setVisibleRules("Annotation", ConfigRule.getVisibleRulesDemo());
    this.configEditor.setActiveRules(ConfigRule.getActiveRulesDemo());
    this.configEditor.addSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        ConfigRule rule = configEditor.getSelectedVisibleRule();
        rule.getParameters().put("Motha", "Fucka");
        attrEditor.displayForCheck(rule);
      }
    }, ConfigurationListeners.VISIBLE_RULES_SELECT_LISTENER);

    this.attrEditor.addSubmitListener(new AttributeSubmissionListener() {
      @Override
      public void actionPerformed(XMLConfig currentRule) {
        rule = currentRule;
        configEditor.setActiveRules(Arrays.asList(new ConfigRule(rule.getName(), "")));
      }
    });
    this.configEditor.addSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        attrEditor.displayForCheck(configEditor.getSelectedActiveRule(), rule);
      }
    }, ConfigurationListeners.ACTIVE_RULES_SELECT_LISTENER);

    XMLConfig config = new XMLConfig("Checker");
    XMLConfig treeWalker = new XMLConfig("TreeWalker");
    XMLConfig rule1 = new XMLConfig("Rule 1");
    rule1.addAttribute("attribute 1", "value 1");
    rule1.addAttribute("attribute 2", "value 2");
    XMLConfig rule2 = new XMLConfig("Rule 2");
    rule2.addMessage("Message 1", "MessageValue");
    treeWalker.addChild(rule1);
    config.addChild(treeWalker);
    config.addChild(rule2);

    this.configEditor.addButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        previewDialog.display(ConfigWriter.xmlPreview(config));
      }
    }, ConfigurationListeners.PREVIEW_BUTTON_LISTENER);
  }

  public void displayConfigEditor() {
    configEditor.setVisible(true);
  }
}