import java.util.ArrayList;
import java.util.Collections;

public class Shoe {

    private ArrayList<Card> cards;
    private int shoeSize;
    private int numDecks;
    private double deckPenetration;
    private int count;
    private int cardIndex;



    //constructs random shoe with *decks* decks
    public Shoe(int decks, double pen){
        numDecks=decks;
        shoeSize=decks*52;
        deckPenetration=pen;
        cards = new ArrayList<>();
        count = 0;
        cardIndex = 0;




        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (int i=0; i<numDecks; i++){
            for (String rank : ranks){
                for (int j=0; j<4; j++){
                    cards.add(new Card(rank));
                }
            }
        }

        shuffle();

    }


    public void shuffle(){
        Collections.shuffle(cards);
        count = 0;
        shoeSize = numDecks*52;
        cardIndex = 0;
    }

    public Card deal(){
        shoeSize--;

        if (cards.get(cardIndex).getValue()<7){
            count++;
        }else{
            if (cards.get(cardIndex).getValue()>9){
                count--;
            }
        }
        //System.out.println(count);
        return cards.get(cardIndex++);
    }

    public int getCount(){
        return count;
    }

    public int getShoeSize(){
        return shoeSize;
    }

    public void changeCount (int change){
        count +=change;
    }

    public int getCardIndex(){
        return cardIndex;
    }
}
