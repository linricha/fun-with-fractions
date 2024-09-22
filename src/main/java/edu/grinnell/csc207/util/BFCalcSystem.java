package edu.grinnell.csc207.util;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Objects;
import java.lang.Integer;


/**
 * Implements the Internal Workings of the BFCalculator like message prompts, reading inputs, etc. 
 * 
 * @author Richard Lin
 */
public class BFCalcSystem {

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Returns a tokenized array based on the spaces in the line read by input.
   * 
   * @param input a Scanner containing the input of a calculator.
   * @return a tokenized array of Strings.
   */
  public static String[] readIt2(Scanner input){
    return input.tokens().toArray(String[] :: new);
  } // readIt2(Scanner)

  /**
   * Returns a String made by concatenating spaces with the elements in arr.
   * 
   * @param arr a tokenized array of Strings.
   * @return a String.
   */
  public static String backtoString(String[] arr){
    String str = "";

    // loops through the String[]
    for(int i = 0; i < arr.length; i++){
      if ((i == 0) || (i == arr.length - 1)){
        str = str.concat(arr[i]);
      }  // if
      else {
        str = str.concat(" "+ arr[i]);
      } // if/else
    } // for

    return str;
  } // backToString(String[])

  /**
   * Returns the corresponding BigFraction based on str and record. Prints an error if a lowercase letter in
   * str is not initialized in record and returns null.
   * 
   * @param str A string representing a fraction, i.e. a string that passes as true/not 0 when put into the method checkFrac in BFCalcInput.
   * @param error A PrintWriter that prints errors.
   * @param record A register containing the values that some lowercase letter keys represent. 
   * @return A BigFraction based on the String str.
   */
  public static BigFraction giveBigFrac(String str, PrintWriter error, BFRegisterSet record){
    boolean wholeNum = BFCalcInput.wholeNumCheck(str);

    // checks if str is just a whole number.
    if (wholeNum == true){

      // Checks if an lowercase letter register is used in the numerator and is the only thing in the numerator.
      if (BFCalcInput.checkLowLetters(str.charAt(0)) == true){

        // Checks if that register has a valid value stored in record / if it is initiated.
        if (record.checkRecord(str.charAt(0)) == true){ 
          return record.get(str.charAt(0)); // need to make check that record is filled for that area.
        } // if
        // prints error if not.
        else{
          error.println("*** ERROR [Invalid Expression: Register value not initiated] ***");
          error.flush();
          return null;
        } // if/else
      } // if
      // Return a whole number.
      else{
        return new BigFraction(Integer.parseInt(str), 1);
      } // if/else
    } // if
    

    return new BigFraction(str);
  } // giveBigFrac(String, PrintWriter, BFRegister)

  /**
   * Returns an array of operators represented by ints, where 1 is *, 2 is /, 3 is +, and 4 is -,
   * in the order that they appear in the String[] line.
   * 
   * @param line A tokenized array of Strings that passes as true when put into the method orderMatters stored in BFCalcInput.
   * @return An array of operators found in line
   */
  public static int[] operatorAssign(String[] line){
    int doFirstCount = 0;

    int[] orderOfOperations = new int[((line.length - 1)/2) + 1];

    // loops through the String[]
    for (int j = 0; j < orderOfOperations.length - 1; j++){
      // Sets the operators to their respective int
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
      } // switch
    } // for
    orderOfOperations[orderOfOperations.length - 1] = doFirstCount;
  
