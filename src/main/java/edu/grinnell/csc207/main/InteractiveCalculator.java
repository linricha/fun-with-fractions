package edu.grinnell.csc207.main;

import java.io.PrintWriter;
import java.util.Scanner;
import edu.grinnell.csc207.util.BFCalculator;
import edu.grinnell.csc207.util.BFRegisterSet;
import edu.grinnell.csc207.util.BFCalcSystem;

/**
 * Runs InteractiveCalculator.
 *
 * @author Richard Lin
 */
public class InteractiveCalculator {

  /**
   * Sets up and runs interactiveCalculator.
   *
   * @param args Command-line arguments.
   * @throws Exception An exception from main.
   */
  public static void main(String[] args) throws Exception {

    PrintWriter pen = new PrintWriter(System.out, true);
    PrintWriter error = new PrintWriter(System.err, true);

    BFRegisterSet cabinet = new BFRegisterSet();
    BFCalculator calcButton = new BFCalculator();

    Scanner open = new Scanner(System.in);
    Scanner look = new Scanner(open.nextLine());

    calcButton.clear();

    BFCalcSystem.interactiveCalculatorStart(pen, error, look, open, calcButton, cabinet);

  } // main(String[] args)
} // class InteractiveCalculator
