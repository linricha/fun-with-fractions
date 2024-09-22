package edu.grinnell.csc207.main;

import java.util.Scanner;
import java.io.PrintWriter;
import edu.grinnell.csc207.util.BFCalculator;
import edu.grinnell.csc207.util.BFRegisterSet;
import edu.grinnell.csc207.util.BFCalcSystem;


public class QuickCalculator {
  
  
  public static void main(String[] arg) throws Exception{
    String[] args = {"3/9"};

    PrintWriter pen = new PrintWriter(System.out, true);
    PrintWriter error = new PrintWriter(System.err, true);

    BFRegisterSet cabinet = new BFRegisterSet();
    BFCalculator calcButton = new BFCalculator();

    Scanner look =  new Scanner(args[0]);

    calcButton.get();

    BFCalcSystem.QuickCalculatorStart(pen, error, look, calcButton, cabinet, args);

  } 
}
