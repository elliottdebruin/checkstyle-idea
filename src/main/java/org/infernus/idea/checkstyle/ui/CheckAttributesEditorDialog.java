package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
  /**
   * The text fields associated with the values of the attributes
   */
  private final Collection<JTextField> textFields = new ArrayList<>();
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
    setIconImage(ConfigurationEditorWindow
        .iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createWindowContent();
    setLocationByPlatform(true);
    setMinimumSize(new Dimension(600, 400));

    // TODO: Remove the code below
    addSubmitListener(new AttributeSubmissionListener() {
      @Override
      public void actionPerformed(String... values) {
        for (String value : values) {
          System.out.println(value);
        }
      }
    });
  }

  protected void createWindowContent() {
    this.centerPanel.setLayout(new GridLayout(0, 2));
    this.centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    getContentPane().setLayout(new BorderLayout());
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
        submissionListeners.forEach(sl -> sl.actionPerformed(textFields.stream().map(tf -> tf.getText())
            .collect(Collectors.toList()).toArray(new String[textFields.size()])));
        setVisible(false);
      }
    });
    bottomRow.add(okBtn);
    bottomRow.add(Box.createHorizontalGlue());

    return bottomRow;
  }

  public void displayForCheck(ConfigRule rule) {
    setTitle("Attributes Editor");

    this.centerPanel.removeAll();
    this.textFields.clear();
    for (Entry<String, String> entry : rule.getParameters().entrySet()) {
      JLabel label = new JLabel(entry.getKey() + ": ");
      label.setHorizontalAlignment(SwingConstants.RIGHT);
      label.setToolTipText(entry.getValue());
      this.centerPanel.add(label);
      JTextField textField = new JTextField(20);
      this.textFields.add(textField);
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