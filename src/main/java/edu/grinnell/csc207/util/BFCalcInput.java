package edu.grinnell.csc207.util;

import java.io.PrintWriter;
import java.lang.Integer;

public class BFCalcInput {

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  public static void TypeCalcError(String[] line, boolean QuickCalc, PrintWriter error, String message){
    if (QuickCalc == true){
      error.print(BFCalcSystem.backtoString(line) + " FAILED ");
    } else {
      error.print("*** ERROR ");
    }

    error.print(message);

    if (QuickCalc == true){
      error.print("\n");
    } else {
      error.print(" ***\n");
    }

    error.flush();
  }

  //mega syntax check for input // takes in tokenized array 
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
      }

      else if (checkQUIT(line[index]) == true){
        previousQUIT = true;
      }

      else if (checkSTORE(line[index])){
        previousSTORE = true;
      }

      else { 
        if (errorPrintedTrackingInt == -1){
          
          TypeCalcError(line, QuickCalc, error, "[Invalid Expression/Command: This calculator only takes in \\\"STORE\\\", \\\"QUIT\\\", and expressions of lowercase letters, +, -, /, * ,and numbers.]");
        }
        return false;
      }
      index++;
    }
    
    if (line.length >= 2){
      if (previousBigFrac == true){
        if (checkOperandsStr(line[index])){
          previousBigFrac = false;
          previousOperand = true;
        }
        else{ // length too big for no error
          TypeCalcError(line, QuickCalc, error, "[Invalid Expression]");
          return false;
        }
      } 
      else if (previousSTORE == true){
        if (checkRegisterLetter(line[index])){
          previousRegister = true;
        } else {
          TypeCalcError(line, QuickCalc, error, "[STORE is missing a valid register]");
          return false;
        }
      }
      index++;
    }

    if ((previousQUIT == true) && (line.length != 1)){
      TypeCalcError(line, QuickCalc, error, "[QUIT command is by itself]");
      return false;
    }

    if ((previousSTORE == true) && (line.length != 2)){
      TypeCalcError(line, QuickCalc, error, "[Can ONLY call a register on a STORE command]");
      return false;
    }

    while(index < line.length){
      if (previousOperand == true){
        if (checkFrac(line[index], error, line, QuickCalc) > 0){
          previousBigFrac = true;
          previousOperand = false;
        } else {
          return false;
        }
      }
      else if (previousBigFrac == true){
        if (checkOperandsStr(line[index])){
          previousBigFrac = false;
          previousOperand = true;
          if (index == line.length - 1){
            TypeCalcError(line, QuickCalc, error, "[Invalid Expression]");
            return false;
          }
        }
      }
      index++;
    }


    return true;
  }



  // check Fractions syntax

  public static int checkFrac(String str, PrintWriter error, String[] line, boolean QuickCalc){
    String errorMsg;
    
    if (QuickCalc){
      errorMsg = BFCalcSystem.backtoString(line) + " FAILED [Invalid Expression]";
    } else {
      errorMsg = "*** ERROR [Invalid Expression] ***";
    }

    boolean wholeNum = wholeNumCheck(str);
    int fracSeparatorLocation = fracSeparatorWhere(str);

    for (int j = 0; j < str.length(); j++){
      if (checkBigFrac(str.charAt(j)) == false){
        
        return -1;
      }
    }

    if (wholeNum == true) {
      if (checkWholeNumArea(str, error, errorMsg, false) == 0){
        return 0;
      }

      // Checks if whole number has letter
      if (checkWholeNumArea(str, error, errorMsg, false) == 2){
        return 2;
      }
    }

    // Check syntax for fraction
    // fraction can't have register in numerator or denominator (only choice 1)
    if (wholeNum == false){
      if (checkWholeNumArea(str.substring(0, fracSeparatorLocation), error, errorMsg, false) != 1){
        return 0;
      }
      if (checkWholeNumArea(str.substring(fracSeparatorLocation + 1, str.length()), error, errorMsg, true) != 1){
        return 0;
      }
    }


    return 1;
  }

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
      }

      if ((str.charAt(i) == '-') && (i != 0)){
        error.println(errorMsg);
        error.flush();
        return 0;
      };
    }

    if ((Integer.parseInt(str) == 0) && (denominator == true)){
      return 0;
    }
    
    return 1;
  }

  public static boolean wholeNumCheck(String str){
    boolean check = true;

    for (int i = 0; i < str.length(); i++){
      if (checkFractionSeparator(str.charAt(i)) == true){
        check = false;
      }
    }
    return check;

  }

  public static int fracSeparatorWhere(String str){
    return str.indexOf('/');
  }



  // Checking Methods: strings

  public static boolean checkOperandsStr (String str){
    if ((str.length() == 1) && checkOperands(str.charAt(0)) == true){
      return true;
    }
    return false;
  }

  public static boolean checkStr(String str, String compare){
    if (str.compareTo(compare) == 0){
      return true;
    }
    return false;
  }

  public static boolean checkQUIT(String str){
    return checkStr(str, "QUIT");
  }

  public static boolean checkSTORE(String str){
    return checkStr(str, "STORE");
  }
  
  public static boolean checkRegisterLetter(String str){
    if ((str.length() == 1) && (checkLowLetters(str.charAt(0)))){
      return true;
    }
    return false;
  }



// Checking Methods: char

  public static boolean checkStuff(char check, char[] stuff){
    boolean present = false;
    for (int i = 0; i < stuff.length; i ++){
      present = (present || (check == stuff[i]));
    }
    return present;
  }

  public static boolean checkOperands(char check){
    char[] operands = {'/', '+', '*', '-'};
    return checkStuff(check, operands);
  }

  public static boolean checkLowLetters(char check){
    return (check <= 'z') && (check >= 'a');
  }

  public static boolean checkNums(char check){
    return (check <= '9') && (check >= '0');
  }

  public static boolean checkFractionSeparator(char check){
    return (check == '/');
  }

  public static boolean checkNegativeSign(char check){
    return (check == '-');
  }

  public static boolean checkBigFrac(char check){
    return checkNums(check) || (checkLowLetters(check) || (checkFractionSeparator(check)) || (checkNegativeSign(check)));
  }




}
