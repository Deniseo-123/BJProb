public class Deviation {
    private String handType;
    private int handValue;
    private int dealerValue;
    private int threshHold;
    private boolean atOrAbove;     // true if above, false if below
    String devAction;

    public Deviation(String Type, int playValue, int dealValue, int thresh, boolean whatSide, String Action){
        handType = Type;
        handValue = playValue;
        dealerValue = dealValue;
        threshHold = thresh;
        atOrAbove = whatSide;
        devAction = Action;
    }

    public boolean matches(Hand hand, int dealValue,int trueCount){
        return (hand.isPair() == handType.equals("Pair") && dealValue == dealerValue && hand.getHandValue()==handValue && ((trueCount >= threshHold && atOrAbove) || (trueCount<=threshHold && !atOrAbove)));
    }

    public String Action(){
        return devAction;
    }
}

