package edu.grinnell.csc207.util;

import java.math.BigInteger;

/**
 * A simple implementation of arbitrary-precision Fractions.
 *
 * @author Samuel A. Rebelsky
 * @author Slok Rajbhandari, Richard Lin (Lab)
 * @authot Richard Lin (MP2)
 * 
 * MP2 Change Notes:
 *  Removed Constants, changed 'simplify' to 'reduction' in design decisions, changed multiply from non-static to static,
 *  removed warning notes from lab, altered toString() method so it can now print
 *  whole numbers and added reduction() call to this in toString(), altered multiply(BigFraction) method 
 *  by changing from static to non-static, added reduction method call to add and multiply in their returns.
 */
public class BigFraction {
  // +------------------+---------------------------------------------
  // | Design Decisions |
  // +------------------+

  /*
   * (1) Denominators are always positive. Therefore, negative fractions
   * are represented with a negative numerator. Similarly, if a fraction
   * has a negative numerator, it is negative.
   *
   * (2) Fractions are not necessarily stored in simplified form. To
   * obtain a fraction in simplified form, one must call the `reduction.` 
   * method.
   */

  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  /** The numerator of the fraction. Can be positive, zero or negative. */
  BigInteger num;

  /** The denominator of the fraction. Must be non-negative. */
  BigInteger denom;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new fraction with numerator num and denominator denom.
   *
   * @param numerator
   *   The numerator of the fraction.
   * @param denominator
   *   The denominator of the fraction.
   */
  public BigFraction(BigInteger numerator, BigInteger denominator) {
    this.num = numerator;
    this.denom = denominator;

    this.placeholderFracSet();
  } // BigFraction(BigInteger, BigInteger)

  /**
   * Build a new fraction with numerator num and denominator denom.
   *
   * @param numerator
   *   The numerator of the fraction.
   * @param denominator
   *   The denominator of the fraction.
   */
  public BigFraction(int numerator, int denominator) {
    this.num = BigInteger.valueOf(numerator);
    this.denom = BigInteger.valueOf(denominator);
    
    this.placeholderFracSet();

  } // BigFraction(int, int)

  /**
   * Build a new fraction by parsing a string.
   *
   * @param str
   *   The fraction in string form
   */
  public BigFraction(String str) {
    int location = str.indexOf('/');

    this.num = BigInteger.valueOf((long)Integer.decode(str.substring(0, location)));
    this.denom = BigInteger.valueOf((long)Integer.decode(str.substring(location + 1)));

    this.placeholderFracSet();
  } // BigFraction

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  public void placeholderFracSet(){
    BigFraction placeholder = this.reduction();
    this.num = placeholder.numerator();
    this.denom = placeholder.denominator();
  }

  /**
   * Add another faction to this fraction.
   *
   * @param addend
   *   The fraction to add.
   *
   * @return the result of the addition.
   */
  public BigFraction add(BigFraction addend) {
    BigInteger resultNumerator;
    BigInteger resultDenominator;

    // The denominator of the result is the product of this object's
    // denominator and addend's denominator
    resultDenominator = this.denom.multiply(addend.denom);
    // The numerator is more complicated
    resultNumerator =
      (this.num.multiply(addend.denom)).add(addend.num.multiply(this.denom));

    // Return the computed value
    return (new BigFraction(resultNumerator, resultDenominator)).reduction();
  } // add(BigFraction)
  /**
   * Express this fraction as a double.
   *
   * @return the fraction approxiamted as a double.
   */
  public double doubleValue() {
    return this.num.doubleValue() / this.denom.doubleValue();
  } // doubleValue()
  /**
   * Get the denominator of this fraction.
   *
   * @return the denominator
   */
  public BigInteger denominator() {
    return this.denom;
  } // denominator()

  /**
   * Get the numerator of this fraction.
   *
   * @return the numerator
   */
  public BigInteger numerator() {
    return this.num;
  } // numerator()

