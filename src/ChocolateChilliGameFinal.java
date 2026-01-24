import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ChocolateChilliGameFinal
{
    public static void main (String [] a) throws IOException {
        printRules();
        int numberOfGames = getNumOfGames();
        int startChocoNum = getValidStartingChocoNum(); // same number of choco used for each game
        String[][] gameInfo = new String[numberOfGames][2];
        GameState[] possibleCompMoves = buildStateTable(startChocoNum+1);
        String whoWon = "";

        for (int i = 1; i <= numberOfGames; i++)
        {
            // start of a new game - store new name in array
            String name = getName();
            int currentChocoNum = startChocoNum;
            System.out.println("Thanks, " + name + "! There are " + currentChocoNum + " chocolates on the table. I will go first.");

            int playerFlag = -1; // even(0) = comp and odd(1) = user
            int previousChocoNum = 0;
            int numRemovedChocoByComp = 0;

            while (currentChocoNum > 0) // game loop,
            {
                // previousChocoNum only updates before comp turn -> stores the value of  chocolates before comp did its previous turn
                previousChocoNum = currentChocoNum;

                // computer turn
                playerFlag++;
                currentChocoNum = computerTurn(currentChocoNum, possibleCompMoves);
                numRemovedChocoByComp = previousChocoNum - currentChocoNum;

                //player turn - only if greater than 0
                if (currentChocoNum > 0)
                {
                    playerFlag++;
                    currentChocoNum = playerTurn(currentChocoNum);
                }
            }

            // get player or comp won
            whoWon = checkWhoWon(playerFlag, currentChocoNum);

            if (!whoWon.equals("paused"))
            {
                updateTable(whoWon, numRemovedChocoByComp, possibleCompMoves, previousChocoNum);

                // stores name and whoWon in 2d array gameInfo
                gameInfo[i-1][0] = name;
                gameInfo[i-1][1] = whoWon;
                //i++;
            }
            else
            {
                System.out.println("BEause you paused the game, I cannot tell you the results");
                i = numberOfGames + 1; // breaks out of for loop
            }
        }
        if (!whoWon.equals("paused"))
        {
            calculateStatistics(gameInfo);
        }
        endGame(startChocoNum, possibleCompMoves);

    }
    // -------------------------unclassified methods??--------------------------------------

    public static void endGame(int chocoNum, GameState[] possibleMoves ) throws IOException
    {
        String fileName = createFileName(chocoNum);
        checkForExistingFile(fileName);
        saveToFile(fileName, possibleMoves);
        System.out.println("file saved. Game ended");
    }


    // is run once game has ended - prints who had won or lost
    public static String checkWhoWon(int playerFlag, int chocoNum)
    {
        if (chocoNum == -1) // comp had resigned so player wins
        {
            System.out.println("Because I have resigned, I lost. You win.");
            return "player"; // return player won
        }

        if (chocoNum == -2)
        {
            System.out.println("The game has been paused");
            return "paused"; //paused so neither computer or player has won
        }


        playerFlag = playerFlag % 2; // check if even(comp) or odd(player)
        if (playerFlag == 0) // comp wins
        {
            System.out.println("YES!\n" + "You have lost. I win.");
            return "computer"; // return comp won
        }
        else
        {
            System.out.println("Oh no!! There are none left. \n I have to eat a chilli. \n I lost. You win."); // comp loses
            return "player"; // return player won
        }
    }


    // this method is used to print the rules of the game
    public static void printRules()
    {
        System.out.println("Welcome to the Chilli Chocolate Game");
        System.out.println("The rules are simple: \n We will take turns to take a number of chocolates from the table at a time (the maximum in one turn is three) \n The person who eats teh last chocolate will be told to eat a chili and therefore loses");
        return;
    }

    // ----------------------ADT accessor methods--------------------------------------------------

    // gets the value of EatOne from a record
    public static boolean getEatOne(GameState gs)
    {
        return gs.EatOne;
    }

    // gets the value of EatTwo from a record
    public static boolean getEatTwo(GameState gs)
    {
        return gs.EatTwo;
    }

    // gets the value of EatThree from a record
    public static boolean getEatThree(GameState gs)
    {
        return gs.EatThree;
    }

    // this is a general setter, used to create and return a record that stores the values passed in to the method
    public static GameState setGameState(boolean eatOne, boolean eatTwo, boolean eatThree)
    {
        GameState g = new GameState();
        g.EatOne = eatOne;
        g.EatTwo = eatTwo;
        g.EatThree = eatThree;
        return g;
    }

    public static GameState setEatOne(GameState gs, boolean toSet)
    {
        gs.EatOne = toSet;
        return gs;
    }
    public static GameState setEatTwo(GameState gs, boolean toSet)
    {
        gs.EatTwo = toSet;
        return gs;
    }
    public static GameState setEatThree(GameState gs, boolean toSet)
    {
        gs.EatThree = toSet;
        return gs;
    }


    // --------------------------other ADT methods ------------------------------------------

    // update the computer move table if it made a move that resulted in it losing
    public static void updateTable(String whoWon, int removedByComp, GameState[] possibleCompMoves, int previousChocoNum)
    {
        if (whoWon.equals("player"))
        {
            GameState gs = possibleCompMoves[previousChocoNum];

            if (removedByComp == 1)
            {
                gs = setEatOne(gs, false);
                //System.out.println("1 removed at pos:" + previousChocoNum );
            }
            else if (removedByComp == 2)
            {
                gs = setEatTwo(gs, false);
                //System.out.println("2 removed at pos: " + previousChocoNum);
            }
            else if (removedByComp == 3)
            {
                gs = setEatThree(gs, false);
                //System.out.println("3 removed at pos: " + previousChocoNum);
            }
            possibleCompMoves[previousChocoNum] = gs;
            //System.out.println("updated table");
        }
    }

    // this method builds the array of records used to store the possible moves the computer can do given a certain number of remaining chocolates
    public static GameState[] buildStateTable(int length) throws IOException
    {
        String fileName = createFileName(length - 1);
        GameState[] possibleCompMoves = new GameState[length];

        if (checkForExistingFile(fileName))
        {
            possibleCompMoves = rebuildCompTable(fileName, length);
        }
        else
        {
            int len = possibleCompMoves.length;

            // first element
            GameState gs = setGameState(true, false, false);
            possibleCompMoves[1] = gs;

            //second element
            gs = setGameState(true, true, false);
            possibleCompMoves[2] = gs;

            // all other elements
            for (int i = 3; i < len; i++) {
                gs = setGameState(true, true, true);
                possibleCompMoves[i] = gs;
            }
        }
        return possibleCompMoves;
    }


    // --------------------------get input methods-------------------------------------------

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

    // this is a general method that is used to get any string input from the user
    public static String getStringInput (String question)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println(question);
        String textInput = sc.nextLine();
        return textInput;
    }

    // method used to get the number of games (rounds) the user want to play
    public static int getNumOfGames()
    {
        String askNumOfGames = "How many games would you like to play?";
        int numberOfGames = getIntInput(askNumOfGames);

        System.out.println("Okay. We will play " + numberOfGames + " games!");
        return numberOfGames;

    }

    // method used to get the number of starting chocolates that will be used for each round
    public static int getValidStartingChocoNum()
    {
        String askChocoNumber = "How many chocolates would you like to start with?";
        int startChocoNum = getIntInput(askChocoNumber);
        while (startChocoNum <= 3)
        {
            System.out.println("Please enter a value greater than 3");
            startChocoNum = getIntInput(askChocoNumber);
        }

        return startChocoNum;
    }

    // method used to get the name of the player at the start of each round
    public static String getName()
    {
        String askNameQuestion = "What is your name?";
        String name = getStringInput(askNameQuestion);

        return name;
    }

    // ----------------------player and computer turn methods---------------------------------

    // controls the players turn - returns the new value of currentChocoNum
    public static int playerTurn(int chocoNum)
    {
        boolean isPaused = isGamePaused();

        if (isPaused == false)
        {
            String question = "How many chocolates would you like to remove? (max 3)";
            int toRemove = getIntInput(question);

            while ((!isValidRemovalAmount(toRemove)) | (!isRemovalLegal(chocoNum, toRemove))) {
                System.out.println("Invalid Number");

                if (isValidRemovalAmount(toRemove)) {
                    System.out.println("Please enter a number between 1 and 3");
                } else {
                    System.out.println("Please enter a number that is not greater than the current number of available chocolates and is between 1 and 3");
                }
                toRemove = getIntInput(question);
            }

            chocoNum = chocoNum - toRemove;
            return chocoNum;
        }
        else
        {
            return chocoNum = -2; // new case -> if choco num is -2 the game has been paused.
        }
    }

    public static boolean isGamePaused ()
    {
        String question = "do want to pause y or n?";
        String pause = getStringInput(question);

        if (pause.equals("y"))
        {
            return true;
        }
        return false;
    }



    // controls the computers turn, returns the new value of numCurrentChoco
    public static int computerTurn(int chocoNum, GameState[] possibleCompMoves)
    {
        int toRemove = getValidComputerToRemove(chocoNum, possibleCompMoves);

        while (!isRemovalLegal(chocoNum, toRemove))
        {
            toRemove = getValidComputerToRemove(chocoNum, possibleCompMoves);
        }

        if (toRemove != 0) // comp not resigned, game continues
        {
            chocoNum = chocoNum - toRemove;
            System.out.println("I have removed " + toRemove);
            System.out.println("There are now " + chocoNum);
            return chocoNum;
        }

        // is only run when the computer has resigned
        chocoNum = -1;
        return chocoNum;
    }

    // gets a random value (1-3) for the computer to remove, ensures it is a valid move by checking the possible moves array
    public static int getValidComputerToRemove(int chocoNum, GameState[] possibleCompMoves)
    {
        GameState gs = possibleCompMoves[chocoNum];
        int toRemove = rollDice();
        if ((toRemove == 1) & (getEatOne(gs)))
        {
            return toRemove;
        }
        else if ((toRemove == 2) & (getEatTwo(gs)))
        {
            return toRemove;
        }
        else if ((toRemove == 3) & (getEatThree(gs)))
        {
            return toRemove;
        }

        // if there are no possible moves the computer will resign
        if (!checkIfValidMoveRemaining(gs))
        {
            System.out.println ("I have resigned");
            return 0;
        }
        toRemove = getValidComputerToRemove(chocoNum, possibleCompMoves);
        return toRemove;
    }

    // returns a ranodm number from 1 to 3 - change var names
    public static int rollDice()
    {
        Random random = new Random();

        int random0to2 = random.nextInt(3);
        int randomNumber = random0to2 + 1;

        return randomNumber;
    }


    // ---------------------validation methods for player and comp moves---------------------------------

    //ensures a valid number has been entered between ranges of 3 and 1
    public static boolean isValidRemovalAmount(int toRemove)
    {
        if ((toRemove > 3) | (toRemove < 1))
        {
            System.out.println("f");
            return false;
        }
        return true;
    }

    // checks if there are any possible moves left for computer to do
    public static boolean checkIfValidMoveRemaining(GameState gs)
    {
        if ((!getEatOne(gs)) & (!getEatTwo(gs)) & (!getEatThree(gs)) )
        {
            return false;
        }
        return true;
    }

    // ensures the value of currentChocoNum can not go below 0
    public static boolean isRemovalLegal(int chocoNum,int remove)
    {
        if (chocoNum - remove < 0)
        {
            return false;
        }
        return true;
    }

    // -------------------calculate statistics methods-------------------------------

    // store player names and their win counts in separate arrays
    public static void calculateStatistics(String[][] gameInfo)
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

                if (index == -1) // no existing instance ->   new instance
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
        printWinner(winningPlayerCounter-1, winCount, playerNames);
    }

    // this mthod is used to determine which player has the highest number of wins and prints the name with their win count
    public static void printWinner(int length, int[] winCount, String[] playerNames)
    {
        //sort array of player wins into descending order using a bubble sort

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
            if (!swapped)
            {
                i = length+1;
            }
        }

        //print player with highest wins
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

    // ----------------------file I/O methods-------------------------

    // Rebuilds an array of GameState objects by reading saved data from a file.
    // Each line in the file contains three boolean values: eatOne, eatTwo, eatThree.
    // chocoNum determines the size of the GameState array to reconstruct.
    public static GameState[] rebuildCompTable(String fileName, int chocoNum) throws IOException
    {
        BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
        String s = inputStream.readLine();

        GameState[] possibleCompMoves = new GameState[chocoNum+1];
        int i = 1;

        while(s!=null)
        {
            String[] array = s.split(",");

            boolean eatOne = Boolean.parseBoolean(array[0]);
            boolean eatTwo = Boolean.parseBoolean(array[1]);
            boolean eatThree = Boolean.parseBoolean(array[2]);

            GameState gs = setGameState(eatOne, eatTwo, eatThree);
            possibleCompMoves[i] = gs;
            i++;

            s = inputStream.readLine();
        }
        inputStream.close();
        return possibleCompMoves;
    }

    // checks if the file to read/write to already exists. If it does not, the file is then created.
    // returns true if file had previously existed and false if it was created
    public static boolean checkForExistingFile(String fileName) throws IOException
    {
        File file = new File(fileName); // Create File object
        if (file.createNewFile())
        {           // Try to create the file
            System.out.println("File created: ");
            return false;
        }
        else
        {
            System.out.println("File already exists.");
            return true;
        }
    }

    // uses the starting number of chocolates to generate the fileName of which data should be retrieved from/stored in
    public static String createFileName (int chocoNum)
    {
        String num = Integer.toString(chocoNum);
        String fileName = "data"+num+".csv";
        return fileName;
    }



    // used to save information aboutthe possible computer game moves to a file
    // each line in the file represents one GameState's boolean properties
    public static void saveToFile(String fileName, GameState[] possibleMoves) throws IOException
    {
        PrintWriter outputStream = new PrintWriter (new FileWriter(fileName));

        for (int i = 1; i < possibleMoves.length - 1; i++)
        {
            GameState gs = possibleMoves[i];

            String eatOne = Boolean.toString(getEatOne(gs));
            String eatTwo = Boolean.toString(getEatTwo(gs));
            String eatThree = Boolean.toString(getEatThree(gs));
            System.out.println(i+ " " + eatOne+" " + eatTwo + " " + eatThree);

            String toSave = eatOne + "," + eatTwo + "," + eatThree;

            outputStream.println(toSave);
        }
        outputStream.close();
    }

} // END class