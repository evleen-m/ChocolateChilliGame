import java.util.Random;
import java.util.Scanner;

public class chocoL5
{
    public static void main (String [] a)
    {

        int numberOfGames = startApp();
        int numStartChoco = getChocolates(); // same number of choco used for each game

        String[][] gameInfo = new String[numberOfGames][2];
        GameState[] possibleCompMoves = buildStateTable(numStartChoco+1);

        for (int i = 1; i <= numberOfGames; i++)
        {
            // start of a new game - store new name in array


            String name = getName();
            int numCurrentChoco = numStartChoco;
            System.out.println("Thanks, " + name + "! There are " + numCurrentChoco + " chocolates on the table. I will go first.");

            int playerFlag = -1; // even(0) = comp and odd(1) = user

            int previousChocoNum = 0;
            int removed = 0;

            while (numCurrentChoco > 0) // game loop,
            {
                previousChocoNum = numCurrentChoco;

                // computer turn
                playerFlag++;
                numCurrentChoco = computerTurn(numCurrentChoco, possibleCompMoves); // does comp turn
                removed = previousChocoNum - numCurrentChoco;

                //gameEnded = checkGameEnded(numCurrentChoco); // checks if game ended

                //previousChocoNum = numCurrentChoco;
                //player turn - only if greater than 0
                if (numCurrentChoco > 0)
                {
                    playerFlag++;
                    numCurrentChoco = playerTurn(numCurrentChoco); // does player turn
                    //gameEnded = checkGameEnded(numCurrentChoco); // checks if game ended
                }
            }

            String whoWon = checkWhoWon(playerFlag);

            //printrray(possibleCompMoves);
            //computer made a move that lead to immediate loss
            if (whoWon.equals("player"))
            {

                var gs = possibleCompMoves[previousChocoNum];

                if (removed == 1)
                {
                    gs.EatOne = false;
                    //System.out.println("1 removed at pos:" + previousChocoNum );
                }
                else if (removed == 2)
                {
                    gs.EatTwo = false;
                    //System.out.println("2 removed at pos: " + previousChocoNum);
                }
                else if (removed == 3)
                {
                    gs.EatThree = false;
                    //System.out.println("3 removed at pos: " + previousChocoNum);
                }
                possibleCompMoves[previousChocoNum] = gs;
                //System.out.println("updated table");
            }
            //printrray(possibleCompMoves);

            gameInfo[i-1][0] = name;
            gameInfo[i-1][1] = whoWon;

        }

        PrintResults(gameInfo);
        calculateStatistics(gameInfo);
    }

    private static GameState[] buildStateTable(int length)
    {
        GameState[] possibleCompMoves = new GameState[length];
        int len  = possibleCompMoves.length;

        // first element
        GameState gs = setGameState(true, false,false);
        possibleCompMoves[1] = gs;
        //second element
        gs = setGameState(true, true, false);
        possibleCompMoves[2] = gs;

        // all other elements
        for (int i = 3; i < len; i++)
        {
            gs = setGameState(true, true,true);
            possibleCompMoves[i] = gs;
        }

        return possibleCompMoves;
    }

    private static GameState setGameState(boolean eatOne, boolean eatTwo, boolean eatThree)
    {
        GameState g = new GameState();
        g.EatOne = eatOne;
        g.EatTwo = eatTwo;
        g.EatThree = eatThree;
        return g;
    }


    // store player names and their win counts in separate arrays
    private static void calculateStatistics(String[][] gameInfo)
    {
        int length = gameInfo.length;
        String[] playerNames = new String[gameInfo.length];
        int[] winCount = new int[gameInfo.length];
        int winningPlayerCounter = 0; // how many new winning players have been identified

        //loop through current array to find winning names
        for (int i = 0; i < length; i++)
        {
            if(gameInfo[i][1].equals("player"))
            {
                String name = gameInfo[i][0];
                int index = findIndex(playerNames, name); // find index of existing instance of any winning names

                if (index == -1) // no existing instance -> create new instance
                {
                    playerNames[winningPlayerCounter] = name;
                    winCount[winningPlayerCounter] = 1;
                    winningPlayerCounter += 1;
                }
                else // already existing instance
                {
                    winCount[index] = winCount[index] + 1;
                }
            }
        }

        //sort array of player wins into descending order
        length = winningPlayerCounter - 1;
        boolean swapped;
        for (int i = 0; i < length; i++)
        {
            swapped = false;
            for (int j = 0; j < length-i; j++ )
            {
                if (winCount[j] < winCount[j+1])
                {
                    // swap values in both arrays
                    String tempName;
                    int tempWin;
                    tempWin = winCount[j];
                    winCount[j] = winCount[j+1];
                    winCount[j+1] = tempWin;

                    tempName = playerNames[j];
                    playerNames[j] = playerNames[j+1];
                    playerNames[j+1] = tempName;

                    swapped = true;
                }
            }

            // If no two elements were
            // swapped by inner loop, then break
            if (swapped == false)
            {
                i = length+1;
            }
        }

        //printRes(playerNames, winCount);
        System.out.println(playerNames[0] + " got the highest score with " + winCount[0] + " wins!!");
    }



