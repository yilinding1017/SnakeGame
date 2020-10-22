import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    int xPos;
    int yPos;
    boolean isFruit;

    Tile(int xCoor, int yCoor, int size, boolean colour, int fromX, int fromY) {
        xPos = xCoor * size + fromX;
        yPos = yCoor * size + fromY;
        isFruit = false;
        setWidth(size);
        setHeight(size);

        relocate(xCoor * size + fromX, yCoor * size + fromY);

        //#afeeee
        //#b1f3f2
        //#b1e9e4
        setFill(colour ? Color.valueOf("#e0ffff") : Color.valueOf("#c3eaea"));

    }

    ImageView addFruit() {
        isFruit = true;
        ImageView imageView = new ImageView("apple.png");
        imageView.setFitWidth(26);
        imageView.setFitHeight(26);
        imageView.setX(xPos);
        imageView.setY(yPos);

        return imageView;
    }

}
