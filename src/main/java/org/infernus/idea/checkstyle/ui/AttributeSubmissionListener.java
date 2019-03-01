package org.infernus.idea.checkstyle.ui;

import org.infernus.idea.checkstyle.model.ConfigRule;
import org.infernus.idea.checkstyle.model.XMLConfig;

/**
 * This class represents a listener that gets notified whenever the Attributes
 * Editor Dialog is submitted.
 */
public interface AttributeSubmissionListener {
  /**
   * The action to perform on successful submission.
   * 
   * @param configRule The name and description of the rule that was submitted
   * @param xmlRule The rule, along with attribute values, that was submitted
   * @param isNewRule Whether this rule is not already active
   */
  public void attributeSubmitted(ConfigRule configRule, XMLConfig xmlRule, boolean isNewRule);

  /**
   * The action to perform on cancel.
   * 
   * @param configRule The name and description of the rule that was submitted
   * @param xmlRule The rule, along with attribute values, that was submitted
   * @param isNewRule Whether this rule is not already active
   */
  public void attributeCancelled(ConfigRule configRule, XMLConfig xmlRule, boolean isNewRule);
}