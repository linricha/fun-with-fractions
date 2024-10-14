package edu.grinnell.csc207.util;

import java.io.PrintWriter;

/**
 * A class that checks if the inputs to BFCalcSystem are of the correct syntax.
 *
 * @author Richard Lin
 */
public class BFCalcInput {

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Prints the error message corresponding to QuickCalc.
   *
   * @param line A tokenized array of Strings.
   * @param quickCalc A boolean saying if QuickCalculation will take place or not.
   * @param error A PrintWriter that prints errors.
   * @param message A string to be printed.
   */
  public static void typeCalcError(
      String[] line, boolean quickCalc, PrintWriter error, String message) {
    // Checks if QuickCalculation is taking place. Prints the first part of the error message.
    if (quickCalc) {
      error.print(BFCalcSystem.backtoString(line) + " FAILED ");
    } else {
      error.print("*** ERROR ");
    } // if/else

    error.print(message);

    // Checks if QuickCalculation is taking place. Prints the last part of the error message.
    if (quickCalc) {
      error.print("\n");
    } else {
      error.print(" ***\n");
    } // if/else

    error.flush();
  } // TypeCalcError(String[], boolean, PrintWriter, String)

  /**
   * Checks if syntax of line is correct and prints error otherwise.
   *
   * @param line A tokenized array of Strings.
   * @param error Used to print errors.
   * @param quickCalc A boolean saying if QuickCalculation will take place or not.
   * @return boolean saying if the syntax of line is correct.
   */
  public static boolean orderMatters(String[] line, PrintWriter error, boolean quickCalc) {
    boolean previousRegister = false;
    boolean previousBigFrac = false;
    boolean previousOperand = false;
    boolean previousQUIT = false;
    boolean previousSTORE = false;

    int index = 0;

    int errorPrintedTrackingInt = 0;


    if (line.length >= 1) {
      errorPrintedTrackingInt = checkFrac(line[index], error, line, quickCalc);
      if (errorPrintedTrackingInt > 0) {
        previousBigFrac = true;
      } else if (checkQUIT(line[index])) {
        previousQUIT = true;
      } else if (checkSTORE(line[index])) {
        previousSTORE = true;
      } else {
        if (errorPrintedTrackingInt == -1) {

          typeCalcError(line, quickCalc, error,
              "[Invalid Expression/Command: This calculator only takes in \\\"STORE\\\","
              + "\\\"QUIT\\\", and expressions of lowercase letters, +, -, /, * ,and numbers.]");
        } // if
        return false;
      } // if/else-if/else-if/else
      index++;
    } // if/else

    if (line.length >= 2) {
      if (previousBigFrac) {
        if (checkOperandsStr(line[index])) {
          previousBigFrac = false;
          previousOperand = true;
        } else { // length too big for no error
          typeCalcError(line, quickCalc, error, "[Invalid Expression]");
          return false;
        } // if/else
      } else if (previousSTORE) {
        if (checkRegisterLetter(line[index])) {
          previousRegister = true;
        } else {
          typeCalcError(line, quickCalc, error, "[STORE is missing a valid register]");
          return false;
        } // if/else
      } // if/elseif
      index++;
    } // if

    //prints Error if input for QUIT is incorrect.
    if ((previousQUIT) && (line.length != 1)) {
      typeCalcError(line, quickCalc, error, "[QUIT command is by itself]");
      return false;
    } // if

    //prints Error if input for STORE is incorrect.
    if ((previousSTORE) && (line.length != 2)) {
      typeCalcError(line, quickCalc, error, "[Can ONLY call a register on a STORE command]");
      return false;
    } // if

    while (index < line.length) {
      if (previousOperand) {
        if (checkFrac(line[index], error, line, quickCalc) > 0) {
          previousBigFrac = true;
          previousOperand = false;
        } else {
          return false;
        } // if/else
      } else if (previousBigFrac) {
        if (checkOperandsStr(line[index])) {
          previousBigFrac = false;
          previousOperand = true;
          if (index == line.length - 1) {
            typeCalcError(line, quickCalc, error, "[Invalid Expression]");
            return false;
          } // if
        } // if
      } // if/elseif
      index++;
    } // while


    return true;
  } // orderMatters(String[], PrintWriter, boolean)



