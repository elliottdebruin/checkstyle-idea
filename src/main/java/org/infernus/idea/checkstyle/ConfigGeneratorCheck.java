package org.infernus.idea.checkstyle;

import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.Configuration;


public class ConfigGeneratorCheck {
  public static void main(String[] args) throws Exception{
    Configuration testing = ConfigurationLoader.loadConfiguration("src/main/java/org/infernus/idea/checkstyle/checkstyle.xml", null);

    System.out.println(testing.getName());
    System.out.println(testing.getChildren()[0].getName());
  }
}
