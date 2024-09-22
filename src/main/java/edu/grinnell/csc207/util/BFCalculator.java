package edu.grinnell.csc207.util;

/**
 * A simple implementation of a calculator involving Big Fractions. 
 * 
 * @author Richard Lin
 */
public class BFCalculator {
  
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  // The last value computed by the BFCalculator Object
  BigFraction lastValue;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public BFCalculator(){
    this.lastValue = null; 
  } // BFCalculator()
 
  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+


  /**
   * Returns the last value stored or the BigFraction value equal to 0 if there is no last Value.
   * 
   * @return lastValue or 0 in BigFraction
   */
  public BigFraction get(){
    if (lastValue == null){
      clear();
    } // if
    return lastValue;
  } // get()

  /**
   * Adds val onto the lastValue
   * 
   * @param val a BigFraction that will be added onto lastValue
   */
  public void add(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.add(val));
  } // add(BigFraction)

  /**
   * Subtracts val from the lastValue
   * 
   * @param val a BigFraction that will be subtracted from lastValue
   */
  public void subtract(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.subtract(val));
  } // substract(BigFraction)

  /**
   * Multiplies val onto the lastValue
   * 
   * @param val a BigFraction that will be multiplied onto lastValue
   */
  public void multiply(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.multiply(val));
  } // multiply(BigFraction)

  /**
   * Divides lastValue by val
   * 
   * @param val a BigFraction that will be dividing lastValue
   */
  public void divide(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.divide(val));
  } // divide(BigFraction)

  /**
   * Sets the lastValue of the BFCalculator object to 0.
   */
  public void clear(){
    this.lastValue = new BigFraction(0,1);
  } // clear()

} // class BFCalculator
