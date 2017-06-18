/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package public_goods_game;

import java.util.Scanner;

/**
 *
 * @author Rodrigo
 */
public class Public_Goods_Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Contribution Simulator 1.0\n");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		int choose;
		do {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\n	    Main Menu\n");
			System.out.println("1. Start Game");
			System.out.println("2. Theory");
			System.out.println("3. Instructions");
			System.out.println("4. Exit");
			System.out.println("\nSelect a Command :");

			do {
				while (!scan.hasNextInt())
				{
					System.out.println("Error : InputMismatchException\n Select a Command :");
					scan.next();
				}
			}while (!scan.hasNextInt());
            choose = scan.nextInt();
            int players;
			int neighSize;
			int rounds;
			boolean lock1 = true;
			boolean lock2 = true;
			switch(choose) {
			case 1 :// Difficulty setting has fixed parameters
				int mode;
				do {
					System.out.println("-------------------------------------------------");
					System.out.println("\n	    Difficulty \n");
					System.out.println("1. Easy   => (5  Players, 2 Neighbors, 10 Rounds)");
					System.out.println("2. Medium => (9  Players, 4 Neighbors, 15 Rounds)");
					System.out.println("3. Hard   => (13 Players, 6 Neighbors, 20 Rounds)");
					System.out.println("\nSelect Difficulty :");


					 while (!scan.hasNextInt())
						{
							System.out.println("Error : InputMismatchException\n Select Difficulty :");
							scan.next();
						}
	                  mode = scan.nextInt();
	         	}while (mode < 1 || mode >3);
				if (mode==1) {
					players = 5;
					neighSize = 3;
					rounds = 10;
				}
				else if (mode==2) {
					players = 9;
					neighSize = 5;
					rounds = 15;
				}
				else {
					players = 13;
					neighSize = 7;
					rounds = 20;
				}
				scan = new Scanner(System.in);
				int cRound = 0;
				
				ContributionGame game = new ContributionGame(players,neighSize);
				char ch;
				game.createPopulation();
				Node player = game.getHeader();
				while (cRound != rounds) {
					int command;
					do {
						
						System.out.println("---------------Round "+(cRound+1)+"---------------");
						//Once you unlock an ability it's yours to keep
						System.out.println("\n	    Game Commands\n");
						System.out.println("1. Contribute");
						if (lock2==true) {
							System.out.println("2. Peek at Neighborhood - Locked (Unlock for 45)");
						}
						else {
							System.out.println("2. Peek at Neighborhood - Unlocked");
						}
						if (lock1==true) {
							System.out.println("3. Peek at Population - Locked (Unlock for 135)");
						}
						else {
							System.out.println("3. Peek at Population - Unlocked");
						}
						System.out.println("Your Cash = "+player.getBank());
						System.out.println("\nSelect a Command :");

						while (!scan.hasNextInt())
						{
							System.out.println("Error : InputMismatchException\n Select a Command :");
							scan.next();
						}
			    	}while (!scan.hasNextInt());
		            command = scan.nextInt();
					switch(command) {
					case 1 :
						game.resetPayoff();
						game.contribute();
						if (lock1 == false) {
							game.getPopulation();// Calls method in contribution file. Shows population statistics.
						}
						else if (lock2 == false) {
							game.getNeighborhood();// Shows neighborhood statistics
						}
						else {
							game.summTurn();
						}
						cRound++;
						break;
					case 2 :
						if (lock1==true) {
							System.out.println("\nUnlock this ability for 45? (Type y or n) \n");
				            ch = scan.next().charAt(0);
				            while ((ch != 'y' && ch != 'Y') && (ch != 'n' && ch != 'N'))
							{
								System.out.println("Invalid input\nUnlock this ability for 45? (Type y or n)");
								ch = scan.next().charAt(0);
							}
				            if (ch == 'y' || ch == 'Y') {
				            	if (player.getBank() >= 45) {
									game.getNeighborhood();
									lock2 = false;
									player.setBank(player.getBank()-45);
								}
								else {
									System.out.println("Not enough Cash!");
								}
				            }
						}
						break;
					case 3 :
						if (lock1==true) {
							System.out.println("\nUnlock this ability for 135? (Type y or n) \n");
				            ch = scan.next().charAt(0);
				            while ((ch != 'y' && ch != 'Y') && (ch != 'n' && ch != 'N'))
							{
								System.out.println("Invalid input\nUnlock this ability for 135 ? (Type y or n)");
								ch = scan.next().charAt(0);
							}
				            if (ch == 'y' || ch == 'Y') {
				            	if (player.getBank() >= 135) {
									game.getPopulation();
									lock1 = false;
									player.setBank(player.getBank()-135);
								}
								else {
									System.out.println("Not enough Cash!");
								}
				            }
						}
						else {
						System.out.println("You already bought this ability");
						}
						break;
					default : 
	                    System.out.println("Error : IndexOutOfBoundsException\n Select a command :");
	                    break;
					}
				}
				//Shows results at the end of rounds 
				int num = game.getPlayers();
				float winner = game.checkWinner(num);
				System.out.println("------Results-------------------------");
				if (player.getBank() == winner) {
					System.out.println("		YOU WIN!");
				}
				else {
					System.out.println("		YOU LOSE!");
				}
				game.getPopulation();
				System.out.println("\nYour Score =     "+player.getBank()+"\nHighest Score =  "+winner);//Returns individual private bank accounts
				System.out.println("Social Welfare = "+game.getWelfare());//Gets game welfare 
				System.out.println("Max Welfare =	 "+((50*num)*rounds));
				break;
			case 2 :
				do {
					System.out.println("Enter Number of Players :");
					 while (!scan.hasNextInt())
						{
							System.out.println("Error : Enter Number of Players :");
							scan.next();
						}
	                 players = scan.nextInt();
	         	}while (players < 1);
				
				do {
					System.out.println("Enter Number of Neighbors :");
					 while (!scan.hasNextInt())
						{
							System.out.println("Error : Enter Number of Neighbors :");
							scan.next();
						}
	                 neighSize = scan.nextInt();
	         	}while (neighSize < 1 || neighSize > (players-1));
				
				rounds = 0;
				//Theory option has no fixed parameters
				ContributionGame game2 = new ContributionGame(players,(neighSize+1));
				game2.createPopulation();
				game2.getPopulation();
				Node player2 = game2.getHeader();
				int command;
				do {
					do {
						//Theory option ends when you wan it to end
						System.out.println("---------------Round "+(rounds+1)+"---------------");
						
						System.out.println("\n	    Game Commands\n");
						System.out.println("1. Contribute");
						System.out.println("2. Welfare");
						System.out.println("3. Quit");
						System.out.println("\nSelect a Command :");

						while (!scan.hasNextInt())
						{
							System.out.println("Error : Select a Command:");
							scan.next();
						}
			    	}while (!scan.hasNextInt());
		            command = scan.nextInt();
					switch(command) {
					case 1 :
						game2.resetPayoff();
						game2.contribute();
						game2.getPopulation();
						rounds++;
						break;
					case 2 :
						//Option updates you every turn
						System.out.println("------Welfare-------------------------");
						int num2 = game2.getPlayers();
						float winner2 = game2.checkWinner(num2);
						System.out.println("\nYour Score =     "+player2.getBank()+"\nHighest Score =  "+winner2);
						System.out.println("Social Welfare = "+game2.getWelfare());
						System.out.println("Max Welfare =	 "+((50*num2)*(rounds)));
						break;
					case 3 :
						System.out.println("Quitting...");
						break;
					default : 
	                    System.out.println("Error : IndexOutOfBoundsException\n Select a Command :");
	                    break;
					}
				}while (command != 3);
				break;
			case 3 :
				System.out.println("--------------------------------------Instructions----------------------------------------------------");
                                System.out.print("The objective of the game is to obtain the highest score which can be done by making the smart decision at every round. \nThe player can choose to be either an altrusit or egoist, this is decided by entering an amount to contribute.");
                                System.out.println(" The results will be revealed at the end of every round after the player enters an amount. \nThe difficulty comes in that the player does not know how much its neighbors will contribute, so just as the player can benefit from its neighbors so can the neighbors from the player.");
                                System.out.println("Amounts are limited to any number between 1-10.");
				break;
			case 4 :
				System.out.println("Exiting...");
				break;	
			default : 
	            System.out.println("Error : IndexOutOfBoundsException\n Select a Command :");
	            break;
			}
		}while (choose != 4);
        System.out.println("Program is closed");
    }
    
}
