package com.forsale;
import java.io.*;
import java.net.Socket;

public class Player implements Serializable, Comparable<Player>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1128859479092408925L;

	private transient DataInputStream fromPlayer;
	private transient DataOutputStream toPlayer;
	private transient ObjectInputStream objFromPlayer;
	private transient ObjectOutputStream objToPlayer;
	private transient Socket playerSocket;
	
	
	private int money;
	private int count;
	private boolean myTurn = false;
	private boolean bidding = true;
	private int totalScore;
	private String name;
	private int place;
	private int bid = 0;
	
	
	public Player(String name) {
		this.name = name;
	}
	
	public Player(int count) {
		this.count = count;		
	}
	
	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public DataInputStream getFromPlayer() {
		return fromPlayer;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public int getPlaceValue() {
		return place;
	}

	public void setPlaceValue(int place) {
		this.place = place;
	}

	public void setFromPlayer(DataInputStream fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	
	public DataOutputStream getToPlayer() {
		return toPlayer;
	}
	public void setToPlayer(DataOutputStream toPlayer) {
		this.toPlayer = toPlayer;
	}

	public Socket getPlayerSocket() {
		return playerSocket;
	}
	public void setPlayerSocket(Socket playerSocket) {
		this.playerSocket = playerSocket;
	}
	
	public ObjectInputStream getObjFromPlayer() {
		return objFromPlayer;
	}
	public void setObjFromPlayer(ObjectInputStream objFromPlayer) {
		this.objFromPlayer = objFromPlayer;
	}
	
	public ObjectOutputStream getObjToPlayer() {
		return objToPlayer;
	}
	public void setObjToPlayer(ObjectOutputStream objToPlayer) {
		this.objToPlayer = objToPlayer;
	}

	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getPlayerNumber() {
		return count;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public boolean isBidding() {
		return bidding;
	}

	public void setBidding(boolean bidding) {
		this.bidding = bidding;
	}

	@Override
	public int compareTo(Player p) {
		if (this.totalScore > p.getTotalScore()) {
			return 1;
		}
		if (this.totalScore < p.getTotalScore())  {
			return -1;
		}
		if (this.getMoney() > p.getMoney()) {
			return 1;
		}
		if (this.getMoney() < p.getMoney()) {
			return -1;
		}
		return 0;
	}
	
	
	
}