  // check Fractions syntax

  /**
   * Checks a string to see if the syntax follows that of a fraction.
   * Prints and error based on QuickCalc if it fails.
   *
   * @param str A String to be Checked.
   * @param error prints errors.
   * @param line A tokenized array of Strings.
   * @param quickCalc A boolean saying if QuickCalculation will take place or not.
   * @return An int value corresponding to 0: not frac, -1: not frac and no error printed,
   * 1 frac with no lowercase letteres, 2 frac with lowercase letters.
   */
  public static int checkFrac(String str, PrintWriter error, String[] line, boolean quickCalc) {
    String errorMsg;

    if (quickCalc) {
      errorMsg = BFCalcSystem.backtoString(line) + " FAILED [Invalid Expression]";
    } else {
      errorMsg = "*** ERROR [Invalid Expression] ***";
    } // if/else

    boolean wholeNum = wholeNumCheck(str);
    int fracSeparatorLocation = fracSeparatorWhere(str);

    for (int j = 0; j < str.length(); j++) {
      if (!checkBigFrac(str.charAt(j))) {

        return -1;
      } // if
    } // for

    if (wholeNum) {
      if (checkWholeNumArea(str, error, errorMsg, false) == 0) {
        return 0;
      } // if

      // Checks if whole number has letter
      if (checkWholeNumArea(str, error, errorMsg, false) == 2) {
        return 2;
      } // if
    } // if

    // Check syntax for fraction
    // fraction can't have register in numerator or denominator (only choice 1)
    if (!wholeNum) {
      if (checkWholeNumArea(str.substring(0, fracSeparatorLocation), error, errorMsg, false) != 1) {
        return 0;
      } // if
      if (checkWholeNumArea(
          str.substring(fracSeparatorLocation + 1, str.length()), error, errorMsg, true) != 1) {
        return 0;
      } // if
    } // if


    return 1;
  } // checkFrac(String, PrintWriter, String[], boolean)


  /**
   * Checks an area of numbers to see if they represent a whole number.
   * Returns 0 if str is not a whole number, 1 if str is a whole number
   * and lowercase is not present, and 2 if str is lowercase and is a whole number.
   *
   * @param str The string to be checked.
   * @param error Prints error messages.
   * @param errorMsg The error message to print.
   * @param denominator Boolean seeing if str is a denominator.
   * @return An int represing if the area to be checked is whole number or not.
   */
  public static int checkWholeNumArea(
      String str, PrintWriter error, String errorMsg, boolean denominator) {

    for (int i = 0; i < str.length(); i++) {

      //  Makes sure that if there is a lowercase letter, there is only one.
      // Prints error if not. Also. no negative lowercase letters.
      if ((str.length() != 1) && (checkLowLetters(str.charAt(i)))) {
        error.println(errorMsg);
        error.flush();
        return 0;
        // CANT HAVE LETTER WITH A NUMBER OR ANOTHER LETTER
      }  else if ((str.length() == 1) && (checkLowLetters(str.charAt(i)))) {
        //this is only a letter
        return 2;
      } // if/else-if

      // checks if there is a - not at the front
      if ((str.charAt(i) == '-') && (i != 0)) {
        error.println(errorMsg);
        error.flush();
        return 0;
      } // if
    } // for

    if ((Integer.parseInt(str) == 0) && (denominator)) {
      return 0;
    } // if

    return 1;
  } // checkWholeNumArea(String, PrintWriter, String, boolean)

  /**
   * Checks if str is a whole number.
   *
   * @param str to be checked
   * @return a boolean
   */
  public static boolean wholeNumCheck(String str) {
    boolean check = true;

    for (int i = 0; i < str.length(); i++) {
      if (checkFractionSeparator(str.charAt(i))) {
        check = false;
      } // if
    } // for
    return check;

  } // wholeNumCheck(String)

