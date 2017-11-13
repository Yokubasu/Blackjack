public class Dealer {
    public Hand hand = new Hand();

    public void dealCard(Hand hand, Deck fromDeck, boolean faceDown) {
        if(fromDeck.isEmpty()){
            fromDeck.populateDeck();
        }
        Card card = fromDeck.takeCard();
        card.setFaceDown();
        hand.addCard(card);
    }

    public void dealCard(Hand hand, Deck deck) {
        if(deck.isEmpty()){
            deck.populateDeck();
        }
        hand.addCard(deck.takeCard());
    }

    public int pay(Player player, double betMultiplier) {
        double amount = player.getBettedAmount() * betMultiplier;
        player.addToBalance((int) amount);
        return (int) amount;
    }

    public int payInsurance(Player player) {
        int amount = player.getInsurance();
        player.addInsuranceToBalance();
        return amount;
    }

    public void takeSideBet(Player player){
        int amount = player.getInsurance();
        player.setInsuranceToZero();
        player.subtractFromBalance(amount);
    }

    public void takeBet(Player player){
        int amount = player.getBettedAmount();
        player.subtractFromBalance(amount);
    }

    public boolean canLookAtFaceDownCard(){
        if (this.hand.getCards().get(0).points() == 10) return true;
        if (this.hand.getCards().get(0).points() == 11) return true;
        return false;
    }

    public boolean firstCardIsAce(){
        return this.hand.getCards().get(0).points() == 11;
    }


}
