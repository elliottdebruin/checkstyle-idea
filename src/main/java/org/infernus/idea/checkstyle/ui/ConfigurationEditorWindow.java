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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
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

enum ConfigurationListeners {
  IMPORT_BUTTON_LISTENER, PREVIEW_BUTTON_LISTENER, GENERATE_BUTTON_LISTENER, CATEGORY_SELECT_LISTENER,
  VISIBLE_RULES_SELECT_LISTENER, ACTIVE_RULES_SELECT_LISTENER;
}

public class ConfigurationEditorWindow extends JFrame {
  private static final long serialVersionUID = 17L;

  private final JTextField configNameField = new JTextField(20);
  private final JLabel categoryLabel = new JLabel();
  private final JList<String> categoryList = new JList<>();
  private final JList<String> visibleRulesList = new JList<>();
  private final JList<String> activeRulesList = new JList<>();

  private final Collection<ActionListener> importBtnListeners = new ArrayList<>();
  private final Collection<ActionListener> previewBtnListeners = new ArrayList<>();
  private final Collection<ActionListener> generateBtnListeners = new ArrayList<>();

  private final Collection<ListSelectionListener> categorySelectListeners = new ArrayList<>();
  private final Collection<ListSelectionListener> visibleRulesSelectListeners = new ArrayList<>();
  private final Collection<ListSelectionListener> activeRulesSelectListeners = new ArrayList<>();

  public ConfigurationEditorWindow() {
    super("Configuration Editor");
    setIconImage(iconToImage(IconLoader.getIcon("/org/infernus/idea/checkstyle/images/checkstyle32.png")));
    createCenterPanel();
    setMinimumSize(new Dimension(950, 600));
    pack();
    setLocationByPlatform(true);
    // setResizable(false);

    setCategories(Arrays.asList("Annotation", "Blocks", "Coding", "Design", "Header", "Indentation", "Javadoc",
        "Metrics", "Modifier", "Naming", "Sizes", "Whitespace", "Other"));
    addSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        String category = categoryList.getSelectedValue();
        setVisibleRules(category, Arrays.asList("Rule 1", "Rule 2"));
      }
    }, ConfigurationListeners.CATEGORY_SELECT_LISTENER);
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

  protected JPanel createBottomRow() {
    JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    bottomRow.add(Box.createHorizontalStrut(4));
    bottomRow.add(new JLabel("Configuration Name:"));
    bottomRow.add(Box.createHorizontalStrut(4));
    bottomRow.add(this.configNameField);
    bottomRow.add(Box.createHorizontalStrut(4));

    JButton previewBtn = new JButton("Preview");
    previewBtn.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        previewBtnListeners.forEach(pbl -> pbl.actionPerformed(e));
      }
    });
    bottomRow.add(previewBtn);
    bottomRow.add(Box.createHorizontalStrut(4));

    JButton generateBtn = new JButton("Generate");
    generateBtn.addActionListener(new ActionListener(){
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

  protected JPanel createSelectRulesPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEADING));

    Font font = this.categoryLabel.getFont();
    this.categoryLabel.setFont(new Font(font.getName(), Font.BOLD, 48));

    topRow.add(Box.createHorizontalStrut(4));
    topRow.add(this.categoryLabel);
    topRow.add(Box.createHorizontalStrut(4));

    this.visibleRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.visibleRulesList.setLayoutOrientation(JList.VERTICAL);
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

  public void setCategories(Collection<String> categories) {
    this.categoryList.setListData(categories.toArray(new String[categories.size()]));
  }

  public void setVisibleRules(String category, Collection<String> rules) {
    this.categoryLabel.setText(category);
    this.visibleRulesList.setListData(rules.toArray(new String[rules.size()]));
  }

  public String getConfigurationName() {
    return this.configNameField.getText();
  }

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
