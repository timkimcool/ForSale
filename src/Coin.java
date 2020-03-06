import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Coin implements ForSaleConstants{
	private ImageView coinImageView;
	private static String name = "dollar.png";
	
	
	public Coin() {
		Image coinImage = new Image(RESOURCE_PATH.concat(name));
		coinImageView = new ImageView(coinImage);
		coinImageView.setPreserveRatio(true);
		coinImageView.setFitWidth(COIN_WIDTH);
		this.enableClick();

	}
	
	public ImageView getImageView() {
		return coinImageView;
	}
	
	public void disableClick() {
		coinImageView.setOnMouseClicked(e -> {});
	}
	
	public void enableClick() {
		coinImageView.setOnMouseClicked(e -> {
			if (coinImageView.getEffect() == null) {
				coinImageView.setEffect(new DropShadow(10, Color.RED));
			} else { coinImageView.setEffect(null); }
		});
	}
	
	/*
	public static void main(String[] args) {
		System.out.println(RESOURCE_PATH.concat(name));
	}
	*/
}
