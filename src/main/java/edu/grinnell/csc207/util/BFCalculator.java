package edu.grinnell.csc207.util;


public class BFCalculator {
  
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  BigFraction lastValue;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public BFCalculator(){
    this.lastValue = null; 
  }

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+


  //SAYS RETURNS 0 IF NO VALUE (SHOULD SET LASTVALUE TO 0?)
  public BigFraction get(){
    if (lastValue == null){
      clear();
    }
    return lastValue;
  }

  public void add(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.add(val));
  }

  public void subtract(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.subtract(val));
  }

  public void multiply(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.multiply(val));
  }

  public void divide(BigFraction val){
    this.get();
    this.lastValue = (this.lastValue.divide(val));
  }

  public void clear(){
    this.lastValue = new BigFraction(0,1);
  }




  // Methods
}
