public class Player {
    public Hand hand = new Hand();
    public Hand splitHand = null;
    private int balance = 1000;
    private int betted = 0;
    private int insurance = 0;
    private boolean doubledDown = false;

    public boolean canDoubleDown() {
        if (this.hand.getScore() == 9 || this.hand.getScore() == 10 || this.hand.getScore() == 11) {
            if (this.hand.getCards().size() == 2) {
                if (!((this.balance - this.betted * 2) < 0))
                    return true;
            }
        }
        return false;
    }

    public int getInsurance() {
        return this.insurance;
    }

    public void setInsuranceToZero() {
        this.insurance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public boolean hasDoubledDown() {
        return doubledDown;
    }

    public void setDoubledDownToFalse() {
        this.doubledDown = false;
    }

    public void addToBalance(int amount) {
        this.balance += amount;
    }

    public void addInsuranceToBalance() {
        this.balance += insurance;
        setInsuranceToZero();
    }

    public void subtractFromBalance(int amount) {
        this.balance -= amount;
    }

    public int getBettedAmount() {
        return this.betted;
    }

    public void bet(int amount) {
        this.betted = amount;
    }

    public void removeBet() {
        this.betted = 0;
    }

    public void doubleDown() {
        this.betted *= 2;
        doubledDown = true;
    }

    public void splitPair() {
        if (this.canSplitAPair()) {
            splitHand = this.hand.split();
        }
    }

    public boolean hasInsurance() {
        return insurance > 0;
    }

    public void makeInsurance(int amount) {
        this.insurance = amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean canSplitAPair() {
        if (this.splitHand == null) {
            return this.hand.isAPair();
        }
        return false;
    }
}
