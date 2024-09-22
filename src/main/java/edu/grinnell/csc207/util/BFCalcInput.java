package edu.grinnell.csc207.util;

import java.io.PrintWriter;
import java.lang.Integer;

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
   * @param QuickCalc A boolean saying if QuickCalculation will take place or not.
   * @param error A PrintWriter that prints errors.
   * @param message A string to be printed.
   */
  public static void TypeCalcError(String[] line, boolean QuickCalc, PrintWriter error, String message){
    // Checks if QuickCalculation is taking place. Prints the first part of the error message.
    if (QuickCalc == true){
      error.print(BFCalcSystem.backtoString(line) + " FAILED ");
    } // if 
    else {
      error.print("*** ERROR ");
    } // if/else

    error.print(message);

    // Checks if QuickCalculation is taking place. Prints the last part of the error message.
    if (QuickCalc == true){
      error.print("\n");
    } // if
    else {
      error.print(" ***\n");
    } // if/else

    error.flush();
  } // TypeCalcError(String[], boolean, PrintWriter, String)

  //mega syntax check for input // takes in tokenized array 

  /**
   * Checks if syntax of line is correct and prints error otherwise.
   * 
   * 
   * @param line A tokenized array of Strings.
   * @param error Used to print errors.
   * @param QuickCalc A boolean saying if QuickCalculation will take place or not.
   * @return boolean saying if the syntax of line is correct.
   */
  public static boolean orderMatters(String[] line, PrintWriter error, boolean QuickCalc){
    boolean previousRegister = false; // kinda useless but oh well
    boolean previousBigFrac = false;
    boolean previousOperand = false;
    boolean previousQUIT = false;
    boolean previousSTORE = false;

    int index = 0;

    int errorPrintedTrackingInt = 0;

    
    if (line.length >= 1){
      if ((errorPrintedTrackingInt = checkFrac(line[index], error, line, QuickCalc)) > 0){
        previousBigFrac = true;
      } // if

      else if (checkQUIT(line[index]) == true){
        previousQUIT = true;
      } // if/elseif

      else if (checkSTORE(line[index])){
        previousSTORE = true;
      } // if/elseif/elseif

      else { 
        if (errorPrintedTrackingInt == -1){
          
          TypeCalcError(line, QuickCalc, error, "[Invalid Expression/Command: This calculator only takes in \\\"STORE\\\", \\\"QUIT\\\", and expressions of lowercase letters, +, -, /, * ,and numbers.]");
        } // if
        return false;
      } // if/else
      index++;
    } // if/elseif/elseif/else
    
    if (line.length >= 2){
      if (previousBigFrac == true){
        if (checkOperandsStr(line[index])){
          previousBigFrac = false;
          previousOperand = true;
        } // if
        else{ // length too big for no error
          TypeCalcError(line, QuickCalc, error, "[Invalid Expression]");
          return false;
        } // if/else
      } // if
      else if (previousSTORE == true){
        if (checkRegisterLetter(line[index])){
          previousRegister = true;
        } else {
          TypeCalcError(line, QuickCalc, error, "[STORE is missing a valid register]");
          return false;
        } // if/else
      } // if/elseif
      index++;
    } // if

    //prints Error if input for QUIT is incorrect.
    if ((previousQUIT == true) && (line.length != 1)){
      TypeCalcError(line, QuickCalc, error, "[QUIT command is by itself]");
      return false;
    } // if

    //prints Error if input for STORE is incorrect.
    if ((previousSTORE == true) && (line.length != 2)){
      TypeCalcError(line, QuickCalc, error, "[Can ONLY call a register on a STORE command]");
      return false;
    } // if

    while(index < line.length){
      if (previousOperand == true){
        if (checkFrac(line[index], error, line, QuickCalc) > 0){
          previousBigFrac = true;
          previousOperand = false;
        } else {
          return false;
        } // if/else
      } // if
      else if (previousBigFrac == true){
        if (checkOperandsStr(line[index])){
          previousBigFrac = false;
          previousOperand = true;
          if (index == line.length - 1){
            TypeCalcError(line, QuickCalc, error, "[Invalid Expression]");
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
   * Checks a string to see if the syntax follows that of a fraction. Prints and error based on QuickCalc if it fails.
   * 
   * @param str A String to be Checked.
   * @param error prints errors.
   * @param line A tokenized array of Strings.
   * @param QuickCalc A boolean saying if QuickCalculation will take place or not.
   * @return An int value corresponding to 0: not frac, -1: not frac and no error printed, 1 frac with no lowercase letteres, 2 frac with lowercase letters.
   */
  public static int checkFrac(String str, PrintWriter error, String[] line, boolean QuickCalc){
    String errorMsg;
    
    if (QuickCalc){
      errorMsg = BFCalcSystem.backtoString(line) + " FAILED [Invalid Expression]";
    } else {
      errorMsg = "*** ERROR [Invalid Expression] ***";
    } // if/else

    boolean wholeNum = wholeNumCheck(str);
    int fracSeparatorLocation = fracSeparatorWhere(str);

    for (int j = 0; j < str.length(); j++){
      if (checkBigFrac(str.charAt(j)) == false){
        
        return -1;
      } // if
    } // for

    if (wholeNum == true) {
      if (checkWholeNumArea(str, error, errorMsg, false) == 0){
        return 0;
      } // if

      // Checks if whole number has letter
      if (checkWholeNumArea(str, error, errorMsg, false) == 2){
        return 2;
      } // if
    } // if

    // Check syntax for fraction
    // fraction can't have register in numerator or denominator (only choice 1)
    if (wholeNum == false){
      if (checkWholeNumArea(str.substring(0, fracSeparatorLocation), error, errorMsg, false) != 1){
        return 0;
      } // if
      if (checkWholeNumArea(str.substring(fracSeparatorLocation + 1, str.length()), error, errorMsg, true) != 1){
        return 0;
      } // if
    } // if


    return 1;
  } // checkFrac(String, PrintWriter, String[], boolean)

  //returns 0 for false, 1 for lowercase not present and true, 2 for lowercase present and true 
  public static int checkWholeNumArea(String str, PrintWriter error, String errorMsg, boolean denominator){
    
    for (int i = 0; i < str.length(); i++){

      //  Makes sure that if there is a lowercase letter, there is only one. Prints error if not. Also. no negative lowercase letters.
      if ((str.length() != 1) && (checkLowLetters(str.charAt(i)) == true)){
        error.println(errorMsg);
        error.flush();
        return 0;
        // CANT HAVE LETTER WITH A NUMBER OR ANOTHER LETTER
      }  // This is only a letter
      else if ((str.length() == 1) && (checkLowLetters(str.charAt(i))) == true){
        return 2;
      } // if/else if

      // checks if there is a - not at the front
      if ((str.charAt(i) == '-') && (i != 0)){
        error.println(errorMsg);
        error.flush();
        return 0;
      } // if
    } // for

    if ((Integer.parseInt(str) == 0) && (denominator == true)){
      return 0;
    } // if
    
    return 1;
  } // 

  /**
   * Checks if str is a whole number.
   * 
   * @param str to be checked
   * @return a boolean
   */
  public static boolean wholeNumCheck(String str){
    boolean check = true;

    for (int i = 0; i < str.length(); i++){
      if (checkFractionSeparator(str.charAt(i)) == true){
        check = false;
      } // if
    } // for
    return check;

  } // wholeNumCheck(String)
  
  /**
   * Returns the location of /
   * 
   * @param str A String to be looked at.
   * @return The location of / or -1 if / does not exist.
   */
  public static int fracSeparatorWhere(String str){
    return str.indexOf('/');
  } // fracSeparatorWhere(String)



  // Checking Methods: strings

  /**
   * Returns a boolean value based on whether or not the first char of a String is an operand.
   * 
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkOperandsStr (String str){
    if ((str.length() == 1) && checkOperands(str.charAt(0)) == true){
      return true;
    } // if
    return false;
  } // checkOperandsStr

  /**
   * Returns a boolean value based on whether or not Str is equal to String compare.
   * 
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkStr(String str, String compare){
    // Checks if two strings are equal
    if (str.compareTo(compare) == 0){
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
  public static boolean checkQUIT(String str){
    return checkStr(str, "QUIT");
  } // checkQUIT(String)

  /**
   * Returns a boolean value based on whether or not Str is equal to "STORE".
   * 
   * @param str Str to be checked.
   * @return A boolean value.
   */
  public static boolean checkSTORE(String str){
    return checkStr(str, "STORE");
  } // checkSTORE(String)
  
  /**
   * Returns a boolean value corresponding to whether or not str is a register letter.
   * 
   * @param str Str to be checked.
   * @return a boolean value.
   */
  public static boolean checkRegisterLetter(String str){
    // check if it is a lowercase letter and the only thing in the string
    if ((str.length() == 1) && (checkLowLetters(str.charAt(0)))){
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
  public static boolean checkStuff(char check, char[] stuff){
    boolean present = false;
    for (int i = 0; i < stuff.length; i ++){
      present = (present || (check == stuff[i]));
    }
    return present;
  } // checkStuff(char, char[])

  /**
   * Checks if char is of the correct syntax for operands.
   * 
   * @param check a char that will be checked to see if it of the correct for operands.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkOperands(char check){
    char[] operands = {'/', '+', '*', '-'};
    return checkStuff(check, operands);
  } // checkOperands(char)

  /**
   * Checks if char is of the correct syntax for lowercase letters.
   * 
   * @param check a char that will be checked to see if it of the correct syntax for lowercase letters. 
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkLowLetters(char check){
    return (check <= 'z') && (check >= 'a');
  } // checkLowLetters(char)

  /**
   * Checks if char is of the correct syntax for Numbers.
   * 
   * @param check a char that will be checked to see if it of the correct syntax for Numbers.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkNums(char check){
    return (check <= '9') && (check >= '0');
  } // checkNums(char)

  /**
   * Checks if char is of the correct syntax for /.
   * 
   * @param check a char that will be checked to see if it of the correct syntax for /.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkFractionSeparator(char check){
    return (check == '/');
  } // checkFractionSeparator(char)

  /**
   * Checks if char is of the correct syntax for -.
   * 
   * @param check a char that will be checked to see if it of the correct syntax for -.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkNegativeSign(char check){
    return (check == '-');
  } // checkNegativeSign(char)

  
  /**
   * Checks if char is of the correct syntax for BigFractions.
   * 
   * @param check a char that will be checked to see if it of the correct syntax for BigFractions.
   * @return a boolean value representing if the char is of that syntax.
   */
  public static boolean checkBigFrac(char check){
    return checkNums(check) || (checkLowLetters(check) || (checkFractionSeparator(check)) || (checkNegativeSign(check)));
  } // checkBigFrac(char)




}
