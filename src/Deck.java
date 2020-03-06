import java.util.*;

import javafx.embed.swing.JFXPanel;

public class Deck {
	private List<Card> deck;
	private String cardName;
	private int playerCount;
	
	static Random random;
	
	
	public Deck(String cardName, int playerCount) {
		deck = new ArrayList<Card>();
		random = new Random();
		this.cardName = cardName;
		this.playerCount = playerCount;
		makeDeck();
	}
	
	public void addCard(Card c) {
		deck.add(c);
	}
	
	public void removeCard(int c) {
		for (int i = 0; i < c; i++)
			removeCard();
	}
	
	public Card removeCard() {
		return deck.remove(Math.abs(random.nextInt() % deck.size()));
	}
	
	public void makeDeck() {
		for (int i = 1; i < 31; i++) {
			deck.add(new Card(cardName.concat(i + ".png")));
		}
		
		if (playerCount == 3) {
			removeCard(6);
		} else if (playerCount == 4) {
			removeCard(2);
		}
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	/*
	public static void main(String[] args) {
		JFXPanel jfxPanel = new JFXPanel();
		Deck houseDeck = new Deck("house", 3);
		System.out.println(houseDeck.getDeck().size());
		houseDeck.removeCard();
		houseDeck.removeCard();
		houseDeck.removeCard();
	}
	*/
	
}