    // Linear-search function to find the index of an element
    public static int findIndex(String[] a, String t)
    {
        int length = a.length;
        int i = 0;

        // traverse in the array
        while (i < length) {

            if (a[i] == null)
            {
                return -1;
                // no values at this point in array -> no values in array past this so can return 1
            }
            // if the i-th element is t
            // then return the index
            if (a[i].equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }

        return -1;
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

    // is run once game has ended - prints who had won or lost
    public static String checkWhoWon(int playerFlag)
    {
        playerFlag = playerFlag % 2; // check if even(comp) or odd(player)

        if (playerFlag == 0) // comp wins
        {
            System.out.println("YES!\n" + "You have lost. I win.");
            return "computer"; // return comp won
        }
        else {
            System.out.println("Oh no!! There are none left. \n I have to eat a chilli. \n I lost. You win."); // comp loses
            return "player"; // return player won
        }
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
    public static int computerTurn(int chocoNum, GameState[] possibleCompMoves)
    {
        boolean endCompTurn = false;
        int toRemove = 0;

        while (!endCompTurn) {

            toRemove = getComputerMove(chocoNum, possibleCompMoves);
            boolean isLegalRemoval = isRemovalLegal(chocoNum,toRemove);

            if(isLegalRemoval)
            {
                endCompTurn = true;
            }
        }
        if (toRemove != 0) {
            chocoNum = chocoNum - toRemove;
            System.out.println("I have removed " + toRemove);
            System.out.println("There are now " + chocoNum);
            return chocoNum;
        }
        chocoNum = 0;
        return chocoNum;
    }

    private static int getComputerMove(int chocoNum, GameState[] possibleCompMoves)
    {
        System.out.println("ran");
        int temp = rollDice();
        if ((temp == 1) & (getEatOne(possibleCompMoves[chocoNum])))
        {
            return temp;
        }
        else if ((temp == 2) & (getEatTwo(possibleCompMoves[chocoNum])))
        {
            return temp;
        }
        else if ((temp == 3) & (getEatThree(possibleCompMoves[chocoNum])))
        {
            return temp;
        }

        boolean existsValidMoves = checkIfValidMoveRemaining(possibleCompMoves[chocoNum]);
        if (!existsValidMoves)
        {
            System.out.println ("I have resigned");
            return 0;
        }

        temp = getComputerMove(chocoNum, possibleCompMoves);
        return temp;
    }

    private static boolean checkIfValidMoveRemaining(GameState gs)
    {
        if ((!gs.EatOne) & (!gs.EatTwo) & (!gs.EatThree) )
        {
            return false;
        }
        return true;
    }

    private static boolean getEatTwo(GameState gs)
    {
        return gs.EatTwo;
    }

    private static boolean getEatOne(GameState gs)
    {
        return gs.EatOne;
    }

    private static boolean getEatThree(GameState gs)
    {
        return gs.EatThree;
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

    /**
     *  The following methods are all used to print stuff
     */

    // this method is used to print the rules of the game
    public static void printRules()
    {
        System.out.println("Welcome to the Chilli Chocolate Game");
        System.out.println("The rules are simple: \n We will take turns to take a number of chocolates from the table at a time (the maximum in one turn is three) \n The person who eats teh last chocolate will be told to eat a chili and therefor loses");
        return;
    }

    private static void printrray(GameState[] possibleCompMoves)
    {
        for(int i = 1; i < possibleCompMoves.length; i++)
        {
            System.out.println("item " + i);
            System.out.println("eat1:" + getEatOne(possibleCompMoves[i]) + " eat2: " + getEatTwo(possibleCompMoves[i]) + " eat3: " + getEatThree(possibleCompMoves[i]));
        }
    }

    private static void printRes(String[] playerNames, int[] winCount)
    {
        for ( int i = 0; i < playerNames.length; i++)
        {
            System.out.println(playerNames[i] + " got " + winCount[i] + " wins");
        }
    }

    private static void PrintResults(String[][] gameResults)
    {
        for (int i = 0; i < gameResults.length; i++)
        {
            System.out.println(gameResults[i][0] +" " + gameResults[i][1]);
        }
    }



} // END class chatbot1


