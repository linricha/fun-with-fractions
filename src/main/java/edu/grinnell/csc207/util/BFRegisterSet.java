package edu.grinnell.csc207.util;

/**
 * A register containing the stored BigFraction values.
 * 
 * @author Richard Lin
 */
public class BFRegisterSet {
  
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  // where the stored values of the register go.
  BigFraction[] record;


  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public BFRegisterSet(){
    this.record = new BigFraction[26];
    for (int i = 0; i < record.length; i++){
      record[i] = null;
    } // for
  } // BFRegisterSet()

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+


  /**
   * Translates a lowercase letter value into the corresponding index.
   * 
   * @param register a specific location inside record.
   * @return translates the letter value to an index value corresponding to how values are stored in record.
   */
  int translate(char register){
    int index = (int)(register - 'a');
    return index;
  } // translate(char)

  /**
   * Stores val into record at the location that register corresponds to.
   * 
   * @param register a specific location inside record.
   * @param val the value to be stored at the location register in record.
   */
  public void store(char register, BigFraction val){
    this.record[translate(register)] = val;
  } // stor(char, BigFraction)

  /**
   * Returns the value located at the location that register corresponds to in record.
   * 
   * @param register a specific location inside record.
   * @return the value stored at register in record.
   */
  public BigFraction get(char register){
    return this.record[translate(register)];
  } // get(char)
  

  /**
   * Returns a boolean value stating whether or not there is a value 
   * in location that register corresponds to in record.
   * 
   * @param register a specific location inside record.
   * @return a boolean stating whether or not the register location is initiated.
   */
  public boolean checkRecord(char register){
    if(this.get(register) == null){
      return false;
    } // if
    return true;
  } // checkRecord(char)

} // class BFRegisterSet