  /**
   * Returns the location of /.
   *
   * @param str A String to be looked at.
   * @return The location of / or -1 if / does not exist.
   */
  public static int fracSeparatorWhere(String str) {
    return str.indexOf('/');
  } // fracSeparatorWhere(String)



  // Checking Methods: strings

  /**
   * Returns a boolean value based on whether or not the first char of a String is an operand.
   *
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkOperandsStr(String str) {
    if ((str.length() == 1) && checkOperands(str.charAt(0))) {
      return true;
    } // if
    return false;
  } // checkOperandsStr

  /**
   * Returns a boolean value based on whether or not Str is equal to String compare.
   *
   * @param str Str to be checked.
   * @param compare Str to be cmopared to.
   * @return A boolean value.
   */
  public static boolean checkStr(String str, String compare) {
    // Checks if two strings are equal
    if (str.compareTo(compare) == 0) {
      return true;
    } // if
    return false;
  } // checkStr(String, String)

  /**
   * Returns a boolean value based on whether or not Str is equal to "QUIT".
   *
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkQUIT(String str) {
    return checkStr(str, "QUIT");
  } // checkQUIT(String)

  /**
   * Returns a boolean value based on whether or not Str is equal to "STORE".
   *
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkSTORE(String str) {
    return checkStr(str, "STORE");
  } // checkSTORE(String)

  /**
   * Returns a boolean value corresponding to whether or not str is a register letter.
   *
   * @param str Str to be checked.
   * @return a boolean value.
   */
  public static boolean checkRegisterLetter(String str) {
    // check if it is a lowercase letter and the only thing in the string
    if ((str.length() == 1) && (checkLowLetters(str.charAt(0)))) {
      return true;
    } // if
    return false;
  } // checkRegisterLetter(String)



// Checking Methods: char
  /**
   * Checks if char is of the correct syntax for something.
   *
   * @param check a char that will be checked to see if it of the correct syntax.
   * @param stuff The char[] that will be used for checking.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkStuff(char check, char[] stuff) {
    boolean present = false;
    for (int i = 0; i < stuff.length; i++) {
      present = (present || (check == stuff[i]));
    } // for
    return present;
  } // checkStuff(char, char[])

  /**
   * Checks if char is of the correct syntax for operands.
   *
   * @param check a char that will be checked to see if it of the correct for operands.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkOperands(char check) {
    char[] operands = {'/', '+', '*', '-'};
    return checkStuff(check, operands);
  } // checkOperands(char)

  /**
   * Checks if char is of the correct syntax for lowercase letters.
   *
   * @param check
   *  a char that will be checked to see if it of the correct syntax for lowercase letters.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkLowLetters(char check) {
    return (check <= 'z') && (check >= 'a');
  } // checkLowLetters(char)

  /**
   * Checks if char is of the correct syntax for Numbers.
   *
   * @param check a char that will be checked to see if it of the correct syntax for Numbers.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkNums(char check) {
    return (check <= '9') && (check >= '0');
  } // checkNums(char)

  /**
   * Checks if char is of the correct syntax for /.
   *
   * @param check a char that will be checked to see if it of the correct syntax for /.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkFractionSeparator(char check) {
    return (check == '/');
  } // checkFractionSeparator(char)

  /**
   * Checks if char is of the correct syntax for -.
   *
   * @param check a char that will be checked to see if it of the correct syntax for -.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkNegativeSign(char check) {
    return (check == '-');
  } // checkNegativeSign(char)


  /**
   * Checks if char is of the correct syntax for BigFractions.
   *
   * @param check a char that will be checked to see if it of the correct syntax for BigFractions.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkBigFrac(char check) {
    return checkNums(check) || (checkLowLetters(check)
        || (checkFractionSeparator(check)) || (checkNegativeSign(check)));
  } // checkBigFrac(char)

} // class BFCalcInput
