package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBSplitter;

public class ConfigurationEditorWindow extends JFrame {
  private static final long serialVersionUID = 17L;

  public ConfigurationEditorWindow() {
    super("Configuration Editor");
    setIconImage(iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createCenterPanel();
    pack();
    setLocationByPlatform(true);
  }

  protected void createCenterPanel() {
    // Vertical split pane
    JBSplitter window = new JBSplitter(true);

    // Horizontal split pane
    JBSplitter centerPanel = new JBSplitter(false);
    JPanel bottomPanel = new JPanel(new BorderLayout());

    JPanel centerLeft = new JPanel(new BorderLayout());
    JPanel centerRight = new JPanel(new BorderLayout());

    JLabel label = new JLabel("testing");
    label.setPreferredSize(new Dimension(100, 100));
    centerRight.add(label);

    centerPanel.setFirstComponent(centerLeft);
    centerPanel.setSecondComponent(centerRight);

    window.setFirstComponent(centerPanel);
    window.setSecondComponent(bottomPanel);

    getContentPane().add(window);
  }

  private static Image iconToImage(Icon ico) {
    if (ico instanceof ImageIcon) {
      return ((ImageIcon) ico).getImage();
    } else {
      BufferedImage image = new BufferedImage(ico.getIconWidth(), ico.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = image.createGraphics();
      ico.paintIcon(null, g, 0, 0);
      g.dispose();
      return image;
    }
  }
}
