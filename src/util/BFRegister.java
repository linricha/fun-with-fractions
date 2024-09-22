package edu.grinnell.csc207;

public class BFRegister {
  
  // Fields

  BigFraction[] register;
  BigFraction value;

  // Fields
  // Constructors

  public BFRegister(){
    this.record = new BigFraction[26];
  }

  // Constructors
  // Methods

  int translate(char register){
    int index = (int)(register - 'a');
    return index;
  }

  public void store(char register, BigFraction val){
    this.record[translate(register)] = val;
  }

  public BigFraction get(char register){
    return this.record[translate(register)];
  }

  // Methods

}
