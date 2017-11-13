import java.util.Scanner;

public class Main {

    private static int round = 1;

    public static void main(String[] args) {
        Player player1 = new Player();
        Dealer dealer = new Dealer();
        Deck deck = new Deck();
        deck.populateDeck();
        boolean run = true;
        boolean playerTurn = true;
        boolean dealerTurn = true;
        int amount;
        boolean incorrectBet;
        String move;

        try (Scanner keyboard = new Scanner(System.in)) {
            while (run) {
                System.out.println("ROUND " + round);
                System.out.println("\nPLACING BETS");
                do {
                    System.out.println("Your balance: " + player1.getBalance() + "$");
                    System.out.println("Enter the amount between 2$ and 500$: ");
                    amount = keyboard.nextInt();
                    if (amount < 2) {
                        System.out.println("\nYou have to make a larger bet.");
                        incorrectBet = true;
                    } else if (amount > 500) {
                        System.out.println("\nYou have to make a smaller bet.");
                        incorrectBet = true;
                    } else if (amount > player1.getBalance()) {
                        System.out.println("\nYour balance is insufficient.");
                        incorrectBet = true;
                    } else {
                        player1.bet(amount);
                        incorrectBet = false;
                    }
                } while (incorrectBet);

                if (deck.isEmpty()) {
                    System.out.println("Deck reshuffled.");
                    deck.populateDeck();
                }

                System.out.println("\nDEALLING CARDS");
                // 1st card
                System.out.println("Dealing first card.");
                dealer.dealCard(player1.hand, deck);
                dealer.dealCard(dealer.hand, deck);

                // 2nd card
                System.out.println("Dealing second card.");
                dealer.dealCard(player1.hand, deck);
                dealer.dealCard(dealer.hand, deck, true);

                System.out.println("\t\tPlayer's hand:" + "\t\t\tDealer's hand:");
                System.out.println("\t\t" + player1.hand.getCards().get(0).toString() + "\t\t\t\t\t" + dealer.hand.getCards().get(0).toString());
                System.out.println("\t\t" + player1.hand.getCards().get(1).toString() + "\t\t\t\t\t" + dealer.hand.getCards().get(1).toString());
                System.out.println("Score: " + "\t" + player1.hand.getScore() + "\t\t\t\t\t\t" + dealer.hand.getScore() + "+?");

                if (dealer.firstCardIsAce()) {
                    System.out.println("\nDealer has an ACE.");

                    System.out.println("Do you wish to make an insurance? (y/n)");
                    move = keyboard.next();
                    switch (move) {
                        case "y":
                            System.out.println("You chose yes.");
                            do {
                                System.out.println("Your balance: " + player1.getBalance() + "$");
                                System.out.println("Make a side bet of up to " + player1.getBettedAmount() / 2 + "$: ");
                                amount = keyboard.nextInt();
                                if (amount < 1) {
                                    System.out.println("\nYou have to make a larger bet.");
                                    incorrectBet = true;
                                } else if (amount > player1.getBettedAmount() / 2) {
                                    System.out.println("\nYou have to make a smaller bet.");
                                    incorrectBet = true;
                                } else if (amount > player1.getBalance()) {
                                    System.out.println("\nYour balance is insufficient.");
                                    incorrectBet = true;
                                } else {
                                    player1.makeInsurance(amount);
                                    incorrectBet = false;
                                }
                            } while (incorrectBet);

                            break;
                        case "n":
                            System.out.println("You chose no.");

                            break;
                        default:
                            System.out.println("Incorrect choice.");

                    }

                }

                if (dealer.canLookAtFaceDownCard()) {
                    System.out.println("\nSetting dealer's second card face up:");
                    dealer.hand.getCards().get(1).setFaceUp();
                    if (dealer.hand.getScore() > 21) {
                        System.out.println("Dealer has a soft hand.");
                        System.out.println("Counting ACE as 1.");
                    }
                    dealer.hand.print();

                    if (dealer.hand.isNatural()) {
                        if (player1.hasInsurance()) {
                            System.out.println("Player has an insurance.");
                            System.out.println("Player gets: " + dealer.payInsurance(player1) + "$");
                        }
                        if (player1.hand.isNatural()) {
                            System.out.println("\nIt's a stand-off.");
                            System.out.println("Returning bet.");
                            //player1.removeBet();
                            playerTurn = false;
                            dealerTurn = false;
                            //continue;
                        } else {
                            System.out.println("\nDealer got a Blackjack.");
                            System.out.println("Collecting bet.");
                            dealer.takeBet(player1);
                            playerTurn = false;
                            dealerTurn = false;
                            //continue
                        }
                    } else {
                        if (player1.hasInsurance()) {
                            System.out.println("Player lost the side bet.");
                            dealer.takeSideBet(player1);
                        }
                        if (player1.hand.isNatural()) {
                            System.out.println("\nPlayer got a Blackjack.");
                            System.out.println("Player gets: " + dealer.pay(player1, 1.5) + "$");
                            playerTurn = false;
                            dealerTurn = false;
                            //continue;
                        }
                    }
                } else {
                    if (player1.hand.isNatural()) {
                        System.out.println("\nPlayer got a Blackjack.");
                        System.out.println("Player gets: " + dealer.pay(player1, 1.5) + "$");
                        playerTurn = false;
                        dealerTurn = false;
                        //continue;
                    }
                }

                while (playerTurn) {
                    System.out.println("\nYOUR TURN");
                    if (player1.canDoubleDown() && player1.splitHand == null) {
                        System.out.println("\nYour hand score is " + player1.hand.getScore());

                        System.out.println("Do you wish to double down? (y/n)");
                        move = keyboard.next();
                        switch (move) {
                            case "y":
                                System.out.println("You chose yes.");
                                System.out.println("Doubling your bet.");
                                player1.doubleDown();
                                System.out.println("\nDealing one card face down.");
                                dealer.dealCard(player1.hand, deck, true); // deals one card
                                System.out.println("\nYour new hand:");
                                player1.hand.print();
                                playerTurn = false;

                                break;
                            case "n":
                                System.out.println("You chose no.");

                                break;
                            default:
                                System.out.println("Incorrect choice.");

                        }

                    }

                    if (player1.canSplitAPair() && !player1.hasDoubledDown()) {
                        System.out.println("\nYou have a pair of the same rank.");

                        System.out.println("Do you wish to split your hand? (y/n)");
                        move = keyboard.next();
                        switch (move) {
                            case "y":
                                System.out.println("You chose yes.");
                                System.out.println("Splitting your hand.");
                                player1.splitPair();
                                System.out.println("Putting an equal bet on your second hand.");
                                if (player1.hand.getCards().get(0).getRank() == Card.Ranks.ACE) {
                                    System.out.println("\nDealing one card to each hand.");
                                    dealer.dealCard(player1.hand, deck);
                                    if (player1.hand.isSoft() && player1.hand.isBust()) {
                                        System.out.println("\nYour 1st hand is soft.");
                                        System.out.println("Counting ACE as ONE");
                                    }
                                    if (player1.splitHand.isSoft() && player1.splitHand.isBust()) {
                                        System.out.println("\nYour 2nd hand is soft.");
                                        System.out.println("Counting ACE as ONE");
                                    }
                                    dealer.dealCard(player1.splitHand, deck);
                                    System.out.println("\nYour 1st hand: ");
                                    player1.hand.print();
                                    System.out.println("\nYour 2nd hand: ");
                                    player1.splitHand.print();
                                    playerTurn = false;
                                }
                                break;
                            case "n":
                                System.out.println("You chose no.");

                                break;
                            default:
                                System.out.println("Incorrect choice.");

                        }

                    }

                    if (!playerTurn) {
                        break;
                    }

                    if (player1.splitHand != null) {
                        System.out.println("\nPlay your first hand:");
                        player1.hand.print();
                    }
                    System.out.println("\nHit (enter h) or Stand (enter s)? ");
                    move = keyboard.next();
                    switch (move) {
                        case "h":
                            System.out.println("You chose hit.");
                            System.out.println("\nDealing one card.");
                            dealer.dealCard(player1.hand, deck); // deals one card
                            System.out.println("\nYour new hand:");
                            player1.hand.print();

                            if (player1.hand.isBust()) {
                                if (player1.hand.isSoft()) {
                                    System.out.println("\nYou have a soft hand.");
                                    System.out.println("Counting ACE as 1:");
                                    player1.hand.print();

                                } else {
                                    playerTurn = false;
                                    if (player1.splitHand == null) {
                                        dealerTurn = false;
                                    }
                                    System.out.println("\nYou got bust.");
                                    System.out.println("Collecting bet.");
                                    dealer.takeBet(player1);
                                    // break main cycle
                                }
                            }
                            break;
                        case "s":
                            System.out.println("You chose stand.");
                            playerTurn = false;
                            break;
                        default:
                            System.out.println("Incorrect choice.");
                            break;
                    }
                }

                if (player1.splitHand != null) {
                    playerTurn = true;
                    System.out.println("\nPlay your second hand:");
                    player1.splitHand.print();
                    while (playerTurn) {
                        System.out.println("\nHit (enter h) or Stand (enter s)? ");
                        move = keyboard.next();
                        switch (move) {
                            case "h":
                                System.out.println("You chose hit.");
                                System.out.println("\nDealing one card.");
                                dealer.dealCard(player1.splitHand, deck); // deals one card
                                System.out.println("\nYour new hand:");
                                player1.splitHand.print();

                                if (player1.splitHand.isBust()) {
                                    if (player1.splitHand.isSoft()) {
                                        System.out.println("\nYou have a soft hand.");
                                        System.out.println("Counting ACE as 1:");
                                        player1.splitHand.print();

                                    } else {
                                        playerTurn = false;
                                        dealerTurn = false;
                                        System.out.println("\nYou got bust.");
                                        System.out.println("Collecting bet.");
                                        dealer.takeBet(player1);
                                        // player1.removeBet();
                                        // break main cycle
                                    }
                                }
                                break;
                            case "s":
                                System.out.println("You chose stand.");
                                System.out.println("End of turn.");
                                playerTurn = false;
                                break;
                            default:
                                System.out.println("Incorrect choice.");
                                break;
                        }
                    }
                }

                while (dealerTurn) {
                    System.out.println("\nDEALER'S TURN");
                    System.out.println("Dealer's hand:");
                    dealer.hand.getCards().get(1).setFaceUp();
                    dealer.hand.print();
                    if (dealer.hand.getScore() < 17) {
                        System.out.println("\nDealing one card.");
                        //System.out.println("Dealer's new hand:");
                        dealer.dealCard(dealer.hand, deck);
                        //dealer.hand.print();

                        if (dealer.hand.isBust()) {
                            if (dealer.hand.isSoft()) {
                                System.out.println("Dealer has a soft hand.");
                                System.out.println("Counting ACE as 1.");
                            } else {
                                System.out.println("Dealer's hand:");
                                dealer.hand.print();
                                System.out.println("\nDealer got bust.");
                                System.out.println("Player gets: " + dealer.pay(player1, 1.0) + "$");
                                dealerTurn = false;
                                // break main cycle
                            }
                        }
                    } else { // stand
                        System.out.println("\nEnd of dealer's turn.");
                        dealerTurn = false;
                    }
                }

                if (!player1.hand.isBust() && !dealer.hand.isBust() && !player1.hand.isNatural() && !dealer.hand.isNatural()) {
                    System.out.println("\nSETTLEMENT.");
                    if (player1.hasDoubledDown()) {
                        player1.hand.getCards().get(2).setFaceUp();
                        player1.setDoubledDownToFalse();
                        System.out.println("\nYour hand:");
                        player1.hand.print();
                    }

                    if (dealer.hand.getScore() > player1.hand.getScore()) {
                        System.out.println("\nDealer's hand wins.");
                        System.out.println("Collecting bets.");
                        dealer.takeBet(player1);
                    } else {
                        if (dealer.hand.getScore() < player1.hand.getScore()) {
                            System.out.println("\nPlayer's hand wins.");
                            System.out.println("Player gets: " + dealer.pay(player1, 1.0) + "$");
                        } else {
                            System.out.println("\nIt's a stand off.");
                            //player1.removeBet();
                        }
                    }
                }

                if (player1.splitHand != null) {
                    if (!player1.splitHand.isBust()) {
                        System.out.println("\nSETTLEMENT FOR YOUR SECOND HAND.");

                        if (dealer.hand.getScore() > player1.splitHand.getScore()) {
                            System.out.println("\nDealer's hand wins.");
                            System.out.println("Collecting bets.");
                            dealer.takeBet(player1);
                            //player1.removeBet();
                        } else {
                            if (dealer.hand.getScore() < player1.splitHand.getScore()) {
                                System.out.println("\nPlayer's hand wins.");
                                System.out.println("Player gets: " + dealer.pay(player1, 1.0) + "$");
                            } else {
                                System.out.println("\nIt's a stand off.");
                            }
                        }
                    }
                }

                System.out.println("\nYour balance:");
                System.out.println(player1.getBalance() + "$");
                player1.removeBet();
                String playAgain;
                if (player1.getBalance() < 2) {
                    System.out.println("GAME OVER.");
                    System.out.println("Play again? (y/n)");
                } else {
                    System.out.println("\nPlay another round? (y/n)");
                }
                playAgain = keyboard.next();

                switch (playAgain) {
                    case "n":
                        run = false;
                        System.out.println("You chose no.");
                        System.out.println("\nExiting application.");
                        break;
                    case "y":
                        System.out.println("You chose yes.");
                        round++;
                        if (player1.getBalance() < 2) {
                            round = 1;
                            player1.setBalance(1000);
                            deck.populateDeck();
                        }
                        player1.hand.discard();
                        dealer.hand.discard();
                        player1.setDoubledDownToFalse();
                        playerTurn = true;
                        dealerTurn = true;
                        break;
                    default:
                        System.out.println("Incorrect choice.");
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
