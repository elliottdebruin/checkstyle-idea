package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

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
    // setResizable(false);
  }

  protected void createCenterPanel() {
    // Horizontal split pane
    JBSplitter window = new JBSplitter(false);

    // Vertical split pane
    JBSplitter rightPanel = new JBSplitter(true);

    rightPanel.setFirstComponent(createSelectRulesPanel());
    rightPanel.setSecondComponent(createActiveRulesPanel());

    window.setFirstComponent(createSelectCategoryPanel());
    window.setSecondComponent(rightPanel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(createTopRow(), BorderLayout.NORTH);
    getContentPane().add(window, BorderLayout.CENTER);
    getContentPane().add(createBottomRow(), BorderLayout.SOUTH);
  }

  protected JPanel createTopRow() {
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    JButton importBtn = new JButton("Import");
    topRow.add(importBtn);

    topRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    return topRow;
  }

  protected JPanel createBottomRow() {
    JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    bottomRow.add(Box.createHorizontalStrut(4));
    bottomRow.add(new JLabel("Configuration Name:"));
    bottomRow.add(Box.createHorizontalStrut(4));
    JTextField nameField = new JTextField(20);
    bottomRow.add(nameField);
    bottomRow.add(Box.createHorizontalStrut(4));
    JButton previewBtn = new JButton("Preview");
    bottomRow.add(previewBtn);
    bottomRow.add(Box.createHorizontalStrut(4));
    JButton generateBtn = new JButton("Generate");
    bottomRow.add(generateBtn);
    bottomRow.add(Box.createHorizontalGlue());

    bottomRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    return bottomRow;
  }

  protected JPanel createSelectCategoryPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(100, 800));
    scrollPane.setPreferredSize(new Dimension(100, 800));

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return panel;
  }

  protected JPanel createSelectRulesPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(400, 400));
    scrollPane.setPreferredSize(new Dimension(400, 400));

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return panel;
  }

  protected JPanel createActiveRulesPanel() {
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));
    
    JLabel activeLabel = new JLabel("Active Rules");
    Font font = activeLabel.getFont();
    activeLabel.setFont(new Font(font.getName(), Font.BOLD, 48));
    
    topRow.add(Box.createHorizontalStrut(4));
    topRow.add(activeLabel);
    topRow.add(Box.createHorizontalStrut(4));

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(400, 400));
    scrollPane.setPreferredSize(new Dimension(400, 400));
    
    bottomPanel.add(topRow, BorderLayout.NORTH);
    bottomPanel.add(scrollPane, BorderLayout.CENTER);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return bottomPanel;
  }

  protected static Image iconToImage(Icon ico) {
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
