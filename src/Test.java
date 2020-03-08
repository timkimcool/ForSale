import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Test implements ForSaleConstants {
	public static void main (String[] arg) throws IOException {
		Image cardImage;
		ImageView cardImageView;
		File f = new File("resources/house1.png");
		System.out.println(RESOURCE_PATH.concat("house1.png"));
		System.out.println(f);
		cardImage = new Image("file:resources/house1.png");
		cardImageView = new ImageView(cardImage);
	}
}
