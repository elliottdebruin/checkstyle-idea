package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intellij.openapi.ui.DialogWrapper;

public class ConfigurationEditorDialogWrapper extends JFrame {
  public ConfigurationEditorDialogWrapper() {
    super("Configuration Editor");
    try {
      setIconImage(ImageIO.read(new File("src/main/resources/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    } catch (IOException ex) {
      System.out.println("Could not find icon!");
    }
    createCenterPanel();
    setPreferredSize(new Dimension(500, 500));
    pack();
    // setSize(500, 500);
    setLocationByPlatform(true);
  }

  protected void createCenterPanel() {
    JPanel centerPanel = new JPanel(new BorderLayout());

    JLabel label = new JLabel("testing");
    label.setPreferredSize(new Dimension(100, 100));
    centerPanel.add(label, BorderLayout.CENTER);

    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }
}
