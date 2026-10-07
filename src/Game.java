import java.util.ArrayList;

public class Game {

    private Shoe shoe;
    private Hand player;

    private Hand dealerHand;
    private int decks;
    private double penetration;
    private boolean H17;

    private ArrayList<Hand> playerHands = new ArrayList<>();
    private double wageredUnits;

    private final static  int maxHands = 4;
    private final static boolean surrenderAllow = true;
    private final static boolean DAS = true;



    public Game(int numberDecks, double pen, boolean Hit17) {
        decks = numberDecks;
        penetration = pen;
        H17 = Hit17;
        shoe = new Shoe(numberDecks, penetration);
        player = new Hand();
        dealerHand = new Hand();
    }

    public double playRound(int betAmt){
        int bet=betAmt;
        dealInitialCards();
        wageredUnits = 1.0;
        double totalScore = 0;

        //INSURANCE:
        if (getTrueCount() >= 3 && dealerHand.getDealerUpCard().isItAce()){
            wageredUnits += 0.5;
            if (player.isBlackJack()){
                countHoleCard();
                wong();
                return 1.0*bet;
            }
            if (dealerHand.isBlackJack()){
                countHoleCard();
                wong();
                return 0.0;
            }
            totalScore -= 0.5;
        }


        if (dealerHand.isBlackJack()){
            if (player.isBlackJack()){
                countHoleCard();
                wong();
                return 0.0;
            }
            countHoleCard();
            wong();
            return -1.0*bet;
        }
        if (player.isBlackJack()){
            countHoleCard();
            wong();
            return 1.5*bet;
        }
        playerTurn();
        dealerTurn();
        countHoleCard();
        wong();

        if ((shoe.getShoeSize())/((double) (decks*52))  <  (1-penetration)){
            shoe.shuffle();
        }
        for (Hand hand : playerHands){
            totalScore+=determineWinner(hand);
        }
        return totalScore*bet;

    }


    public void dealInitialCards() {
        playerHands.clear();
        player = new Hand();
        dealerHand = new Hand();
        playerHands.add(player);

        player.addCard(shoe.deal());
        player.addCard(shoe.deal());

        dealerHand.addCard(shoe.deal());
        dealerHand.addCard(shoe.deal());

        //subtracting dealer hole card from count:
        int dealerHoleCardValue = dealerHand.getDealerHoleCard().getValue();
        if (dealerHoleCardValue > 9){
            shoe.changeCount(1);
        }
        if (dealerHoleCardValue < 7){
            shoe.changeCount(-1);
        }
    }

    public void playerTurn() {

        for (int i=0; i<playerHands.size(); i++) {
            Hand newHand = playerHands.get(i);
            playHand(newHand);
        }
    }

    public void playHand(Hand newHand){
        while (!newHand.isBust() && !newHand.splitAces) {
            String action = Strategy.getAction(newHand, dealerHand.getDealerUpCard(), playerHands.size(), maxHands, getTrueCount());

            switch (action) {
                case "Hit":
                    newHand.addCard(shoe.deal());
                    break;                  // leaves the switch; the while loop goes around again
                case "Stand":
                    return;
                case "Doubl":
                    if (newHand.getsize() == 2) {
                        newHand.addCard(shoe.deal());
                        wageredUnits += 1.0;
                        newHand.doubled = true;
                    } else {
                        newHand.addCard(shoe.deal());
                        break;
                    }
                    return;
                case "DblS":
                    if (newHand.getsize() == 2) {
                        newHand.addCard(shoe.deal());
                        wageredUnits += 1.0;
                        newHand.doubled = true;
                    }
                    return;
                case "Split":

                    split(newHand);

                    break;
                case "Surr":
                    if (surrenderAllow && newHand.getsize() == 2) {
                        //System.out.println("Surrendered");
                        newHand.isSurrendered = true;
                        return;
                    } else {
                        if (dealerHand.getDealerUpCard().getValue() == 11 && newHand.getHandValue() == 17) {
                            return;
                        }
                        newHand.addCard(shoe.deal());
                        break;
                    }

                default:
                    System.out.println("unknown action: " + action);
                    return;
            }
        }
    }

    public void split(Hand hand){
        if (playerHands.size()>=maxHands){
            return;
        }
        wageredUnits += 1.0;
        Hand splitHand = new Hand();
        splitHand.addCard(hand.removeCard(1));
        splitHand.fromSplit = hand.fromSplit = true;
        if (splitHand.getHandValue()==11){
            splitHand.splitAces = hand.splitAces = true;
        }
        hand.addCard(shoe.deal());
        splitHand.addCard(shoe.deal());
        playerHands.add(playerHands.indexOf(hand) + 1, splitHand);


    }

    public void dealerTurn() {

        if (H17) {
            while (dealerHand.getHandValue() < 17 || (dealerHand.isSoft() && dealerHand.getHandValue() == 17)) {
                dealerHand.addCard(shoe.deal());
            }
        } else {
            while (dealerHand.getHandValue() < 17) {
                dealerHand.addCard(shoe.deal());
            }
        }
    }

    public void countHoleCard(){
        int dealerHoleCardValue = dealerHand.getDealerHoleCard().getValue();
        if (dealerHoleCardValue > 9){
            shoe.changeCount(-1);
        }
        if (dealerHoleCardValue < 7){
            shoe.changeCount(1);
        }
    }

    public double determineWinner(Hand hand){
        int value = hand.getHandValue();
        int dealerValue = dealerHand.getHandValue();
        if (hand.isSurrendered){
            return -.5;
        }
        if (hand.isBlackJack()){
            if (dealerHand.isBlackJack()){
                return 0.0;
            }
            return 1.5;
        }
        if (value> 21){
            if (hand.doubled){
                return -2.0;
            }
            return -1.0;
        }
        if (dealerHand.isBlackJack()){
            return -1.0;
        }

        if (dealerValue>21){
            if (hand.doubled){
                return 2.0;
            }
            return 1.0;
        }
        if (value>dealerValue){
            if (hand.doubled){
                return 2.0;
            }
            return 1.0;
        }else {
            if (dealerValue>value){
                if (hand.doubled){
                    return -2.0;
                }
                return -1.0;
            } else{
                return 0.0;
            }
        }

    }

    public int getTrueCount(){
        return (int) ((shoe.getCount())/(shoe.getShoeSize()/52.0));
    }

    public double getWageredUnits(){
        return wageredUnits;
    }

    public void wong(){
        if (Main.wonging && getTrueCount() <= Main.wongAtOrBelow){
            shoe.shuffle();
        }
    }

}