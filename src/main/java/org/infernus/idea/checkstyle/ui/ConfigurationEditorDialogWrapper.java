package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intellij.openapi.ui.DialogWrapper;

public class ConfigurationEditorDialogWrapper extends DialogWrapper {
  public ConfigurationEditorDialogWrapper() {
    super(true);
    init();
    setTitle("Configuration Editor");
  }

  @Override
  protected JComponent createCenterPanel() {
    JPanel dialogPanel = new JPanel(new BorderLayout());

    JLabel label = new JLabel("testing");
    label.setPreferredSize(new Dimension(100, 100));
    dialogPanel.add(label, BorderLayout.CENTER);

    return dialogPanel;
  }
}
