public class Card {
    private boolean faceDown = false;

    public enum Suits {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }

    public enum Ranks {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10),
        ACE(11);

        private final int points;

        Ranks(int points) {
            this.points = points;
        }

        public int points() {
            return this.points;
        }
    }

    private Suits suit;
    private Ranks rank;
    private boolean isSoftAce = false;

    public Card(Suits suit, Ranks rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public int points(){
        return this.rank.points();
    }

    public boolean isSoftAce(){
        return isSoftAce;
    }

    public void soften(){
        if(this.rank==Ranks.ACE){
            isSoftAce=true;
        }
    }

    public String toString() {
        String card;
        char suit = ' ';
        if (isFaceDown()) {
            card = "This card is face down";
        } else {
            switch (this.suit) {
                case CLUBS:
                    suit = '\u2663';
                    break;
                case HEARTS:
                    suit = '\u2764';
                    break;
                case SPADES:
                    suit = '\u2660';
                    break;
                case DIAMONDS:
                    suit = '\u2666';
                    break;
            }
            card = suit + this.rank.toString() + suit;
        }
        return card;
    }

    public boolean isFaceDown() {
        return faceDown;
    }

    public void setFaceDown() {
        this.faceDown = true;
    }

    public void setFaceUp() {
        this.faceDown = false;
    }

    public Ranks getRank() {
        return this.rank;
    }

}
