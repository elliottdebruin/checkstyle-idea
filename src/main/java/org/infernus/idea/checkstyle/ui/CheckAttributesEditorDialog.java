package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

  private XMLConfig currentRule;

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
        submissionListeners.forEach(sl -> {
          XMLConfig copy = new XMLConfig(currentRule.getName());
          for (String attr : currentRule.getAttributeNames()) {
            copy.addAttribute(attr, currentRule.getAttribute(attr));
          }
          sl.actionPerformed(copy);
        });
        setVisible(false);
      }
    });
    bottomRow.add(okBtn);
    bottomRow.add(Box.createHorizontalStrut(4));
    JButton cancelBtn = new JButton("Cancel");
    cancelBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });
    bottomRow.add(cancelBtn);
    bottomRow.add(Box.createHorizontalGlue());

    return bottomRow;
  }

  public void displayForCheck(ConfigRule rule) {
    displayForCheck(rule, null);
  }

  public void displayForCheck(ConfigRule rule, XMLConfig config) {
    this.currentRule = new XMLConfig(rule.getRuleName());
    this.nameLabel.setText(rule.getRuleName());
    this.descLabel.setText(rule.getRuleDescription());

    this.centerPanel.removeAll();
    for (Entry<String, String> entry : rule.getParameters().entrySet()) {
      String attr = entry.getKey();

      JLabel label = new JLabel(attr + ": ");
      label.setHorizontalAlignment(SwingConstants.RIGHT);
      label.setToolTipText(entry.getValue());
      this.centerPanel.add(label);

      JTextField textField = new JTextField(20);
      if (config != null) {
        textField.setText(config.getAttribute(attr));
      }
      textField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          currentRule.addAttribute(attr, textField.getText());
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