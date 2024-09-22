package edu.grinnell.csc207.main;

import java.io.PrintWriter;
import java.util.Scanner;
import edu.grinnell.csc207.util.BFCalculator;
import edu.grinnell.csc207.util.BFRegisterSet;
import edu.grinnell.csc207.util.BFCalcSystem;

/**
 * Runs InteractiveCalculator
 * 
 * @author Richard Lin
 */
public class InteractiveCalculator {

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

  } // main(String[] args)
} // class InteractiveCalculator
