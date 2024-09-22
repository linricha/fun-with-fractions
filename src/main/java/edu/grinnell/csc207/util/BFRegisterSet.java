package edu.grinnell.csc207.util;

public class BFRegisterSet {
  
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  BigFraction[] record;
  BigFraction value;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public BFRegisterSet(){
    this.record = new BigFraction[26];
    for (int i = 0; i < record.length; i++){
      record[i] = null;
    }
  }

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  int translate(char register){
    int index = (int)(register - 'a');
    return index;
  }

  public void store(char register, BigFraction val){
    this.record[translate(register)] = val;
  }

  // MAKE ERRO HERE for if register not initiated?
  public BigFraction get(char register){
    return this.record[translate(register)];
  }
  

  public boolean checkRecord(char register){
    if(this.get(register) == null){
      return false;
    }
    return true;
  }

}
