//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static int trueCount = 0;
    static boolean wonging;
    static int wongAtOrBelow;

    public static void main(String[] args) {


        //EDIT YOUR TABLE RULES:

        //How many decks are in use?
        final int numberOfDecks = 8;

        //What portion of cards are dealt before shuffling?
        final double pen = .75;

        //Does dealer Hit on soft 17?
        final boolean H17 = true;

        //How many rounds are played per hour?
        final int hourlyRounds = 100;




        //EDIT YOUR PERSONAL PLAYING STRATEGY:

        //Wonging is leaving the table when the count drops sufficiently low, when the house has an advantage.
        wonging = true;
        wongAtOrBelow = -3;


        //This is your bet spread, which is where a card counter's advantage comes from. Bet higher on higher counts, lower on the low counts.
        //                        -1  0   +1  +2  +3  +4   +5   +6   +7+
        final int[] betAmounts = {10, 10, 20, 40, 80, 120, 150, 160, 160};

        //How much money will you "gamble"? 5k-10k is usually the minimum starting point.
        final int bankRoll = 10000;

        //How many betting rounds do you want to simulate? Simulator usually runs at ~5,000,000 rounds/second.
        final int rounds = 10000000;



        //RUN IT










        Game newgame = new Game(numberOfDecks, pen, H17);
        double aV=0;
        double totalWagered = 0;
        int curBankRoll = bankRoll;
        int numRuins = 0;
        int games = 1;
        double sumOfSquares = 0;


        int numTC0 = 0;
        int numTC1 = 0;
        int numTCn1 = 0;
        int numTC3 = 0;


        for (int i=0; i<rounds; i++){
            int bet;
            trueCount = newgame.getTrueCount();

            if (trueCount<=0){
                bet = betAmounts[0];
            }else{
                if (trueCount>=7){
                    bet = betAmounts[8];
                }else{
                    bet = betAmounts[trueCount+1];
                }
            }
            //System.out.println(trueCount + ",    bet:  " + bet);
            double result = newgame.playRound(bet);
            aV+=result;
            sumOfSquares += result * result;
            totalWagered += newgame.getWageredUnits()*bet;
            curBankRoll += result;
            if (curBankRoll <= bet){
                curBankRoll = bankRoll;
                numRuins++;
                games++;
            }else{
                if (curBankRoll > 10*bankRoll){
                    curBankRoll = bankRoll;
                    games++;
                }
            }



            //if (trueCount == 0){
            //    numTC0++;
            //}
            //if (trueCount == 1){
            //    numTC1++;
            //}
            //if (trueCount == -1){
            //    numTCn1++;
            //}
            //if (trueCount == 3){
            //    numTC3++;
            //}


        }
        System.out.print("Average EV: ");
        System.out.printf("%.5f", aV/totalWagered);
        System.out.print("\nAverage Bet ($):  $");
        System.out.printf("%.2f", totalWagered/rounds);
        System.out.print("\nSimulated Risk of Ruin:  ");
        System.out.printf("%.2f", 100*((double) numRuins/games));
        System.out.println("%" + "\nover " + games + " games\n");
        System.out.print("Expected Value per Hour:  $");
        System.out.printf("%.2f", aV/((double) rounds/hourlyRounds));


        //ror math calculator
        double mean = aV / rounds;
        double variance = (sumOfSquares / rounds) - (mean * mean);
        double ror = Math.exp(-2 * mean * bankRoll / variance);
        System.out.print("\nRoR (based on equation): ");
        System.out.printf("%.2f", ror*100);
        System.out.println("%\n\nRisk of Ruin (or RoR) is the percent chance that, given an infinite amount of rounds, you will lose all of your inital bankroll.\n\n");



        //System.out.println("True Count -1 bets:  " + ((double) numTCn1)/rounds);
        //System.out.println("True Count 0 bets:  " + ((double) numTC0)/rounds);
        //System.out.println("True Count 1 bets:  " + ((double) numTC1)/rounds);
        //System.out.println("True Count 3 bets:  " + ((double) numTC3)/rounds);






        //for (int i = 0; i < 100; i++) {
        //    System.out.println(newShoe.deal().getValue());
        //}
        //System.out.println(newShoe.getShoeSize());
    }
}