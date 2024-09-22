package edu.grinnell.csc207.main;

import java.io.PrintWriter;
import java.util.Scanner;
import edu.grinnell.csc207.util.BFCalculator;
import edu.grinnell.csc207.util.BFRegisterSet;
import edu.grinnell.csc207.util.BFCalcSystem;

public class InteractiveCalculator {
  


  //MESSAGE TO SELF:
  // maybe replace return with break and have while loop contian boolean where it determines to keep running or not
  // i.e. boolean random = false when return occurs and then break happens.
  // Then, add close objects call to end (gets rid of annoying yellow underline)


  // Same with quickcalc except add and statement to the for loop where it also considers if boolean random is still true;






  public static void main(String[] args) throws Exception{
    //String[] args = {"1/3 + 4" ,"B", "a", "a + 1"};

    PrintWriter pen = new PrintWriter(System.out, true);
    PrintWriter error = new PrintWriter(System.err, true);

    BFRegisterSet cabinet = new BFRegisterSet();
    BFCalculator calcButton = new BFCalculator();

    Scanner open = new Scanner(System.in);
    Scanner look = new Scanner(open.nextLine());

    calcButton.clear();

    BFCalcSystem.InteractiveCalculatorStart(pen, error, look, open, calcButton, cabinet);

  }
}
