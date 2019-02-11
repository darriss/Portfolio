package project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/** This class represents the cards in the input file "cards.txt"
 * It provides all of the variables and methods needed to represent a single item
 * in a deck of UNO cards
 *
 * @author Enpu & Sammy
 */

/** This will be a public class called in the uno.java
 */
public class CardItem {

  private String color;
  private String face;


  /**
   * Construct an instance of the unoCards.
   */
  public CardItem(String cardColor, String cardFace) {
    color = cardColor;
    face = cardFace;
  }


  /** This is the getColor method.
   * It reads from the input
   */
  public String getColor() {
    return color;
  }

  /** This is the getFace method.
   * It reads from the input
   */
  public String getFace() {
    return face;
  }

  /** This is the equals method.
   * It checks the variable types
   */
  public boolean equals(Object obj) {
    return this.getClass() == obj.getClass()
      && ((CardItem)obj).color.equalsIgnoreCase(this.color)
      && ((CardItem)obj).face.equalsIgnoreCase(this.face);
  }

  /** This is the toString method.
   * It returns in the variable of String
   */
  public String toString() {
    return new String(color + "," + face);
  }









}
