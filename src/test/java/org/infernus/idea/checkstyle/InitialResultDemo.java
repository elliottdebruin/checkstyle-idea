package org.infernus.idea.checkstyle;

import org.infernus.idea.checkstyle.util.ConfigReader;
import org.infernus.idea.checkstyle.util.ConfigWriter;
import org.junit.Test;

public class InitialResultDemo {
  @Test
  public void InitialResultDemo() throws Exception{
    ConfigWriter.saveConfig("regenRule.xml", ConfigReader.readConfig("google_checkstyle.xml"));
  }
}