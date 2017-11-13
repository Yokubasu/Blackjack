import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards = new Stack();
//    public Deck(){
//        for (Card.Suits s: Card.Suits.values()) {
//            for (Card.Ranks r: Card.Ranks.values()) {
//                cards.add(new Card(s, r));
//            }
//        }
//        Collections.shuffle(cards);
//    }

    public void populateDeck() {
        cards.clear();
        for (int i = 0; i < 6; i++) {
            for (Card.Suits s : Card.Suits.values()) {
                for (Card.Ranks r : Card.Ranks.values()) {
                    this.cards.push(new Card(s, r));
                }
            }
        }
        Collections.shuffle(this.cards);
    }

    public Card takeCard() {
        if (!this.cards.empty()) {
            return this.cards.pop();
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        if (this.cards.empty()) {
            return true;
        } else {
            return false;
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public int size() {
        return this.cards.size();
    }
}
