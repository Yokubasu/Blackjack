import java.util.ArrayList;
import java.util.Objects;

public class Hand {
    private ArrayList<Card> cards = new ArrayList();
    private int score = 0;

    public void addCard(Card card) {
        this.cards.add(card);
        this.score += card.points();
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public void print() {
        if (cards.isEmpty()) {
            System.out.println("\nThe hand is empty");
        } else {
            for (Card c : cards) {
                System.out.println(c.toString());
            }
            if (score != getScore()) {
                System.out.println(getScore() + " + ?");
            } else {
                System.out.println(score);
            }
        }
    }

    public int getScore() {
        int score = this.score;
        for (Card c : cards) {
            if (c.isFaceDown()) {
                score -= c.points();
            }
        }
        return score;
    }

    public boolean isNatural() {
        if (cards.size() == 2 && this.score == 21) {
            return true;
        }
        return false;
    }

    public boolean isBust() {
        if (this.score > 21) {
            return true;
        }
        return false;
    }

    public void discard() {
        cards.clear();
        this.score = 0;
    }

    public boolean isSoft() {
        if (!this.isNatural()) {
            for (Card c : cards) {
                if (c.getRank() == Card.Ranks.ACE) {
                    if (!c.isSoftAce()) {
                        c.soften();
                        this.score -= 10;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAPair() {
        if (cards.size() == 2) {
            if (cards.get(0).getRank() == cards.get(1).getRank()) {
                return true;
            }
        }
        return false;
    }

    public Hand split() {
        Card card;
        Hand splitHand;
        if (this.isAPair()) {
            card = this.cards.get(1);
            this.cards.remove(1);
            this.score-=card.points();
            splitHand = new Hand();
            splitHand.addCard(card);
            return splitHand;
        }
        return null;
    }

}
