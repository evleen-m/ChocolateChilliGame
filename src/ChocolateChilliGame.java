import java.util.Scanner;
import java.util.Random;

public class ChocolateChilliGame
{
    public static void main (String [] a)
    {
        int numberOfGames = startApp();
        int numStartChoco = getChocolates(); // same number of choco used for each game

        GameResult[] playerWinArray = new GameResult[numberOfGames];

        for (int i = 1; i <= numberOfGames; i++)
        {
            // start of a new game - store new name in array

            String name = getName();
            int numCurrentChoco = numStartChoco;
            System.out.println("Thanks, " + name + "! There are " + numCurrentChoco + " chocolates on the table. I will go first.");

            int playerFlag = -1; // 0 = comp and 1 = user


            while (numCurrentChoco > 0) // game loop,
            {
                // computer turn
                playerFlag++;
                numCurrentChoco = computerTurn(numCurrentChoco); // does comp turn
                //gameEnded = checkGameEnded(numCurrentChoco); // checks if game ended

                //player turn - only if greater than 0
                if (numCurrentChoco > 0)
                {
                    playerFlag++;
                    numCurrentChoco = playerTurn(numCurrentChoco); // does player turn
                    //gameEnded = checkGameEnded(numCurrentChoco); // checks if game ended
                }
            }

            boolean didWin = checkPlayerWon(playerFlag);
            GameResult gr = setGameResult(name, didWin, 0);
            playerWinArray[i-1] = gr;

        }

        PrintResults(playerWinArray);
        getPlayersByWins(playerWinArray);
    }

    private static GameResult setGameResult(String name, boolean didWin, int numberOfWins)
    {
        GameResult g = new GameResult();
        g.PlayerWin = didWin;
        g.PlayerName = name;
        g.NumberOfWins = numberOfWins;
        return g;
    }

    private static boolean getGameResultWon (GameResult gr)
    {
        var temp = gr.PlayerWin;
        return temp;
    }

    private static String getGameResultName(GameResult gr)
    {
        var temp = gr.PlayerName;
        return temp;
    }
    private static int getGameResultNum(GameResult gr)
    {
        var temp = gr.NumberOfWins;
        return temp;
    }


    private static void calculateStatistics(GameResult[] playerWinArray)
    {


    }

    // store player names and their win counts in separate arrays
    private static void getPlayersByWins(GameResult[] gameInfo)
    {
        int length = gameInfo.length;

        GameResult[] winsOnly = new GameResult[gameInfo.length];

        String[] playerNames = new String[gameInfo.length];
        int[] winCount = new int[gameInfo.length];
        int winningPlayerCounter = 0; // how many new winning players have been identified

        //loop through current array to find winning names
        for (int i = 0; i < length; i++)
        {
            var temp = gameInfo[i];
            var temp1 = getGameResultWon(temp);
            if(temp1 == true) // win
            {
                String name = getGameResultName(gameInfo[i]);
                int index = findIndex(winsOnly, name); // find index of existing instance of any winning names

                if (index == -1) // no existing instance -> create new instance
                {
                    GameResult gr = setGameResult(name, true,1 );
                    winsOnly[winningPlayerCounter] = gr;
                    winningPlayerCounter += 1;
                }
                else // already existing instance
                {
                    var a = winsOnly[index];
                    var b = getGameResultNum(a) + 1;
                    GameResult gr = setGameResult(name, true,b);
                    winsOnly[index] = gr;
                    winningPlayerCounter += 1;
                }
            }
        }

//        //sort array of player wins into descending order
//        length = winningPlayerCounter - 1;
//        boolean swapped;
//        for (int i = 0; i < length; i++)
//        {
//            swapped = false;
//            for (int j = 0; j < length-i; j++ )
//            {
//                if (winCount[j] > winCount[j+1])
//                {
//                    // swap values in both arrays
//                    String tempName;
//                    int tempWin;
//                    tempWin = winCount[j];
//                    winCount[j] = winCount[j+1];
//                    winCount[j+1] = tempWin;
//
//                    tempName = playerNames[j];
//                    playerNames[j] = playerNames[j+1];
//                    playerNames[j+1] = tempName;
//
//                    swapped = true;
//                }
//            }
//
//            // If no two elements were
//            // swapped by inner loop, then break
//            if (swapped == false)
//            {
//                i = length+1;
//            }
//        }
//
//        System.out.println(playerNames[0] + " got the highest score: " + winCount[0] + " wins");

    }


