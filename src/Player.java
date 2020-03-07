import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9080621067547329111L;
	private transient DataInputStream fromPlayer;
	private transient DataOutputStream toPlayer;
	private transient ObjectInputStream objFromPlayer;
	private transient ObjectOutputStream objToPlayer;
	private transient Socket playerSocket;
	
	
	private int money;
	private ArrayList<Card> propertyCards;
	private ArrayList<Card> currencyCards;
	private String name;
	private int count;
	
	private boolean myTurn;
	private boolean bidding;
	
	
	public Player(String name) {
		this.name = name;
		myTurn = false;
		bidding = true;
	}
	
	public Player(int count) {
		this.count = count;
	}
	
	
	// getter/setter methods
	public DataInputStream getFromPlayer() {
		return fromPlayer;
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
	
	
	
}
