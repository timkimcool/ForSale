package com.forsale;
import java.util.*;

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
	
	public void makeHouseDeck() {
		for (int i = 1; i < 31; i++) {
			deck.add(new Card(cardName.concat(i + ".png"), i, true));
		}
		
		if (playerCount == 3) {
			removeCard(6);
		} else if (playerCount == 4) {
			removeCard(2);
		}
	}
	
	public void makeCurrencyDeck() {
		for (int i = 0; i < 16; i++) {
			if (i == 1) {
				continue;
			}
			deck.add(new Card(cardName.concat(".png"), i, true));
			deck.add(new Card(cardName.concat(".png"), i, true));
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
	
}
