package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.intellij.openapi.util.IconLoader;

/**
 * This class represents the window that displays a preview of the XML
 * Configuration that will be generated.
 */
public class XMLPreviewDialog extends JFrame {
  private static final long serialVersionUID = 31L;

  /**
   * The text area that displays the XML string
   */
  private final JTextArea textArea = new JTextArea();

  /**
   * Creates the JFrame, sets the icon image, and utilizes createWindowContent()
   * to populate the window content.
   */
  public XMLPreviewDialog() {
    super();
    setTitle("XML Preview");
    setIconImage(ConfigurationEditorWindow
        .iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createWindowContent();
    setLocationByPlatform(true);
  }

  /**
   * Sets the text aread as uneditable, gives it a border, and adds it to the
   * frame.
   */
  protected void createWindowContent() {
    this.textArea.setEditable(false);
    this.textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.textArea, BorderLayout.CENTER);
  }

  /**
   * Opens the preview window with xml in the text area.
   * @param xml The xml preview
   */
  public void display(String xml) {
    this.textArea.setLineWrap(false);
    this.textArea.setText(xml);

    pack();
    revalidate();
    repaint();
    this.textArea.setLineWrap(true);
    if (!isVisible()) {
      setVisible(true);
    } else {
      requestFocus();
    }
  }
}