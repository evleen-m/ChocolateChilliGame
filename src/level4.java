import java.util.Random;
import java.util.Scanner;

public class level4
{
    public static void main (String [] a)
    {
        int numCurrentChoco = startApp();
        int playerFlag = 0; // even = comp and odd = user
        // boolean gameEnded = false;

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

        endApp(playerFlag);

    }

    /** the first method that is run when the game starts
     *   -> calls method to print rules and gets username and start choco number
     * @return choco number
     */
    public static int startApp()
    {
        printRules();

        String askNameQuestion = "What is your name?";
        String name = getStringInput(askNameQuestion);

        String askChocoNumber = "How many chocolates would you like to start with?";
        int numStartChoco = getIntInput(askChocoNumber);

        System.out.println("Thanks, " + name + "! There are " + numStartChoco + " chocolates on the table. I will go first.");
        return numStartChoco;
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

        while (!endCompTurn)
        {
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

    // returns a ranodm number from 1 to 3
    public static int rollDice()
    {
        Random random = new Random();

        int random0to2 = random.nextInt(3);
        int randomNumber = random0to2 + 1; // increases limits to 1-3

        return randomNumber;
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

            if ((isValid == true) && (isLegalRemoval == true))
            {
                endPlayerTurn = true;
            }
            else
            {
                System.out.println("Invalid Number");
            }
        }
        chocoNum = chocoNum - toRemove;
        System.out.println(chocoNum);
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

    // checks game has ended
    public static boolean checkGameEnded(int chocoNum)
    {
        if (chocoNum <= 0)
        {
            return true; // game ended
        }
        return false; // game not ended
    }

    // is run once game has ended - prints who has won or lost
    public static void endApp(int playerFlag)
    {
        playerFlag = playerFlag % 2; // check if odd or even

        if (playerFlag == 0) // computer loses
        {
            System.out.println("Oh no. \n I have eaten a chilli. \n I lost. You win.");
        }
        else {
            System.out.println("YES! \n You have lost. I win.");
        }
    }


} // END class chatbot1