  /** 
   * Convert this fraction to a string for ease of printing.
   * 
   * !!! Altered by Richard Lin (MP2):
   *  Can now print whole numbers.
   *
   * @return a string that represents the fraction.
   */
  public String toString() {
    BigFraction placeholder = this.reduction();

    // Special case: It's zero
    if (placeholder.num.equals(BigInteger.ZERO)) {
      return "0";
    } // if it's zero

    // Check for whole numbers
    if (placeholder.denom.equals(new BigInteger("1"))){
      return placeholder.num.toString();
    }

    // Lump together the string represention of the numerator,
    // a slash, and the string representation of the denominator
    // return this.num.toString().concat("/").concat(this.denom.toString());
    return placeholder.num + "/" + placeholder.denom;
  } // toString()

    // Lump together the string represention of the numerator,/.reduction();

  /**
   * Returns the result of multiplying two BigFractions
   * 
   * !!! Altered by Richard Lin (MP2) to change method from static to non-static.
   * 
   * @param multiplier what to multiply this BigFraction by
   * @return the result of multiplication
   */
  public BigFraction multiply(BigFraction multiplier){
    BigInteger top = this.numerator();
    top = top.multiply(multiplier.numerator());

    BigInteger bottom = this.denominator();
    bottom = bottom.multiply(multiplier.denominator());

    return (new BigFraction(top, bottom)).reduction();
  }//multiply()


  public BigFraction fractional(){
    BigInteger num = this.numerator();
    BigInteger denom = this.denominator();

    BigInteger top = (num.mod(denom));
    return new BigFraction(top, denom);
  }



  // Additional methods add by Richard Lin for MP2.

  // maybe add restiction for 0 since division is not accurate or safe with 0.
  public BigFraction divide(BigFraction divisor){
    BigFraction flipped = new BigFraction(divisor.denominator(), divisor.numerator());

    return (this.multiply(flipped)).reduction();
  }

  // addition with a negative number
  public BigFraction subtract(BigFraction minus){
    BigInteger top = (minus.numerator()).multiply(BigInteger.valueOf(-1));

    return (this.add(new BigFraction(top, minus.denominator()))).reduction();
  }


  public BigFraction reduction(){
    BigInteger top = this.numerator();
    BigInteger bottom = this.denominator();

    int topInt = top.intValue();
    int bottomInt = bottom.intValue();

    boolean topNeg = false;
    boolean botNeg = false;


    // b/c % doesn't work as well with negatives / works differently
    if (topInt < 0){
      topNeg = true;
      topInt *= -1;
    }
    if (bottomInt < 0){
      botNeg = true;
      bottomInt *= -1;
    }

    // loops through divising until i is eventuall greater than numerator: thus no more simplifying can be done.
    for(int i = 2; i <= topInt; i++){
      
      // Keeps simplifying both if can
      // Stops when the divisor is removed completely.
      while ((topInt % i == 0) && (bottomInt % i == 0)){
        topInt = topInt/i;
        bottomInt = bottomInt/i;
      }
    }

    if (topNeg){
      topInt *= -1;
    }
    if (botNeg){
      bottomInt *= -1;
    }


    // ADD STUFF TO MAKE IT SO NEGATIVES CANCEL AND 
    // IF DENOMINATOR IS NEGATIVE, MAKE IT POSITIVE AND MAKE NUMERATOR NEGATIVE INSTEAD.
    if (bottomInt < 0){
      topInt *= -1;
      bottomInt *= -1;
    }


    BigInteger newTop = BigInteger.valueOf((long)topInt);
    BigInteger newBottom = BigInteger.valueOf((long)bottomInt);

    return new BigFraction(newTop, newBottom);
  }



  //READ AND CHANGE COMMENTS AT TOP SINCE SIMPLIFY FUNCTION IS CALLED REDUCTION AND THERE ARE OTHER THINGS THAT 
  // DONT MAKE SENSE
} // class BigFraction
