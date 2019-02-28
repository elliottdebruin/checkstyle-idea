package org.infernus.idea.checkstyle;

import java.util.Arrays;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.ui.AttributeSubmissionListener;
import org.infernus.idea.checkstyle.ui.CheckAttributesEditorDialog;
import org.infernus.idea.checkstyle.ui.ConfigurationEditorWindow;
import org.infernus.idea.checkstyle.util.ConfigurationListeners;

public class CSConfigController {
  private final ConfigurationEditorWindow configEditor = new ConfigurationEditorWindow();
  private final CheckAttributesEditorDialog attrEditor = new CheckAttributesEditorDialog();

  public CSConfigController() {
    this.configEditor.setCategories(Arrays.asList("Annotation", "Blocks", "Coding", "Design", "Header", "Indentation",
        "Javadoc", "Metrics", "Modifier", "Naming", "Sizes", "Whitespace", "Other"));
    this.configEditor.setVisibleRules("Annotation", ConfigRule.getVisibleRulesDemo());
    this.configEditor.setActiveRules(ConfigRule.getActiveRulesDemo());
    this.configEditor.addSelectionListener(new ListSelectionListener(){
      @Override
      public void valueChanged(ListSelectionEvent e) {
        ConfigRule rule = configEditor.getSelectedVisibleRule();
        rule.getParameters().put("Motha", "Fucka");
        attrEditor.displayForCheck(rule);
      }
    }, ConfigurationListeners.VISIBLE_RULES_SELECT_LISTENER);

    
    this.attrEditor.addSubmitListener(new AttributeSubmissionListener(){
      @Override
      public void actionPerformed(String... values) {
        for (String value : values) {
          System.out.println(value);
        }
      }
    });
  }

  public void displayConfigEditor() {
    configEditor.setVisible(true);
  }
}