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
    } else if (type.equals("lineSeparator")) {
      return value.equals("crlf") || value.equals("cr") || value.equals("lf") || value.equals("lf_cr_crlf")
              || value.equals("system");
    } else if (type.equals("Pad Policy")) {
      return value.equals("nospace") || value.equals("space");
    } else if (type.equals("Wrap Operator Policy")) {
      return value.equals("nl") || value.equals("eol");
    } else if (type.equals("Block Policy")) {
      return value.equals("text") || value.equals("statement");
    } else if (type.equals("Scope")) {
      return value.equals("nothing") || value.equals("public") || value.equals("protected")
              || value.equals("package") || value.equals("private") || value.equals("anoninner");
    } else if (type.equals("Access Modifier Set")) {
      String split[] = value.split(", ");

      if (split.length == 0) {
        return value.equals("public") || value.equals("protected")
                || value.equals("package") || value.equals("private");
      }

      for (String subValue : split) {
        if (!(subValue.equals("public") || subValue.equals("protected")
                || subValue.equals("package") || subValue.equals("private"))) {
          return false;
        }
      }
      return true;
    } else if (type.equals("Severity")) {
      return value.equals("ignore") || value.equals("info") || value.equals("warning") || value.equals("error");
    } else if (type.equals("Import Order Policy")) {
      return value.equals("top") || value.equals("above") || value.equals("inflow") || value.equals("under")
              || value.equals("bottom");
    } else if (type.equals("Element Style")) {
      return value.equals("expanded") || value.equals("compact") || value.equals("compact_no_array")
              || value.equals("ignore");
    } else if (type.equals("Closing Parens") || type.equals("Trailing Comma")) {
      return value.equals("always") || value.equals("never") || value.equals("ignore");
    }

    throw new IllegalArgumentException();
  }
}
