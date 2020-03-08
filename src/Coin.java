import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Coin implements ForSaleConstants{
	private ImageView coinImageView;
	private boolean selected = false;
	private LabelPane yourBidPane;
	static Image coinImage;

	public Coin(LabelPane yourBidPane, Image image) {
		coinImage = image;
		coinImageView = new ImageView(coinImage);
		coinImageView.setPreserveRatio(true);
		coinImageView.setFitWidth(COIN_WIDTH);
		this.yourBidPane = yourBidPane;
	}
	
	public Coin(LabelPane yourBidPane, Image image, boolean clickable) {
		this(yourBidPane, image);
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
				selected = true;
				yourBidPane.setBid(yourBidPane.getBid() + 1);
				String bidText = "Your current bid: $" + yourBidPane.getBid() + " ";
				String labelText = yourBidPane.getLabel().getText();
				yourBidPane.getLabel().setText(bidText + labelText.substring(labelText.indexOf('|')));
			} else { 
				coinImageView.setEffect(null); 
				selected = false;
				yourBidPane.setBid(yourBidPane.getBid() - 1);
				String bidText = "Your current bid: $" + yourBidPane.getBid() + " ";
				String labelText = yourBidPane.getLabel().getText();
				yourBidPane.getLabel().setText(bidText + labelText.substring(labelText.indexOf('|')));
			}
		});
	}

	public boolean isSelected() {
		return selected;
	}
	
	/*
	public static void main(String[] args) {
		System.out.println(RESOURCE_PATH.concat(name));
	}
	*/
}
