import java.io.*;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Card extends ImageView implements ForSaleConstants, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1754864923431884435L;
	private transient ImageView cardImageView;
	private transient Image cardImage;
	
	public Card(String name) {
		cardImage = new Image(RESOURCE_PATH.concat(name));
		cardImageView = new ImageView(cardImage);

		resetImage();
	}
	
	
	public ImageView getImageView() {
		return cardImageView;
	}
	
	public Image getCardImage() {
		return cardImage;
	}
	
	private void writeObject(ObjectOutputStream outStream) throws IOException {
		outStream.defaultWriteObject();
		System.out.println(cardImage);
		ImageIO.write(SwingFXUtils.fromFXImage(cardImage, null), "png", outStream);
	}
	
	private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
		inStream.defaultReadObject();
		cardImage = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);
		cardImageView = new ImageView(cardImage);
		resetImage();
	}
	
	public void resetImage() {
		cardImageView.setFitWidth(CARD_WIDTH);
		cardImageView.setFitHeight(CARD_HEIGHT);
		Rectangle clip = new Rectangle();
		clip.setWidth(CARD_WIDTH);
		clip.setHeight(CARD_HEIGHT);
		clip.setArcHeight(30);
		clip.setArcWidth(30);
		clip.setStroke(Color.WHITE);
		cardImageView.setClip(clip);
	}
	
	// circle.centerXProperty().bind(pane.maxWidthProperty().divide(2));
	//cardImage1.setEffect(new DropShadow(20, Color.BLACK));
}
