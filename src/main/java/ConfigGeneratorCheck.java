import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;


public class ConfigGeneratorCheck {
  public static void main(String[] args) throws Exception{
    //Method configLoader = ConfigurationLoader.class.getMethod("loadConfiguration", String.class, PropertyResolver.class);
    //Configuration testing = (Configuration) configLoader.invoke(null, "src/main/java/org/infernus/idea/checkstyle/checkstyle.xml", null);

    Configuration testing = new DOME();
    testing = ConfigurationLoader.loadConfiguration("src/main/java/org/infernus/idea/checkstyle/checkstyle.xml", null);
    System.out.println(testing.getName());
    System.out.println(testing.getChildren()[0].getName());
  }

  static class DOME implements Configuration {
    /**
     * The set of attribute names.
     * @return The set of attribute names, never null.
     */
    public String[] getAttributeNames() {
      return new String[]{"test"};
    }

    /**
     * The attribute value for an attribute name.
     * @param name the attribute name
     * @return the value that is associated with name
     * @throws CheckstyleException if name is not a valid attribute name
     */
    public String getAttribute(String name) throws CheckstyleException {
      return "testing2";
    }

    /**
     * The set of child configurations.
     * @return The set of child configurations, never null.
     */
    public Configuration[] getChildren() {
      return null;
    }

    /**
     * The name of this configuration.
     * @return The name of this configuration.
     */
    public String getName() {
      return "name";
    }

    /**
     * Returns an unmodifiable map instance containing the custom messages
     * for this configuration.
     * @return unmodifiable map containing custom messages
     */
    public ImmutableMap<String, String> getMessages() {
      return null;
    }
  }
}
