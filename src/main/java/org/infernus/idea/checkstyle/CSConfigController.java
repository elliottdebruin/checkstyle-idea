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
    
  }

  public void displayConfigEditor() {
    configEditor.setVisible(true);
  }
}