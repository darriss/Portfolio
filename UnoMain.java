package project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/** This class includes the process of the uno game and calls the method from
 * uno and unoCards classes.
 *
 * @author Enpu & Sammy
 */
public class UnoMain {


  private static CardItem prevCard = null;
  private static int index = 1;
  private static CardItem wildCard = new CardItem("black", null);
  private static CardItem reverCard = new CardItem(null, "reverse");
  private static CardItem skipCard = new CardItem(null, "skip");
  private static CardItem drawtwoCard = new CardItem(null, "draw two");
  private static String winner = null;
  private static int playerScore = 0;
  private static int compScore = 0;
  private static boolean newGame = false;


  /** main.
   *
   */
  public static void main(String [] args) throws IOException {
    // Display messages and rules
    System.out.println("Welcome to UNO!");
    System.out.println("You are going to Play a uno game with the computer!");
    System.out.println("In this game, your goal is to get rid of your cards "
        + "before computer does. When it is your turn, try to match the previous "
        + "card, either by number, color, or symbol.");
    System.out.println("If you do not have a matching card, you must draw a "
        + "card from the draw pile by typing in \"Draw again\". If the card that you picked up "
        + "can be played, you are free to do so in the same round.\nOtherwise, it will "
        + "be the computer\'s turn. When you only "
        + "have one card left, you must type in \"UNO\". Failure to do this results in "
        + "automatically picking up 2 cards from the draw pile.");
    System.out.println("Are you ready? Type \"Yes\" or \"No\" ");



    // Declare the scanner and uno variables
    Scanner scanner = new Scanner(System.in);
    Scanner fileScanner = new Scanner(new File("input/cards.txt"));

    UnoPlayer playerHand = new UnoPlayer();
    UnoPlayer compHand = new UnoPlayer();

    ArrayList<CardItem> newDeck = new ArrayList<CardItem>();

    // read in the text file to the ArrayList
    while (fileScanner.hasNext()) {
      String unoCardLine = fileScanner.nextLine();
      Scanner cardScanner = new Scanner(unoCardLine);
      cardScanner.useDelimiter(",");

      String unoCardColor;
      String unoCardFace;


      unoCardColor = cardScanner.next();
      unoCardFace = cardScanner.next();
      CardItem unoCardItem = new CardItem(unoCardColor, unoCardFace);
      newDeck.add(unoCardItem);
    }



    // initiating the game
    while (scanner.hasNext() && newGame != true) {

      String command = scanner.nextLine();
      if (command.equalsIgnoreCase("Yes")) {
        // shuffle the card deck
        Collections.shuffle(newDeck);
        System.out.println("Shuffling the Uno Cards...");
      } else if (command.equalsIgnoreCase("No")) {
        System.out.println("Ok...Bye");
        System.exit(0);
      }


      /**
       * Method to determine turns.
       */
      boolean isPlayerTurn = true;
      boolean isCompTurn = false;
      boolean isWin = false;



      int initialDraw = 7;
      playerHand.drawCard(initialDraw, newDeck);
      compHand.drawCard(initialDraw, newDeck);

      /** Game Loop
       */
      while (newDeck.isEmpty() != true) {
        /** player round
         */
        while (isPlayerTurn = true && playerHand.isEmpty() != true && isWin != true) {
          System.out.println("These are your cards \n" + playerHand.toString());
          System.out.println("If you have a card to play, please type in \"Play\". "
              + "If you don't have a card matching with the previous card, "
              + "please type in \"Draw again\".");

          command = scanner.nextLine();

          // TO-DO: declare a CardItem that represents the card
          if (command.equalsIgnoreCase("Draw again")) {
            playerHand.drawCard(1, newDeck);
            isPlayerTurn = true;
            isCompTurn = false;
          } else if (command.equalsIgnoreCase("Play")) {
            System.out.println("Please type in the card you want to play.");
            command = scanner.nextLine();

            CardItem thisCard = turnCard(command);
            if (index == 1) {
              prevCard = thisCard;
              index++;
            }


            // Check if the Card could be played
            if (playerHand.containCard(thisCard) && canPlay(thisCard, prevCard)) {
              playerHand.play(thisCard);

              if (thisCard.getFace().equals("wild draw four")) {
                compHand.drawCard(4, newDeck);
                isPlayerTurn = true;
                isCompTurn = false;
              } else if (thisCard.getFace().equals("draw two")) {
                compHand.drawCard(2, newDeck);
                isPlayerTurn = true;
                isCompTurn = false;
                break;
              } else if (thisCard.getFace().equals("skip")
                  || thisCard.getFace().equals("reverse")
                  || thisCard.getFace().equals("wild")) {
                isPlayerTurn = true;
                isCompTurn = false;
                break;
              } else {
                prevCard = thisCard;
                isCompTurn = true;
                isPlayerTurn = false;
              }

              System.out.println("You played this card " + command);
              if (playerHand.size() == 1) {
                command = scanner.nextLine();
                if (command.equals("UNO")) {
                  isWin = true;
                  System.out.println("Congratulations, you win!");
                  winner = "Player";
                } else {
                  System.out.println("You didn\'t type in \"UNO\", so there "
                      + "will be two more cards in your hand");
                  playerHand.drawCard(2, newDeck);
                }
              } else {
                System.out.println("You finished your round.");
                System.out.println(" ");
              }
              break;
            } else {
              System.out.println("You can\'t play this card.");
              System.out.println(" ");
            }
          }
        }



        // computer's turn

        while (isCompTurn = true && !compHand.isEmpty() && isWin != true) {
          System.out.println("It is computer's round now.");
          Iterator<CardItem> cardMatched = compHand.checkMatchedCard(prevCard);
          if (cardMatched.hasNext()) {
            CardItem matchingCard = cardMatched.next();
            compHand.play(matchingCard);
            System.out.println("Computer played this card " + matchingCard.toString());
            if (matchingCard.getFace().equals("skip")
                || matchingCard.equals("reverse")) {
              isPlayerTurn = false;
              isCompTurn = true;
              prevCard = null;
            } else if (matchingCard.getFace().equals("draw two")) {
              playerHand.drawCard(2, newDeck);
              isPlayerTurn = false;
              isCompTurn = true;
              prevCard = null;
            } else {
              prevCard = matchingCard;
              isPlayerTurn = true;
              isCompTurn = false;
            }
            break;
          } else if (compHand.containCard(skipCard)) {
            Iterator<CardItem> skipCardMatched = compHand.checkMatchedCard(skipCard);
            CardItem matchingSkipCard = skipCardMatched.next();
            compHand.play(matchingSkipCard);
            System.out.println("Computer played this card " + matchingSkipCard.toString());
            System.out.println(" ");
            isPlayerTurn = false;
            isCompTurn = true;
            prevCard = null;
          } else if (compHand.containCard(drawtwoCard)) {
            Iterator<CardItem> drawCardMatched = compHand.checkMatchedCard(drawtwoCard);
            CardItem matchingDrawCard = drawCardMatched.next();
            compHand.play(matchingDrawCard);
            System.out.println("Computer played this card " + matchingDrawCard.toString());
            System.out.println(" ");
            playerHand.drawCard(2, newDeck);
            isPlayerTurn = false;
            isCompTurn = true;
            prevCard = null;
          } else if (compHand.containCard(reverCard)) {
            Iterator<CardItem> reverCardMatched = compHand.checkMatchedCard(reverCard);
            CardItem matchingReverCard = reverCardMatched.next();
            compHand.play(matchingReverCard);
            System.out.println("Computer played this card " + matchingReverCard.toString());
            System.out.println(" ");
            isPlayerTurn = false;
            isCompTurn = true;
            prevCard = null;
          } else {
            compHand.drawCard(1, newDeck);
            System.out.println("Computer doesn't have a matched card so it draws another card");
          }

          if (compHand.size() == 1) {
            isWin = true;
            winner = "Computer";
            System.out.println("UNO! Computer won!");
          }
        }

        if (isWin) {
          if (winner.equals("Computer")) {
            System.out.println("Computer has " + compScore + playerHand.getScore() + "points");
            compScore += playerHand.getScore();
          } else if (winner.equals("Player")) {
            System.out.println("You have " + playerScore + compHand.getScore() + "points");
            playerScore += compHand.getScore();
          }

          System.out.println("The overall score is: Computer: "
              + compScore + " Player: " + playerScore);
          System.out.println("Type in \"Replay\" to replay, type in \"Quit\" to exit the game.");
          command = scanner.nextLine();
          if (command.equalsIgnoreCase("Replay")) {
            isWin = false;
            index = 1;
            newGame = true;
          } else {
            System.exit(0);
          }
        }



      }
    }
  }

  // turning string object into CardItem object
  private static CardItem turnCard(String cardCommand) {

    Scanner commandCardScanner = new Scanner(cardCommand);
    commandCardScanner.useDelimiter(",");

    String cardCommandColor = commandCardScanner.next();
    String cardCommandFace = commandCardScanner.next();

    CardItem commandCardItem = new CardItem(cardCommandColor, cardCommandFace);
    return commandCardItem;
  }

  /** can play boolean.
   * Check if the card can be played
   *
   */
  public static boolean canPlay(CardItem unoThisCard, CardItem unoPrevCard) {
    boolean canPlay = false;
    if (unoThisCard.getColor().equals("black")) {
      canPlay = true;
    } else if (unoThisCard.getColor().equals(unoPrevCard.getColor())) {
      canPlay = true;
    } else if (unoThisCard.getFace().equals(unoPrevCard.getFace())) {
      canPlay = true;
    } else {
      canPlay = false;
    }
    return canPlay;
  }






}
