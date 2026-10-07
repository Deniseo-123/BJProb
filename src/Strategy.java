import java.util.List;

public class Strategy {

    private static final List<Deviation> deviations = List.of(
            new Deviation("Hard", 16, 10, 0, true, "Stand"),
            new Deviation("Hard", 15, 10, 4, true, "Stand"),
            new Deviation("Pair", 20, 5,  5, true, "Split"),
            new Deviation("Pair", 20, 6,  4, true, "Split"),
            new Deviation("Hard", 10, 10, 4, true, "Doubl"),
            new Deviation("Pair", 10, 10, 4, true, "Doubl"), //fallback for pair of 5s
            new Deviation("Hard", 12, 3 , 2, true, "Stand"),
            new Deviation("Hard", 12, 2 , 3, true, "Stand"),
            new Deviation("Hard", 11, 11, 1, true, "Doubl"),
            new Deviation("Hard", 9 , 2 , 1, true, "Doubl"),
            new Deviation("Hard", 10, 11, 4, true, "Doubl"),
            new Deviation("Hard", 9 , 7 , 3, true, "Doubl"),
            new Deviation("Hard", 16, 9 , 5, true, "Stand"),
            new Deviation("Hard", 13, 2 , -1, false, "Hit"),
            new Deviation("Hard", 12, 4 , 0, false, "Hit"),
            new Deviation("Hard", 12, 5, -2, false, "Hit"),
            new Deviation("Hard", 12, 6, -1, false, "Hit"),
            new Deviation("Hard", 13, 3, -2, false, "Hit")
    );


    //                       2        3        4        5        6        7        8        9        10       A
    private static final String[][] hardStrat = {
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //21
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //20
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},  //19
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //18
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Surr" }, //17
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Surr", "Surr", "Surr"},  //16
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Surr", "Hit"},  //15
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"}, //14
                            {"Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"}, //13
                            {"Hit"  , "Hit"  , "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"}, //12
                            {"Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl"}, //11
                            {"Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Hit", "Hit"},  //10
                            {"Hit"  , "Doubl", "Doubl", "Doubl", "Doubl", "Hit", "Hit", "Hit", "Hit", "Hit"}  //9

    };

    private static final String[][] pairStrat = {
            {"Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"}, //A,A
            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //10,10
            {"Split", "Split", "Split", "Split", "Split", "Stand", "Split", "Split", "Stand", "Stand"}, //9,9
            {"Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"}, //8,8
            {"Split", "Split", "Split", "Split", "Split", "Split", "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //7,7
            {"Split", "Split", "Split", "Split", "Split", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //6,6
            {"Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Doubl", "Hit"  , "Hit"  }, //5,5 (play as hard 10)
            {"Hit"  , "Hit"  , "Hit"  , "Split", "Split", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //4,4
            {"Split", "Split", "Split", "Split", "Split", "Split", "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //3,3
            {"Split", "Split", "Split", "Split", "Split", "Split", "Hit"  , "Hit"  , "Hit"  , "Hit"  }  //2,2
    };

    private static final String[][] softStrat = {
            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //A,10 (21)
            {"Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"}, //A,9  (20)
            {"Stand", "Stand", "Stand", "Stand", "DblS" , "Stand", "Stand", "Stand", "Stand", "Stand"}, //A,8  (19)
            {"DblS" , "DblS" , "DblS" , "DblS" , "DblS" , "Stand", "Stand", "Hit"  , "Hit"  , "Hit"  }, //A,7  (18)
            {"Hit"  , "Doubl", "Doubl", "Doubl", "Doubl", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //A,6  (17)
            {"Hit"  , "Hit"  , "Doubl", "Doubl", "Doubl", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //A,5  (16)
            {"Hit"  , "Hit"  , "Doubl", "Doubl", "Doubl", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //A,4  (15)
            {"Hit"  , "Hit"  , "Hit"  , "Doubl", "Doubl", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }, //A,3  (14)
            {"Hit"  , "Hit"  , "Hit"  , "Doubl", "Doubl", "Hit"  , "Hit"  , "Hit"  , "Hit"  , "Hit"  }  //A,2  (13)
    };

    public static String getAction(Hand playerCards, Card dealerCard, int numHands, int maxHands, int TC){

        int value = playerCards.getHandValue();

        //deviations check
        if (value>8 && !playerCards.isSoft()){
            for (Deviation dev : deviations){
                if (dev.matches(playerCards, dealerCard.getValue(), TC)){

                    String recAction = dev.devAction;
                    if (recAction.equals("Split") && numHands>=maxHands){

                    }else{
                        return recAction;
                    }
                    break;
                }
            }

        }

        if (playerCards.isPair() && numHands<maxHands){
            if (playerCards.isSoft()){
                return "Split";
            }
            return pairStrat[11-(value/2)][dealerCard.getValue()-2];
        }else {
            if (value <= 8) {
                return "Hit";
            }
            if (playerCards.isPair() && value==12 && playerCards.isSoft()){
                return "Hit";
            }
        }
        if (playerCards.isSoft()){

            return softStrat[21-value][dealerCard.getValue()-2];
        } else {
            String action = hardStrat[21-value][dealerCard.getValue()-2];
            return action;
        }
    }
}
