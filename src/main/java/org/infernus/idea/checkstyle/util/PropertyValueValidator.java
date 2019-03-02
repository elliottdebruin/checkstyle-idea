package org.infernus.idea.checkstyle.util;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.validator.routines.UrlValidator;

public class PropertyValueValidator {

  /**
   * Validates the value according to the type
   * @param type - The type of the value
   * @param value - The value to validate
   * @return true when the value passed the according validation
   * @throws IllegalArgumentException - When type is not a checkstyle property type
   */
  public static boolean validate(String type, String value) throws IllegalArgumentException {
    if (type.equals("Integer")) {
      return Pattern.matches("^[0-9]+$", value);
    } else if (type.equals("String") || type.equals("String Set")) {
      return true;
    } else if (type.equals("Boolean")) {
      return true; // per checkstyle's document, anything other than yes, true, on will be take as false
                   // therefore any value for boolean is technically correct
    } else if (type.equals("Integer Set")) {
      if (value.contains(",")) {
        String ints[] = value.split(",");

        for (String integer : ints) {
          if (!Pattern.matches("^[0-9]+$", integer)) {
            return false;
          }
        }

        return true;
      }

      return Pattern.matches("^[0-9]+$", value) || value.equals("{}");
    } else if (type.equals("Regular Expression")) {
      try {
        Pattern.compile(value);
      } catch (PatternSyntaxException e) {
        return false;
      }

      return true;
    } else if (type.equals("URI")) {
      String[] schemes = {"http","https"};
      UrlValidator urlValidator = new UrlValidator(schemes);

      return urlValidator.isValid(value) || Pattern.matches("^([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*$", value)
              || (new File(value).exists());
    }

    throw new IllegalArgumentException();
  }
}