    // Linear-search function to find the index of an element
    public static int findIndex(GameResult[] a, String t)
    {
        int length = a.length;
        int i = 0;

        // traverse in the array
        while (i < length) {

            // if the i-th element is t
            // then return the index
            String temp = getGameResultName(a[i]);
            if(temp == null)
            {
                return -1;
            }

            if (temp.equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }

        return -1;
    }


//    private static void Tests()
//    {
//        GameResult[] gameResults = new GameResult[3];
//        gameResults[0] = new GameResult("Bob", true);
//        gameResults[0] = new GameResult("Bob", true);
//        gameResults[0] = new GameResult("Bob", true);
//
//        PrintResults(gameResults);
//    }
    private static void PrintResults(GameResult[] gameResults)
    {
        //group
        //sort
        for(var gr: gameResults)
        {
            System.out.println(gr.PlayerName + " " + gr.PlayerWin);
        }
    }

    public static int startApp()
    {
        printRules();
        String askNumOfGames = "How many games would you like to play?";
        int numberOfGames = getIntInput(askNumOfGames);

        System.out.println("Okay. We will play " + numberOfGames + " games!");
        return numberOfGames;

    }

    /** the first method that is run when the game starts
     *   -> calls method to print rules and gets username and start choco number
     * @return choco number
     */
    public static int getChocolates()
    {
        String askChocoNumber = "How many chocolates would you like to start with?";
        int numStartChoco = getIntInput(askChocoNumber);

        return numStartChoco;
    }

    public static String getName()
    {
        String askNameQuestion = "What is your name?";
        String name = getStringInput(askNameQuestion);

        return name;
    }

    // is run once game has ended - prints who has won or lost
    public static boolean checkPlayerWon(int playerFlag)
    {
        playerFlag = playerFlag % 2; // check if odd or even

        if (playerFlag == 0) // computer loses
        {
            System.out.println("Oh no. \n I have eaten a chilli. \n I lost. You win.");
            return true;
        }
        else {
            System.out.println("YES! \n You have lost. I win.");
            return false;
        }
    }

    // this method is used to print the rules of the game
    public static void printRules()
    {
        System.out.println("Welcome to the Chilli Chocolate Game");
        System.out.println("The rules are simple: \n We will take turns to take a number of chocolates from the table at a time (the maximum in one turn is three) \n The person who eats teh last chocolate will be told to eat a chili and therefor loses");
        return;
    }

    // this is a general method that is used to get any string input from the user
    public static String getStringInput (String question)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println(question);
        String textInput = sc.nextLine();
        return textInput;
    }

    // this is a general method that is used to get any integer input from the user
    public static int getIntInput (String question)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println(question);
        String textInput = sc.nextLine();
        int temp = Integer.parseInt(textInput);
        return temp;
        // add what to do if the input is not an integer later.
    }


    // controls the computers turn, returns the new value of numCurrentChoco
    public static int computerTurn(int chocoNum)
    {
        boolean endCompTurn = false;
        int toRemove = 0;

        while (!endCompTurn) {

            toRemove = rollDice();
            boolean isLegalRemoval = isRemovalLegal(chocoNum,toRemove);

            if(isLegalRemoval)
            {
                endCompTurn = true;
            }
        }
        chocoNum = chocoNum - toRemove;
        System.out.println("I have removed " + toRemove);
        System.out.println("There are now " + chocoNum);

        return chocoNum;
    }


    // controls the players turn - returns the new value of numCurrentChoco
    public static int playerTurn(int chocoNum)
    {
        boolean endPlayerTurn = false;
        int toRemove = 0;
        String question = "How many chocolates would you like to remove? (max 3)";

        while (!endPlayerTurn)
        {
            System.out.println("Please enter a number between 1 and 3");
            toRemove = getIntInput(question);

            boolean isLegalRemoval = isRemovalLegal(chocoNum,toRemove);
            boolean isValid = isValidRemovalAmount(toRemove);

            if ((isValid == true) && (isLegalRemoval == true)) {
                endPlayerTurn = true;
            }
            else{
                System.out.println("Invalid Number");
            }
        }
        chocoNum = chocoNum - toRemove;
        //System.out.println(chocoNum);
        return chocoNum;
    }

    //ensures a valid number has been entered between ranges of 3 and 1
    public static boolean isValidRemovalAmount(int toRemove)
    {
        if ((toRemove <= 3) && (toRemove >= 1))
        {
            return true;
        }
        return false;
    }

    // ensures the value of numCurrentChoco can not go below 0
    public static boolean isRemovalLegal(int chocoNum,int remove)
    {
        if (chocoNum - remove >= 0)
        {
            return true;
        }
       return false;
    }


    // returns a ranodm number from 1 to 3 - change var names
    public static int rollDice()
    {
        Random random = new Random();

        int random0to2 = random.nextInt(3);
        int randomNumber = random0to2 + 1;

        return randomNumber;
    }



} // END class chatbot1



