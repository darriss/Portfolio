package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/** This class represent player and its method.
 *
 *
 * @author Enpu & Sammy
 */


public class UnoPlayer {

  ArrayList<CardItem> playerHand = new ArrayList<CardItem>();

  /**
   * Construct an instance of the player.
   */
  public UnoPlayer() {
  }

  public boolean isEmpty() {
    return playerHand.isEmpty();
  }


  public int size() {
    return playerHand.size();
  }

  public void removeCard(CardItem removeCard) {
    playerHand.remove(removeCard);
  }

  public boolean containCard(CardItem containCard) {
    return playerHand.contains(containCard);
  }

  /**
   * return an iterator of the first card matching with the previous card.
   */
  public Iterator<CardItem> checkMatchedCard(CardItem prevCard) {
    ArrayList<CardItem> matchedItem = new ArrayList<CardItem>();
    Iterator<CardItem> iterator = playerHand.iterator();
    while (iterator.hasNext() && matchedItem.size() == 0) {
      CardItem matchedItems = iterator.next();
      if (matchedItems.getColor().equals(prevCard.getColor())
          || matchedItems.getFace().equals(prevCard.getFace())) {
        matchedItem.add(matchedItems);
        break;
      }
    }
    return matchedItem.iterator();
  }



  /** Method to draw card to hand.
   *
   */
  public void drawCard(int numDraw, ArrayList<CardItem> fromDeck) {
    for (int i = 0; i < numDraw; i++) {
      playerHand.add(fromDeck.get(0));
      fromDeck.remove(0);
    }
  }

  /** Method to play card.
   *
   */
  public void play(CardItem playthisCard) {
    playerHand.remove(playthisCard);
  }


  /** Method to calculate the score.
   *
   */
  public int getScore() {
    int score = 0;
    Iterator<CardItem> iterator = playerHand.iterator();
    while (iterator.hasNext()) {
      CardItem playerHandCard = iterator.next();
      if (playerHandCard.getColor().equals("Wild")) {
        score = score + 50;
      } else if (playerHandCard.getFace().equals("Skip")
          || playerHandCard.getFace().equals("Reverse")
          || playerHandCard.getFace().equals("Draw two")) {
        score = score + 20;
      } else {
        int facePoint = Integer.valueOf(playerHandCard.getFace());
        score = score + facePoint;
      }
    }
    return score;
  }


  /** to string method.
   * to string method presents the cards in the player's hand
   */
  public String toString() {
    StringBuilder builder = new StringBuilder();
    Iterator<CardItem> iterator = playerHand.iterator();
    while (iterator.hasNext()) {
      builder.append(iterator.next().toString());
      if (iterator.hasNext()) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }




}
