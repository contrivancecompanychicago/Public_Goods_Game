/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package public_goods_game;

import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author Rodrigo
 */
public class ContributionGame {
	private Node header;
	private Node tail;
	private boolean altruist = true;
	private boolean egoist = false;
	private int hoodSize;
	private int neighborSize;
	private float socialWelfare;
	private int players;
	private Scanner scan;
	private int aCount;
	private int eCount;
	private float aTotalPay;
	private float eTotalPay;
	private boolean[] traits;
	private int index;
	
	Random random;
	//Initializes variables and sets primary parameters
	public ContributionGame(int players, int hoodSize) {
            socialWelfare = 0;
            aTotalPay = 0;
            eTotalPay = 0;
            aCount = 0;
            eCount = 0;
            index = 0;
            this.hoodSize = hoodSize;
            this.players = players;
            traits = new boolean[players];
            header = null;
            tail = null;
            neighborSize = hoodSize/2;
	}	
	//generates nodes linking them to each other ultimately making a circle
	public void createPopulation() {
            Node player;
            for (int i=0; i<players; i++) {
                if (i==0){
                    //Randomly assigns a trait to each node generated
                    player = new Node((i+1),randomBoolean(),0,0);
                    header = player;
                    tail = player;
                }
                else {
                    player = new Node((i+1),randomBoolean(),0,0);
                    tail.setRight(player);
                    player.setLeft(tail);
                    tail = tail.getRight();	
                }
            }
            header.setLeft(tail);
            tail.setRight(header);
	}
	//Method for calculation contribution of the single
	public void hooligan(Node x, float contribution) {
            Node temp = getLeftestNeighbor(x, neighborSize);
            float bonus = (contribution*5)/hoodSize;
            for (int i=0; i<hoodSize; i++) {
                float bank = temp.getBank();
                float payoff = temp.getPayoff();
                if (temp == x) {
                    bank = ((10-contribution)*2) + bank + bonus;
                    payoff = ((10-contribution)*2) + payoff + bonus;
                }
                else {
                    bank = bank + bonus;
                    payoff = payoff + bonus;
                }
                //Destributes payoffs to everyone on neighborhood
                temp.setBank(bank);
                temp.setPayoff(payoff);
                temp = temp.getRight();
            }
	}	
	//Calculates payoff for every other Node/player
	public void calcPayoff(Node current, float contribution) {
            if (current==header) {
                hooligan(current,contribution);
            }
            else {
                Node temp = getLeftestNeighbor(current, neighborSize);
                float highBonus = (float) (50.00/hoodSize);
                float lowBonus = (float) (15.00/hoodSize);
                //Calculate payoff for Altruists
                if (current.getBoolean()==altruist) {
                    for (int i=0; i<hoodSize; i++) {
                        float bank = temp.getBank();
                        float payoff = temp.getPayoff();
                        bank = bank + highBonus;
                        payoff = payoff + highBonus;
                        temp.setBank(bank);
                        temp.setPayoff(payoff);
                        temp = temp.getRight();
                    }
                }
                //Calculate payoff for Egoists
                else {
                    for (int i=0; i<hoodSize; i++) {
                        float bank = temp.getBank();
                        float payoff = temp.getPayoff();
                        if (temp == current) {
                            bank = bank + 14 + lowBonus;
                            payoff = payoff + 14 + lowBonus;
                        }
                        else {
                            bank = bank + lowBonus;
                            payoff = payoff + lowBonus;
                        }
                        temp.setBank(bank);
                        temp.setPayoff(payoff);
                        temp = temp.getRight();
                    }
                }
            }
	}
	//Method that determines what trait nodes will have next turn
	public void imitDynamics(Node current, int hoodSize) {
            Node start = getLeftestNeighbor(current,neighborSize);
            for (int i=0; i<hoodSize; i++) {
                checkDynamics(start);
                start = start.getRight();
            }
            //Stores next turn traits on an array so that current traits stay
            if (aCount==0) {
                eTotalPay = eTotalPay/eCount;
                traits[index] = egoist;
            }
            else if (eCount==0) {
                aTotalPay = aTotalPay/aCount;
                traits[index] = altruist;
            }
            else {
                aTotalPay = aTotalPay/aCount;
                eTotalPay = eTotalPay/eCount;
                if (aTotalPay > eTotalPay) {
                    traits[index] = altruist;
                }
                else {
                    traits[index] = egoist;
                }
            }
            eTotalPay = 0;
            aTotalPay = 0;
            eCount = 0;
            aCount = 0;
            index++;
	}
	
	public void checkDynamics(Node current) {
            if (current.getBoolean()==altruist) {
                aCount++;
                aTotalPay = aTotalPay + current.getPayoff();
            }
            else {
                eCount++;
                eTotalPay = eTotalPay + current.getPayoff();
            }
	}

	public Node getLeftestNeighbor(Node current, int neighborSize) {
            Node temp = current;
            for (int i=0; i<neighborSize; i++) {
                temp = temp.getLeft();
            }
            return temp;
	}
	
	public void resetPayoff() {
            Node temp = header;
            for (int i=0; i<players; i++) {
                temp.setPayoff(0);
                temp = temp.getRight();
            }
	}
	
	public float getWelfare() {
            Node temp = header;
            for (int i=1; i<=players; i++) {
                socialWelfare = socialWelfare + temp.getBank();
                temp = temp.getRight();
            }
            return socialWelfare;
	}
	
	public void analyzeH(int contribution) {
            if (contribution <= 5) {
                header.setBoolean(egoist);
            }
            else {
                header.setBoolean(altruist);
            }
	}
	
