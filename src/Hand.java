import java.util.ArrayList;

public class Hand {

    private int rawValue;
    private int numAces;
    Card dealerUpCard;

    boolean isSurrendered, fromSplit, splitAces, doubled;

    ArrayList<Card> theHand = new ArrayList<>();


    public Hand (){
        rawValue = 0;
        numAces = 0;

        theHand.clear();

    }

    public void addCard(Card card){
        rawValue+=card.getValue();
        if (card.isItAce()){
            numAces++;
        }
        theHand.add(card);
    }


    public int getHandValue(){
        int aces=numAces;
        int value = rawValue;

        while (value>21 && aces>0){
            value-=10;
            aces--;
        }
        return value;
    }

    public Card getDealerUpCard(){
        return theHand.get(0);
    }

    public Card getDealerHoleCard(){
        return theHand.get(1);
    }


    public boolean isBust(){

        return getHandValue() > 21;
    }

    public boolean isBlackJack(){

        return getHandValue() == 21 && theHand.size()==2 && !fromSplit;
    }

    public boolean isPair(){
        return ((theHand.get(0).getValue() == theHand.get(1).getValue()) && theHand.size()==2);
    }




    public boolean isSoft(){
        int value = rawValue;
        int aces = numAces;
        while (value>21 && aces>0){
            value-=10;
            aces--;
        }
        return aces>0;
    }

    public int getsize(){

        return theHand.size();
    }

    public Card removeCard(int i){
        Card removedC = theHand.remove(i);
        rawValue -= removedC.getValue();
        if (removedC.isItAce()){
            numAces--;
        }
        return removedC;
    }
}
