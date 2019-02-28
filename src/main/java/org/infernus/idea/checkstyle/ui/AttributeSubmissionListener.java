package org.infernus.idea.checkstyle.ui;

import org.infernus.idea.checkstyle.model.XMLConfig;

/**
 * This class represents a listener that gets notified whenever the Attributes
 * Editor Dialog is submitted.
 */
public abstract class AttributeSubmissionListener {
  /**
   * The action to perform.
   * 
   * @param rule The rule, along with attribute values, that was submitted
   */
  public abstract void actionPerformed(XMLConfig rule);
}