	public int getNeighborSize() {
            return neighborSize;
	}
	//Displays neighborhood information. This includes neighbors traits
	public void getNeighborhood() {
            System.out.println("-------------------------------------\n	    Peek at Neighborhood\n");
            Node head = header;
            Node temp = getLeftestNeighbor(head,neighborSize);
            boolean pass = false;
            for (int i=0; i<hoodSize; i++) {
                if (temp == head ) {
                    System.out.println("\n*Player 1*"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank()+"\n");
                    pass = true;
                }
                else if (pass!=true) {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Left Neighbor's Trait = 	  -A-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Left Neighbor's Trait = 	  -E-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }			}
                else {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Right Neighbor's Trait = 	  -A-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Right Neighbor's Trait = 	  -E-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                }
                temp = temp.getRight();
            }
            System.out.println();
	}
	//Displays population traits. This includes population traits and private acounts
	public void getPopulation() {
            System.out.println("--------------------------------------\n	    Peek at Population\n");
            Node head = header;
            Node temp = getLeftestNeighbor(head,(players/2));
            boolean pass = false;
            for (int i=0; i<players; i++) {
                if (temp == head ) {
                    System.out.println("\n*Player 1"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank()+"\n");
                    pass = true;
                }
                else if (pass!=true) {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Left Neighbor's Trait = 	  -A-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Left Neighbor's Trait = 	  -E-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                }
                else {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Right Neighbor's Trait = 	  -A-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Right Neighbor's Trait = 	  -E-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                }
                temp = temp.getRight();
            }
	}
	
	public void summTurn() {
            System.out.println("--------------------------------------\n	    Turn Summary\n");
            Node head = header;
            Node temp = getLeftestNeighbor(head,neighborSize);
            boolean pass = false;
            for (int i=0; i<hoodSize; i++) {
                if (temp == head ) {
                    System.out.println("\n*Player 1*"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank()+"\n");
                    pass = true;
                }
                else if (pass!=true) {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Left Neighbor's Trait = 	  -?-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Left Neighbor's Trait = 	  -?-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }			}
                else {
                    if (temp.getBoolean()==altruist) {
                        System.out.println("*Right Neighbor's Trait = 	  -?-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                    else {
                        System.out.println("*Right Neighbor's Trait = 	  -?-"+"\n	Money Earned Last Round = "+temp.getPayoff()+"\n	Private Account = 	  "+temp.getBank());
                    }
                }
                temp = temp.getRight();
            }
            System.out.println();

	}
	
	public Node getNode(int x) {
            Node temp = header;
            while (temp.getID()==x) {
                temp = temp.getRight();
            }
            return temp;
	}
	
	public void printLine() {
            System.out.println("\n----------------Print Line---------------------");
            Node temp = getLeftestNeighbor(header,players);
            for (int i=0; i<players; i++) {
                if (temp.getID()==1) {
                    System.out.print("H   ");
                }
                else if (temp.getBoolean()==altruist) {
                    System.out.print("A   ");
                }
                else {
                    System.out.print("E   ");
                }
                temp = temp.getRight();
            }
            System.out.println();		
	}
	
	public void printCircle() {
            Node temp = header;
            if (players%2!=0) {
                int topNum = (int) Math.ceil(players / 2);
                Node topNode = getNode(topNum);
                Node topNode2 = topNode.getLeft();
                String indent = "";
                for (int i=0; i<players-3; i++) {
                    if (topNode2.getRight()==temp && topNode.getLeft()==temp) {
                        System.out.println("				"+temp.getBoolean());
                    }
                    System.out.println("				"+topNode2.getBoolean()+indent+"   "+indent+topNode.getBoolean()+"				");
                    indent = indent+"   ";
                    topNode = topNode.getLeft();
                    topNode2 = topNode.getRight();
                }
            }
	}
	
	public void contribute() {
            for (int i=0; i<1; i++) {
                Node temp = header;
                scan = new Scanner(System.in);
                float contribution;
            do{
                System.out.println("Enter contribution:");
                while (!scan.hasNextFloat()){
                    System.out.println("Error : Input has to be a FLOAT\n Enter contribution:");
                    scan.next();
                }
                contribution = scan.nextFloat();
            }while (contribution < 0 || contribution > 10);
            analyzeH((int) contribution);
                for (int j=1; j<=players; j++) {
                    calcPayoff(temp, contribution);
                    temp = temp.getRight();
                }
                temp = header;
                for (int l=1; l<=players; l++) {
                    imitDynamics(temp,hoodSize);
                    temp = temp.getRight();
                }
                setTraits();
                index = 0;
            }
	}
	
	public float checkWinner(int players) {
            Node temp = header;
            float max = temp.getBank();
            for (int i=0; i<players; i++) {
                if (temp.getBank()>max) {
                    max = temp.getBank();
                }
                temp = temp.getRight();
            }
            return max;
	}
	
	public void setTraits() {
            Node temp = header;
            for (int i=0; i<players; i++) {
                temp.setBoolean(traits[i]);
                temp = temp.getRight();
            }
	}
	
	public boolean randomBoolean() {
            random = new Random();
            return random.nextBoolean();
	}
	
	public int getPlayers() {
            return players;
	}
	
	public int getHoodSize() {
            return hoodSize;
	}
	
	public Node getHeader() {
            return header;
	}
	
	public static void main(String[] args) {
            ContributionGame beta = new ContributionGame(4,3);
            beta.createPopulation();
            beta.contribute();
	}
}
