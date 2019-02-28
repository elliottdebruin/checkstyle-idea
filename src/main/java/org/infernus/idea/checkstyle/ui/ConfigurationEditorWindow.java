package org.infernus.idea.checkstyle.ui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBSplitter;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.util.ConfigurationListeners;

/**
 * This class represents the main Configuration Editor Window. Its layout is
 * divided into a "Top Row", a "Category Panel", a "Visible Rules Panel", an
 * "Active Rules Panel", and a "Bottom Row". The Top Row has buttons for
 * importing XML configurations. The Bottom Row has a text field for defining a
 * Configuration name and buttons for previewing and generating XML
 * configurations. Additionally, there are lists of checks and their categories,
 * and the checks attached to the current configuration in each of their
 * corresponding panels.
 */
public class ConfigurationEditorWindow extends JFrame {
  private static final long serialVersionUID = 17L;

  /**
   * The text field that holds the user-inputted name of the configuration.
   */
  private final JTextField configNameField = new JTextField(20);
  /**
   * The label in the "Visible Rules" panel that corresponds with the
   * currently-selected category.
   */
  private final JLabel categoryLabel = new JLabel();

  /**
   * The list of selectable categories in the "Categories" panel.
   */
  private final JList<String> categoryList = new JList<>();
  /**
   * The list of selectable rules in the "Visible Rules" panel. These should be
   * rules that are associated with the currently-selected category.
   */
  private final JList<ConfigRule> visibleRulesList = new JList<>();
  /**
   * The list of selectable rules in the "Active Rules panel". These are rules
   * attached to the current configuration state.
   */
  private final JList<ConfigRule> activeRulesList = new JList<>();

  /**
   * All of the listeners that have been registered with the "Import" button.
   */
  private final Collection<ActionListener> importBtnListeners = new ArrayList<>();
  /**
   * All of the listeners that have been registered with the "Preview" button.
   */
  private final Collection<ActionListener> previewBtnListeners = new ArrayList<>();
  /**
   * All of the listeners that have been registered with the "Generate" button.
   */
  private final Collection<ActionListener> generateBtnListeners = new ArrayList<>();
  /**
   * All of the listeners that have been registered with the list in the
   * "Category" panel
   */
  private final Collection<ListSelectionListener> categorySelectListeners = new ArrayList<>();
  /**
   * All of the listeners that have been registered with the list in the "Visible
   * Rules" panel
   */
  private final Collection<ListSelectionListener> visibleRulesSelectListeners = new ArrayList<>();
  /**
   * All of the listeners that have been registered with the list in the "Active
   * Rules" panel
   */
  private final Collection<ListSelectionListener> activeRulesSelectListeners = new ArrayList<>();

  /**
   * The constructor sets the title, icon image, and minimum size of the window.
   * It also employes <code>createWindowContent()</code> to populate the window
   * with content.
   */
  public ConfigurationEditorWindow() {
    super("Configuration Editor");
    setIconImage(iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createWindowContent();
    setMinimumSize(new Dimension(1500, 900));
    pack();
    setLocationByPlatform(true);
  }

  /**
   * This method builds out the basic structure of the editor window with split
   * panes. It utilizes <code>createCategoryPanel()</code>,
   * <code>createVisibleRulesPanel()</code>,
   * <code>createActiveRulesPanel()</code>, <code>createTopRow()</code>, and
   * <code>createBottomRow()</code> to populate the content of each layout
   * portion.
   */
  protected void createWindowContent() {
    // Horizontal split pane
    JBSplitter window = new JBSplitter(false);
    // Vertical split pane
    JBSplitter rightPanel = new JBSplitter(true);

    rightPanel.setFirstComponent(createVisibleRulesPanel());
    rightPanel.setSecondComponent(createActiveRulesPanel());

    window.setFirstComponent(createSelectCategoryPanel());
    window.setSecondComponent(rightPanel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(createTopRow(), BorderLayout.NORTH);
    getContentPane().add(window, BorderLayout.CENTER);
    getContentPane().add(createBottomRow(), BorderLayout.SOUTH);
  }

  /**
   * Creates the content for the top row of the window. This includes the "Import"
   * button.
   * 
   * @return The top row of the editor window.
   */
  protected JPanel createTopRow() {
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    JButton importBtn = new JButton("Import");
    importBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        importBtnListeners.forEach(ibl -> ibl.actionPerformed(e));
      }
    });
    topRow.add(importBtn);

    topRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    return topRow;
  }

  /**
   * Creates the content for the bottom row of the window. This includes the
   * "Configuration Name" text field and the "Preview" and "Generate" buttons.
   * 
   * @return The bottom row of the editor window.
   */
  protected JPanel createBottomRow() {
    JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    bottomRow.add(Box.createHorizontalStrut(4));
    bottomRow.add(new JLabel("Configuration Name:"));
    bottomRow.add(Box.createHorizontalStrut(4));
    bottomRow.add(this.configNameField);
    bottomRow.add(Box.createHorizontalStrut(4));

    JButton previewBtn = new JButton("Preview");
    previewBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        previewBtnListeners.forEach(pbl -> pbl.actionPerformed(e));
      }
    });
    bottomRow.add(previewBtn);
    bottomRow.add(Box.createHorizontalStrut(4));

    JButton generateBtn = new JButton("Generate");
    generateBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        generateBtnListeners.forEach(gbl -> gbl.actionPerformed(e));
      }
    });
    bottomRow.add(generateBtn);
    bottomRow.add(Box.createHorizontalGlue());

    bottomRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    return bottomRow;
  }

  /**
   * Creates the content for the "Category" Panel of the window.
   * 
   * @return The category panel of the editor window.
   */
  protected JPanel createSelectCategoryPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    this.categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.categoryList.setLayoutOrientation(JList.VERTICAL);
    this.categoryList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        categorySelectListeners.forEach(csl -> csl.valueChanged(e));
      }
    });

    JScrollPane scrollPane = new JScrollPane(this.categoryList);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(200, 400));
    scrollPane.setPreferredSize(new Dimension(200, 800));

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return panel;
  }

  /**
   * Creates the "Visible Rules" Panel of the window.
   * 
   * @return The visible rules panel of the editor window.
   */
  protected JPanel createVisibleRulesPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    Font font = this.categoryLabel.getFont();
    this.categoryLabel.setFont(new Font(font.getName(), Font.BOLD, 48));

    topRow.add(Box.createHorizontalStrut(4));
    topRow.add(this.categoryLabel);
    topRow.add(Box.createHorizontalStrut(4));

    this.visibleRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.visibleRulesList.setLayoutOrientation(JList.VERTICAL);
    this.visibleRulesList.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int index = visibleRulesList.locationToIndex(e.getPoint());
        if (index > -1) {
          visibleRulesList.setToolTipText(visibleRulesList.getModel().getElementAt(index).getRuleDescription());
        }
      }

      @Override
      public void mouseDragged(MouseEvent e) {
      }
    });
    this.visibleRulesList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        visibleRulesSelectListeners.forEach(vrsl -> vrsl.valueChanged(e));
      }
    });

    JScrollPane scrollPane = new JScrollPane(this.visibleRulesList);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(800, 200));
    scrollPane.setPreferredSize(new Dimension(800, 400));

    panel.add(topRow, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return panel;
  }

  /**
   * Creates the "Active Rules" panel of the window.
   * 
   * @return The active rules panel of the editor window.
   */
  protected JPanel createActiveRulesPanel() {
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    JLabel activeLabel = new JLabel("Active Rules");
    Font font = activeLabel.getFont();
    activeLabel.setFont(new Font(font.getName(), Font.BOLD, 48));

    topRow.add(Box.createHorizontalStrut(4));
    topRow.add(activeLabel);
    topRow.add(Box.createHorizontalStrut(4));

    this.activeRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.activeRulesList.setLayoutOrientation(JList.VERTICAL);
    this.activeRulesList.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int index = activeRulesList.locationToIndex(e.getPoint());
        if (index > -1) {
          activeRulesList.setToolTipText(activeRulesList.getModel().getElementAt(index).getRuleDescription());
        }
      }

      @Override
      public void mouseDragged(MouseEvent e) {
      }
    });
    this.activeRulesList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        activeRulesSelectListeners.forEach(arsl -> arsl.valueChanged(e));
      }
    });

    JScrollPane scrollPane = new JScrollPane(this.activeRulesList);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(800, 200));
    scrollPane.setPreferredSize(new Dimension(800, 400));

    bottomPanel.add(topRow, BorderLayout.NORTH);
    bottomPanel.add(scrollPane, BorderLayout.CENTER);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    return bottomPanel;
  }

  /**
   * Converts an Icon to an Image.
   * 
   * @param ico The Icon to convert
   * @return The Icon converted to an Image
   */
  public static Image iconToImage(Icon ico) {
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

  /**
   * Sets the categories to display in the Category panel.
   * 
   * @param categories The categories to set
   */
  public void setCategories(Collection<String> categories) {
    this.categoryList.setListData(categories.toArray(new String[categories.size()]));
  }

  /**
   * Gets the currently-selected category in the Category panel.
   * 
   * @return The currently-selected category
   */
  public String getSelectedCategory() {
    return this.categoryList.getSelectedValue();
  }

  /**
   * Sets the rules to display in the Visible Rules panel.
   * 
   * @param category The category with which <code>rules</code> are associated
   * @param rules    The rules to display
   */
  public void setVisibleRules(String category, Collection<ConfigRule> rules) {
    this.categoryLabel.setText(category);
    this.visibleRulesList.setListData(rules.toArray(new ConfigRule[rules.size()]));
  }

  /**
   * Gets the currently-selected rule in the Visible Rules panel.
   * 
   * @return The currently-selected visible rule
   */
  public ConfigRule getSelectedVisibleRule() {
    return this.visibleRulesList.getSelectedValue();
  }

  /**
   * Sets the rules to display in the Active Rules panel.
   * 
   * @param rules The rules to display
   */
  public void setActiveRules(Collection<ConfigRule> rules) {
    this.activeRulesList.setListData(rules.toArray(new ConfigRule[rules.size()]));
  }

  /**
   * Gets the currently-selected rules in the Active Rules panel.
   * 
   * @return The currently-selected active rule
   */
  public ConfigRule getSelectedActiveRule() {
    System.out.println(this.activeRulesList.getSelectedValue());
    return this.activeRulesList.getSelectedValue();
  }

  /**
   * Sets the text in the Configuration Name text field.
   * 
   * @param name The text to set the field to
   */
  public void setConfigurationName(String name) {
    this.configNameField.setText(name);
  }

  /**
   * Gets the value typed into the Configuration Name text field.
   * 
   * @return The name of this configuration
   */
  public String getConfigurationName() {
    return this.configNameField.getText();
  }

  /**
   * Registers a listener for one of the buttons in the editor window (Import,
   * Preview, Generate).
   * 
   * @param al     The listener to register
   * @param button The button with which to register <code>al</code>
   */
  public void addButtonListener(ActionListener al, ConfigurationListeners button) {
    switch (button) {
    case IMPORT_BUTTON_LISTENER:
      this.importBtnListeners.add(al);
      break;
    case PREVIEW_BUTTON_LISTENER:
      this.previewBtnListeners.add(al);
      break;
    case GENERATE_BUTTON_LISTENER:
      this.generateBtnListeners.add(al);
      break;
    default:
      break;
    }
  }

  /**
   * Registers a listener for one of the selection lists in the editor window
   * (Categories, Visible Rules, Active Rules).
   * 
   * @param lsl           The listener to register
   * @param selectionList The selection list with which to register
   *                      <code>lsl</code>
   */
  public void addSelectionListener(ListSelectionListener lsl, ConfigurationListeners selectionList) {
    switch (selectionList) {
    case CATEGORY_SELECT_LISTENER:
      this.categorySelectListeners.add(lsl);
      break;
    case VISIBLE_RULES_SELECT_LISTENER:
      this.visibleRulesSelectListeners.add(lsl);
      break;
    case ACTIVE_RULES_SELECT_LISTENER:
      this.activeRulesSelectListeners.add(lsl);
      break;
    default:
      break;
    }
  }
}
