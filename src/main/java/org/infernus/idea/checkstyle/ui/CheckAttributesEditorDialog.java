package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.intellij.openapi.util.IconLoader;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.model.PropertyMetadata;
import org.infernus.idea.checkstyle.model.XMLConfig;

/**
 * This class represents the Attributes Editor Dialog that displays the
 * attributes associated with a given CheckStyle rule and allows the user to set
 * them.
 */
public class CheckAttributesEditorDialog extends JFrame {
  private static final long serialVersionUID = 13L;

  /**
   * The center panel that displays attribute names and values
   */
  private final JPanel centerPanel = new JPanel();
  private final JLabel nameLabel = new JLabel();
  private final JLabel descLabel = new JLabel();
  private final JButton cancelBtn = new JButton();

  private ConfigRule configRule;
  private XMLConfig xmlRule;
  private boolean isNewRule;

  /**
   * The listeners that have been registered with the "OK" button
   */
  private final Collection<AttributeSubmissionListener> submissionListeners = new ArrayList<>();

  /**
   * Creates the JFrame, sets the icon image and preferred size, and utilized
   * createWindowContent() to populate the window content.
   */
  public CheckAttributesEditorDialog() {
    super();
    setTitle("Attributes Editor");
    setIconImage(ConfigurationEditorWindow
        .iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createWindowContent();
    setLocationByPlatform(true);
    setMinimumSize(new Dimension(600, 400));
  }

  protected void createWindowContent() {
    this.centerPanel.setLayout(new GridLayout(0, 2));
    this.centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JPanel topRow = new JPanel(new BorderLayout());
    Font font = this.nameLabel.getFont();
    this.nameLabel.setFont(new Font(font.getName(), Font.BOLD, 48));
    topRow.add(this.nameLabel, BorderLayout.NORTH);
    topRow.add(this.descLabel, BorderLayout.SOUTH);
    topRow.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(topRow, BorderLayout.NORTH);
    getContentPane().add(this.centerPanel, BorderLayout.CENTER);
    getContentPane().add(createBottomRow(), BorderLayout.SOUTH);
  }

  protected JPanel createBottomRow() {
    JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.TRAILING));

    bottomRow.add(Box.createHorizontalStrut(4));
    JButton okBtn = new JButton("OK");
    okBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        submissionListeners.forEach(sl -> sl.attributeSubmitted(configRule, xmlRule, isNewRule));
        setVisible(false);
        configRule = null;
        xmlRule = null;
      }
    });
    bottomRow.add(okBtn);
    bottomRow.add(Box.createHorizontalStrut(4));
    this.cancelBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        submissionListeners.forEach(sl -> sl.attributeCancelled(configRule, xmlRule, isNewRule));
        setVisible(false);
        configRule = null;
        xmlRule = null;
      }
    });
    bottomRow.add(this.cancelBtn);
    bottomRow.add(Box.createHorizontalGlue());

    return bottomRow;
  }

  public static String camelToTitle(String camel) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < camel.length(); i++) {
      if (i == 0) {
        sb.append(Character.toUpperCase(camel.charAt(0)));
      } else if (Character.isUpperCase(camel.charAt(i))) {
        sb.append(" " + camel.charAt(i));
      } else {
        sb.append(camel.charAt(i));
      }
    }
    return sb.toString();
  }

  public void displayForCheck(ConfigRule rule) {
    displayForCheck(rule, null);
  }

  public void displayForCheck(ConfigRule rule, XMLConfig config) {
    this.isNewRule = config == null;

    this.configRule = rule;
    this.xmlRule = this.isNewRule ? new XMLConfig(rule.getRuleName()) : config;
    this.nameLabel.setText(rule.getRuleName());
    this.descLabel.setText(rule.getRuleDescription());
    this.cancelBtn.setText(this.isNewRule ? "Cancel" : "Delete");

    this.centerPanel.removeAll();
    for (PropertyMetadata entry : rule.getParameters().values()) {
      String attr = entry.getName();

      JLabel label = new JLabel(camelToTitle(attr) + ": ");
      label.setHorizontalAlignment(SwingConstants.LEFT);
      label.setToolTipText(attr);
      this.centerPanel.add(label);

      JTextField textField = new JTextField(20);
      if (config != null) {
        // Throws an exception if attr has not been set, which CAN happen (rarely)
        textField.setText(config.getAttribute(attr));
      }
      textField.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
          // The below line does not run if the user entered the text by
          // right-clicking and pasting into the boxes
          char keyChar = e.getKeyChar();
          xmlRule.addAttribute(attr, textField.getText() + (Character.isAlphabetic(keyChar) ? keyChar : ""));
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }
      });
      this.centerPanel.add(textField);
    }

    pack();
    revalidate();
    repaint();
    if (!isVisible()) {
      setVisible(true);
    } else {
      requestFocus();
    }
  }

  public void addSubmitListener(AttributeSubmissionListener asl) {
    this.submissionListeners.add(asl);
  }
}