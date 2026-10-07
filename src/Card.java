import java.util.List;

public class Card {

    private String card;
    private boolean isAce;
    private int value;



    //constructs a card
    public Card(String c){
        card = c;
        isAce = c.equals("A");

        if (Character.isDigit(c.charAt(0))) {
            value = Integer.parseInt(c);
        } else if (c.equals("A")) {
            value = 11;
        } else {
            value = 10;   // J, Q, K
        }
    }



    public int getValue(){
        return value;
    }


    public boolean isItAce(){
        return isAce;
    }
}
