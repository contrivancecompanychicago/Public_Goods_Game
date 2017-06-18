/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package public_goods_game;

/**
 *
 * @author Rodrigo
 */
public class Node{
	private Node left;
	private Node right;
	private boolean altruist;
	private float initial;
	private float bank;
	private float payoff;
	private final int ID;
	
	public Node (int ID, boolean altruist, float bank, float payoff) {
		this.ID = ID;
		this.altruist = altruist;
		this.bank = bank;
		this.payoff = payoff;
	}
	
	public float getPayoff() {
		return payoff;
	}
	
	public void setPayoff(float x) {
		payoff = x;
	}
	
	public float getBank() {
		return bank;
	}
	
	public void setBank(float x) {
		bank = x;
	}
	
	public int getID(){
		return ID;
	}
	
	public boolean getBoolean(){
		return altruist;
	}
	
	public float getInitial(){
		return initial;
	}
	
	public void setInitial(float x) {
		initial = x;
	}
	
	public void setBoolean(boolean x) {
		altruist = x;
	}
	
	public Node getLeft(){
		return left;
	}
	
	public Node getRight(){
		return right;
	}
	
	public void setRight(Node x){
		right = x;
	}
	
	public void setLeft(Node x){
		left = x;
	}
}
