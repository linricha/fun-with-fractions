package edu.grinnell.csc207.util;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Objects;
import java.lang.Integer;


public class BFCalcSystem {

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  // turn inputs into array
  public static String[] readIt2(Scanner input){
    return input.tokens().toArray(String[] :: new);
  }

  public static String backtoString(String[] arr){
    String str = "";

    for(int i = 0; i < arr.length; i++){
      if ((i == 0) || (i == arr.length - 1)){
        str = str.concat(arr[i]);
      } 
      else {
        str = str.concat(" "+ arr[i]);
      }
      
    }

    return str;
  }


  // Assumption that str has valid syntax for BigFraction
  public static BigFraction giveBigFrac(String str, PrintWriter error, BFRegisterSet record){
    boolean wholeNum = BFCalcInput.wholeNumCheck(str);

    // int fracSeparatorLocation = 0; MOVE MAYBE TO wholeNumCheck if need this

    if (wholeNum == true){
      if (BFCalcInput.checkLowLetters(str.charAt(0)) == true){
        if (record.checkRecord(str.charAt(0)) == true){ 
          return record.get(str.charAt(0)); // need to make check that record is filled for that area.
        }
        else{
          error.println("*** ERROR [Invalid Expression: Register value not initiated] ***");
          error.flush();
          return null;
        }
      } 
      else{
        return new BigFraction(Integer.parseInt(str), 1);
      }
    }
    

    return new BigFraction(str);
  }


  //Assume string is all correct format
  // Last int in array is number of times * or / occurs
  public static int[] operatorAssign(String[] line){
    int doFirstCount = 0;

    int[] orderOfOperations = new int[((line.length - 1)/2) + 1];
    for (int j = 0; j < orderOfOperations.length - 1; j++){
      switch (line[(j * 2) + 1].charAt(0)){
        case '*':
          orderOfOperations[j] = 1;
          doFirstCount++;
          break;
        case '/':
          orderOfOperations[j] = 2;
          doFirstCount++;
          break;
        case '+':
          orderOfOperations[j] = 3;
          break;
        case '-':
          orderOfOperations[j] = 4;
          break;
        default:
          break;
      }
    }
    orderOfOperations[orderOfOperations.length - 1] = doFirstCount;
  
    return orderOfOperations;
    
  }
  
  // Assume string is a valid string input of expressions
  public static BigFraction[] changeToBigFractions(String[] line, PrintWriter error, BFRegisterSet record){
    //BFRegister placeholder = new BFRegister();
    int lineLen;
    if (line.length > 1){
      lineLen = ((line.length - 1) / 2) + 1;
    } else {
      lineLen = 1; // single char (MAYBE NOT NECESSARY)
    }
    BigFraction[] bigFracsOnly = new BigFraction[lineLen];

    for(int i = 0; i < bigFracsOnly.length; i++){
      if (Objects.equals(giveBigFrac(line[2*i],error, record), null) == false){
        bigFracsOnly[i] = giveBigFrac(line[2*i], error, record);
      }
      else {
        return null;
      }
    }

    return bigFracsOnly;
  }

  public static void closeObjects(PrintWriter pen1, PrintWriter pen2, Scanner sc1){
    pen1.close();
    pen2.close();
    sc1.close();
  }


  public static void multiplyFracsFirst(BigFraction[] simplifiedExpression, int[] operators, BFCalculator calcButton){
    for (int j = 0, k = 0; 0 < operators[operators.length - 1];){
      if (operators[j] <= 2){
        
        calcButton.add(simplifiedExpression[k]);
        simplifiedExpression[k] = new BigFraction(0, 1);
        k++;

        if (operators[j] == 1){
          calcButton.multiply(simplifiedExpression[k]);
        } else {
          calcButton.divide(simplifiedExpression[k]);
        }
        operators[j] = 3; // add
        operators[operators.length - 1]--;
        simplifiedExpression[k] = calcButton.get();
      
        j++;
      }
      else {
        j++;
        k++;
      }
    }
    calcButton.clear();
  }

  // mutator
  public static void addAllFracLeft(BigFraction[] simplifiedExpression, int[] operators, BFCalculator calcButton){
    for (int j = 0, k = 0; k < simplifiedExpression.length; k++, j++){
      if (operators[j] == 4){
        calcButton.add(simplifiedExpression[k]);
        k++;
        calcButton.subtract(simplifiedExpression[k]);
      } else {
        calcButton.add(simplifiedExpression[k]);
      }
    }
  }

  public static void CalcTypeMessaging(boolean QuickCalc, PrintWriter pen, String[] line, String message){
    if (QuickCalc == true){
      pen.print(backtoString(line) + " -> ");
    }
    pen.print(message +"\n");
    pen.flush();
  }


  public static void runProgram(boolean QuickCalc, PrintWriter pen, PrintWriter error, BFCalculator calcButton, BFRegisterSet cabinet, Scanner input, boolean[] result){
    
    String[] stored = readIt2(input);

    if (BFCalcInput.orderMatters(stored, error, QuickCalc) == false){
      result[0] = false;
      return;
    }

    if (BFCalcInput.checkQUIT(stored[0]) == true){
      result[0] = false;
      return;
    }

    if (BFCalcInput.checkSTORE(stored[0]) == true){
      cabinet.store(stored[1].charAt(0), calcButton.get());
      CalcTypeMessaging(QuickCalc, pen, stored, "STORED");
    }

    if (BFCalcInput.checkFrac(stored[0],error, stored, QuickCalc) > 0){
      
      int[] operators = operatorAssign(stored);

      BigFraction[] simplifiedExpression = changeToBigFractions(stored, error, cabinet);

      if(Objects.equals(simplifiedExpression, null) == true){
        result[0] = false;
        return;
      }

      // checks and does only multiplication and division
      multiplyFracsFirst(simplifiedExpression, operators, calcButton);

      // Calculates sum.
      addAllFracLeft(simplifiedExpression, operators, calcButton);

      CalcTypeMessaging(QuickCalc, pen, stored, calcButton.get().toString());
    }

    pen.flush();
    error.flush();
    result[0] = true;
    return;
  }



  public static void QuickCalculatorStart(PrintWriter pen, PrintWriter error, Scanner look, BFCalculator calcButton, BFRegisterSet cabinet, String[] args){
    boolean[] result = {true};

    for (int i = 0; i < args.length; i++){
      look = new Scanner(args[i]);

      if (result[0]){
        runProgram(true, pen, error, calcButton, cabinet, look, result);
      } else {
        break;
      }
    }
    closeObjects(pen, error, look);
  }

  public static void InteractiveCalculatorStart(PrintWriter pen, PrintWriter error, Scanner look, Scanner open, BFCalculator calcButton, BFRegisterSet cabinet){
    boolean[] result = {true};
    while(true){
      open = new Scanner(System.in);
      pen.print("> ");
      look = new Scanner(open.nextLine());
      if (result[0]){
        runProgram(false, pen, error, calcButton, cabinet, look, result);
      } else {
        break;
      }
    }
    closeObjects(pen, error, look);
    open.close();
  }





}
