package com.forsale;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Card extends ImageView implements ForSaleConstants, Serializable{
	private static final long serialVersionUID = 1754864923431884435L;
	private transient ImageView cardImageView;
	private transient Image cardImage;
	private int value;
	private boolean selected = false;
	private transient StackPane stackPane;
	private transient Label label;
	private transient ArrayList<Card> deck;

	public Card (String name, int value, boolean reset) {
		cardImage = new Image(this.getClass().getResourceAsStream(RESOURCE_PATH.concat(name)));
		cardImageView = new ImageView(cardImage);
		this.value = value;
		if (reset) { resetImage(); }
	}
	
	public boolean isSelected() {
		return selected;
	}


	public ImageView getImageView() {
		return cardImageView;
	}
	
	public Image getCardImage() {
		return cardImage;
	}
	
	public int getValue() {
		return value;
	}
	
	public void addDeck(ArrayList<Card> deck) {
		this.deck = deck;		
	}

	private void writeObject(ObjectOutputStream outStream) throws IOException {
		outStream.defaultWriteObject();
		ImageIO.write(SwingFXUtils.fromFXImage(cardImage, null), "png", outStream);
	}
	
	private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
		inStream.defaultReadObject();
		cardImage = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);
		cardImageView = new ImageView(cardImage);
		resetImage();
	}
	
	public void resetImage() {
		Rectangle clip = new Rectangle();
		cardImageView.setFitWidth(CARD_WIDTH);
		cardImageView.setFitHeight(CARD_HEIGHT);
		cardImageView.setPreserveRatio(true);
		clip.heightProperty().bind(cardImageView.fitHeightProperty());
		clip.widthProperty().bind(cardImageView.fitWidthProperty());
		clip.arcHeightProperty().bind(cardImageView.fitHeightProperty().divide(37));
		clip.arcWidthProperty().bind(cardImageView.fitWidthProperty().divide(25));
		clip.setStroke(Color.WHITE);
		cardImageView.setClip(clip);
	}
	
	public void imageViewBind(ObservableValue<? extends Number> height) {
		cardImageView.fitHeightProperty().bind(height);
	}
	
	public void enableClick() {
		cardImageView.setOnMouseClicked(e -> {
			if (cardImageView.getEffect() == null) {
				for (Card c : deck) {
					c.unclick();
				}
				// cardImageView.setEffect(new SepiaTone());
				cardImageView.setEffect(new InnerShadow(15, Color.BLUE));
				selected = true;
			} else { 
				unclick();
			}
		});
	}
	
	public void unclick() {
		cardImageView.setEffect(null); 
		selected = false;
	}
	
	public void disableClick() {
		cardImageView.setOnMouseClicked(e -> {});
	}
	
	public void currencyBox() {
		stackPane = new StackPane();
		label = new Label();
		label.setText("$" + value);
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
		label.setBackground(new Background(new BackgroundFill(Color.rgb(234, 230, 220, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
		// text.setFill(Color.ALICEBLUE);
		stackPane.setAlignment(Pos.CENTER);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public StackPane getStackPane() {
		return stackPane;
	}
	
	// circle.centerXProperty().bind(pane.maxWidthProperty().divide(2));
	//cardImage1.setEffect(new DropShadow(20, Color.BLACK));
}