    return orderOfOperations;
  } // operatorAssign(String[])
  
  
  /**
   * Returns an array BigFractions corresponding to line and record. Returns null if one of the BigFractions is null as decided
   * by giveBigFrac(String[], PrintWriter, BFRegisterSet)
   * 
   * @param line A tokenized array of Strings that passes as true when put into the method orderMatters stored in BFCalcInput.
   * @param error A PrintWriter that prints errors.
   * @param record A register containing the values that some lowercase letter keys represent. 
   * @return An array of BigFractions
   */
  public static BigFraction[] changeToBigFractions(String[] line, PrintWriter error, BFRegisterSet record){
    int lineLen;
    // Sets the length of the array of BigFractions to be returned.
    if (line.length > 1){
      lineLen = ((line.length - 1) / 2) + 1;
    } // if
    else {
      lineLen = 1;
    } // if/else
    BigFraction[] bigFracsOnly = new BigFraction[lineLen];

    // loops through the array
    for(int i = 0; i < bigFracsOnly.length; i++){
      // Checks that the array is not null
      if (Objects.equals(giveBigFrac(line[2*i],error, record), null) == false){
        bigFracsOnly[i] = giveBigFrac(line[2*i], error, record);
      } // if
      else {
        return null;
      } // if/else
    } // for

    return bigFracsOnly;
  } // changeToBigFractions(String[], PrintWriter, BFRegisterSet)

  /**
   * Closes the objects in the parameters.
   * 
   * @param pen1 A PrintWriter to be closed.
   * @param pen2 A PrintWriter to be closed.
   * @param sc1 A Scanner to be closed.
   */
  public static void closeObjects(PrintWriter pen1, PrintWriter pen2, Scanner sc1){
    pen1.close();
    pen2.close();
    sc1.close();
  } // CloseObjects(PrintWriter, PrintWriter, Scanner)

  /**
   * Simplifies simplifiedExpression by doing multiplication and division on the BigFractions first, adhereing to the order of operations 
   * when it involves multiplication, divison, addition, and subtraction. 
   * 
   * @param simplifiedExpression An array of BigFraction used to derive a single BigFraction by simplifying the BigFractions using the operators in int[] operators
   * @param operators An array that represents of ints that represent 4 different kind of operators, derived from the operatorAssign method.
   * @param calcButton A BFCalculator used to do math
   */
  public static void multiplyFracsFirst(BigFraction[] simplifiedExpression, int[] operators, BFCalculator calcButton){
    for (int j = 0, k = 0; 0 < operators[operators.length - 1];){
      // Checks if the int value of operators[j] corresponding to multiplying or dividing
      if (operators[j] <= 2){
        
        calcButton.add(simplifiedExpression[k]);
        simplifiedExpression[k] = new BigFraction(0, 1);
        k++;

        // Multiplies or Divides the previous BigFraction based on the int value of operator[j]
        if (operators[j] == 1){
          calcButton.multiply(simplifiedExpression[k]);
        } // if 
        else {
          calcButton.divide(simplifiedExpression[k]);
        } // if/else
        operators[j] = 3; // add
        operators[operators.length - 1]--;
        simplifiedExpression[k] = calcButton.get();
      
        j++;
      }
      // moves to the next BigFraction and operator if the current operator is not multiplication or division
      else {
        j++;
        k++;
      } // if/else
    } // for
    calcButton.clear();
  } // multiplyFracsFirst(BigFraction[], int[], BFCalculator)

  /**
   * Adds and subtract all the BigFractions to get the final value of the all the BigFractions together based on operators.
   * Final value will be stored as lastValue in calcButton.
   * 
   * @param simplifiedExpression An array of BigFraction used to derive a single BigFraction by simplifying the BigFractions using the operators in int[] operators. 
   *  Multiplication and Division from multiplyFracsFirst should have been already done to this.
   * @param operators An array that represents of ints that represent 4 different kind of operators, derived from the operatorAssign method.
   * @param calcButton A BFCalculator used to do math.
   */
  public static void addAllFracLeft(BigFraction[] simplifiedExpression, int[] operators, BFCalculator calcButton){
    // loops through simplifiedExpression adding and subtracting all the BigFractions
    for (int j = 0, k = 0; k < simplifiedExpression.length; k++, j++){
      // Checks if operator is the int corresponding to subtraction
      if (operators[j] == 4){
        calcButton.add(simplifiedExpression[k]);
        k++;
        calcButton.subtract(simplifiedExpression[k]);
      } // if
      // do addition
      else {
        calcButton.add(simplifiedExpression[k]);
      } // if/else
    } // for
  } // addAllFracLeft(BigFraction[], int[], BFCalculator)

  /**
   * Prints the corresponding error message based on QuickCalc.
   * 
   * @param QuickCalc A boolean saying if QuickCalculation will take place or not.
   * @param pen prints Strings.
   * @param line A tokenized array of Strings. 
   * @param message A message to be printed.
   */
  public static void CalcTypeMessaging(boolean QuickCalc, PrintWriter pen, String[] line, String message){
    // if QuickCalculation is taking place
    if (QuickCalc == true){
      pen.print(backtoString(line) + " -> ");
    } // if
    pen.print(message +"\n");
    pen.flush();
  } // CalcTypeMessaging(boolean, PrintWriter, String[], String)


  /**
   * Reads from the input and prints errors if and error has occured as designed by orderMatters of BFCalcInput. Otherwise,
   * Prints a message based on Quickcalc or prints the result of an expression as read by input.
   * 
   * 
   * @param QuickCalc A boolean saying if QuickCalculation will take place or not.
   * @param pen A PrintWriter to print Strings to output.
   * @param error A PrintWriter to print errors.
   * @param calcButton A BFCalculator used for calculation.
   * @param cabinet A BFRegisterSet that stores the corresponding lowercase letter keys and their values.
   * @param input A Scanner that reads the input.
   * @param result A boolean array of size one that stores the result of whether or not the method returned at the end.
   */
  public static void runProgram(boolean QuickCalc, PrintWriter pen, PrintWriter error, BFCalculator calcButton, BFRegisterSet cabinet, Scanner input, boolean[] result){
    
    String[] stored = readIt2(input);

    // Checks if the inputs are of correct syntax.
    if (BFCalcInput.orderMatters(stored, error, QuickCalc) == false){
      result[0] = false;
      return;
    } // if

    // Checks if the QUIT command has been typed as an input.
    if (BFCalcInput.checkQUIT(stored[0]) == true){
      result[0] = false;
      return;
    } // if

    // Checks if the STORE command has been typed as an input.
    if (BFCalcInput.checkSTORE(stored[0]) == true){
      cabinet.store(stored[1].charAt(0), calcButton.get());
      CalcTypeMessaging(QuickCalc, pen, stored, "STORED");
    } // if

    // Checks if the stored[0] is in the syntax of a fraction.
    if (BFCalcInput.checkFrac(stored[0], error, stored, QuickCalc) > 0){
      
      int[] operators = operatorAssign(stored);

      BigFraction[] simplifiedExpression = changeToBigFractions(stored, error, cabinet);

      // Checks if simplifiedExpression is a valid/not-null array
      if(Objects.equals(simplifiedExpression, null) == true){
        result[0] = false;
        return;
      } // if

      // checks and does only multiplication and division
      multiplyFracsFirst(simplifiedExpression, operators, calcButton);

      // Calculates sum.
      addAllFracLeft(simplifiedExpression, operators, calcButton);

      CalcTypeMessaging(QuickCalc, pen, stored, calcButton.get().toString());
    } // if

    pen.flush();
    error.flush();
    result[0] = true;
    return;
  } // runProgram(boolean, PrintWriter, PrintWriter, BFCalculator, BFRegister, Scanner, boolean[])


  /**
   * Runs/Starts the program for QuickCalculator. 
   * 
   * @param pen A PrintWriter that prints Strings to the output.
   * @param error A PrintWriter that prints errors.
   * @param look A Scanner that reads from args.
   * @param calcButton A BFCalculator that calculates.
   * @param cabinet A BFRegisterSet that stores the corresponding lowercase letter keys and their values.
   * @param args Command-Line Arguments.
   */
  public static void QuickCalculatorStart(PrintWriter pen, PrintWriter error, Scanner look, BFCalculator calcButton, BFRegisterSet cabinet, String[] args){
    boolean[] result = {true};

    // Loops through args
    for (int i = 0; i < args.length; i++){
      look = new Scanner(args[i]);

      // checks if runProgram has run into an error and stops if it has: result[0] == false
      if (result[0]){
        runProgram(true, pen, error, calcButton, cabinet, look, result);
      } // if 
      else {
        break;
      } // if/else
    } // for
    closeObjects(pen, error, look);
  } // QuickCalculatorStart(PrintWriter, PrintWriter, Scanner, BFCalculator, BFRegisterSet, String[])

  /**
   * Runs/Starts the program for InterativeCalculator.
   * 
   * @param pen A PrintWriter that prints Strings to the output.
   * @param error A PrintWriter that prints errors.
   * @param look A Scanner that reads a line from open.
   * @param open A Scanner that reads inputs. 
   * @param calcButton A BFCalculator that calculates.
   * @param cabinet A BFRegisterSet that stores the corresponding lowercase letter keys and their values.
   * @param args Command-Line Arguments.
   */
  public static void InteractiveCalculatorStart(PrintWriter pen, PrintWriter error, Scanner look, Scanner open, BFCalculator calcButton, BFRegisterSet cabinet){
    boolean[] result = {true};
    // A loop that continues until an error has occured
    while(true){
      open = new Scanner(System.in);
      pen.print("> ");
      look = new Scanner(open.nextLine());
      // hecks if runProgram has run into an error and stops if it has: result[0] == false
      if (result[0]){
        runProgram(false, pen, error, calcButton, cabinet, look, result);
      } // if 
      else {
        break;
      } // if/else
    } // while
    closeObjects(pen, error, look);
    open.close();
  } // InteractiveCalculatorStart(PrintWriter, PrintWriter, Scanner, Scanner, BFCalculator, BFRegister)





}
