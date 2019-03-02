package org.infernus.idea.checkstyle.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyValueValidatorTest {
  @Test(expected = IllegalArgumentException.class)
  public void PropertyValueValidatorIllegalTypeTest() {
    PropertyValueValidator.validate("Not a type", "");
  }
  @Test
  public void PropertyValueValidatorIntegerTest() {
    assertTrue(PropertyValueValidator.validate("Integer", "20"));

    assertFalse(PropertyValueValidator.validate("Integer", "as213"));
    assertFalse(PropertyValueValidator.validate("Integer", "221as13"));
    assertFalse(PropertyValueValidator.validate("Integer", "221as"));
  }

  @Test
  public void PropertyValueValidatorStringTest() {
    assertTrue(PropertyValueValidator.validate("String", "this is a string"));
  }

  @Test
  public void PropertyValueValidatorStringSetTest() {
    assertTrue(PropertyValueValidator.validate("String", "IV_ASSIGN,PLUS_ASSIGN"));
  }

  @Test
  public void PropertyValueValidatorBooleanTest() {
    assertTrue(PropertyValueValidator.validate("Boolean", "true"));
    assertTrue(PropertyValueValidator.validate("Boolean", "random things"));
  }

  @Test
  public void PropertyValueValidatorIntSetTest() {
    assertTrue(PropertyValueValidator.validate("Integer Set", "42,666"));
    assertTrue(PropertyValueValidator.validate("Integer Set", "42"));
    assertTrue(PropertyValueValidator.validate("Integer Set", "666"));
    assertTrue(PropertyValueValidator.validate("Integer Set", "{}"));

    assertFalse(PropertyValueValidator.validate("Integer Set", "42, 666"));
  }

  @Test
  public void PropertyValueValidatorRegExpTest() {
    assertTrue(PropertyValueValidator.validate("Regular Expression", "^[0-9]+$"));
    assertTrue(PropertyValueValidator.validate("Regular Expression", "abcde"));

    assertFalse(PropertyValueValidator.validate("Regular Expression", "["));
  }

  @Test
  public void PropertyValueValidatorURLTest() {
    assertTrue(PropertyValueValidator.validate("URI", "http://checkstyle.sourceforge.net/property_types.html#regexp"));
    assertTrue(PropertyValueValidator.validate("URI", "org.infernus.idea.checkstyle.util"));
    assertTrue(PropertyValueValidator.validate("URI", "or_g.infernus.id_ea.ch12eckstyle.util"));
    assertTrue(PropertyValueValidator.validate("URI", "src/test/java/org/infernus/idea/checkstyle/util/PropertyValueValidatorTest.java"));

    assertFalse(PropertyValueValidator.validate("URI", "htt://checkstyle.sourceforge.net/property_types.html#regexp"));
    assertFalse(PropertyValueValidator.validate("URI", "org.infernus.idea.checkstyle."));
    assertFalse(PropertyValueValidator.validate("URI", "0org.infer156nus.idea.checkstyle"));
    assertFalse(PropertyValueValidator.validate("URI", "not exist file"));
  }
}
