package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.intellij.openapi.util.IconLoader;

public class CheckAttributesEditorDialog extends JFrame {
  private static final long serialVersionUID = 13L;

  private final JPanel centerPanel = new JPanel();

  private final Collection<JTextField> textFields = new ArrayList<>();
  private final Collection<AttributeSubmissionListener> submissionListeners = new ArrayList<>();

  public CheckAttributesEditorDialog() {
    super();
    setIconImage(ConfigurationEditorWindow
        .iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createWindowContent();
    setLocationByPlatform(true);
    setMinimumSize(new Dimension(600, 400));

    addSubmitListener(new AttributeSubmissionListener(){
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
        submissionListeners.forEach(sl -> sl.actionPerformed(
          textFields.stream()
            .map(tf -> tf.getText())
            .collect(Collectors.toList())
            .toArray(new String[textFields.size()])
        ));
        setVisible(false);
      }
    });
    bottomRow.add(okBtn);
    bottomRow.add(Box.createHorizontalGlue());

    return bottomRow;
  }

  public void displayForCheck(String checkName, Collection<String> attributes) {
    setTitle(checkName + " Attributes");

    this.centerPanel.removeAll();
    this.textFields.clear();
    for (String attribute : attributes) {
      this.centerPanel.add(new JLabel(attribute + ":"));
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