import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    int x;
    int y;
    int tileSize;
    int xPos;
    int yPos;
    boolean isFruit;
    boolean isSnake;
    boolean col;
    Rectangle snakeBody;

    Tile(int xCoor, int yCoor, int size, boolean colour, int fromX, int fromY) {
        x = xCoor;
        y = yCoor;
        xPos = xCoor * size + fromX;
        yPos = yCoor * size + fromY;
        tileSize = size;
        isFruit = false;
        isSnake = false;
        col = colour;
        setWidth(size);
        setHeight(size);
        relocate(xCoor * size + fromX, yCoor * size + fromY);

        //#afeeee
        //#b1f3f2
        //#b1e9e4
        setFill(colour ? Color.valueOf("#e0ffff") : Color.valueOf("#c3eaea"));

        snakeBody = new Rectangle(xPos,yPos,0,0);
        snakeBody.setFill(Color.valueOf("#f69100"));
        SnakeGame.snakeGroup.getChildren().add(snakeBody);
    }

    public ImageView addFruit() {
        isFruit = true;
        ImageView imageView = new ImageView("apple.png");
        imageView.setFitWidth(26);
        imageView.setFitHeight(26);
        imageView.setX(xPos);
        imageView.setY(yPos);
        // track the fruit
        SnakeGame.fruitTrack[x][y] = imageView;

        return imageView;
    }

    public void initSnake() {
        isSnake = true;
        snakeBody.setWidth(tileSize);
        snakeBody.setHeight(tileSize);
        SnakeGame.snake.add(this);
    }

    public void addSnake(int dir) {
        double speed = (double)tileSize/Math.pow(2,(4-SnakeGame.level));
        if(dir == 0) {
            // right
            snakeBody.setWidth(snakeBody.getWidth()+speed);
            snakeBody.setHeight(tileSize);
        } else if (dir == 1) {
            // down
            snakeBody.setWidth(tileSize);
            snakeBody.setHeight(snakeBody.getHeight()+speed);
        } else if (dir == 2) {
            // left
            snakeBody.setWidth(snakeBody.getWidth()+speed);
            snakeBody.setHeight(tileSize);
            snakeBody.setX(xPos+tileSize-snakeBody.getWidth());
        } else if (dir == 3) {
            // up
            snakeBody.setWidth(tileSize);
            snakeBody.setHeight(snakeBody.getHeight()+speed);
            snakeBody.setY(yPos+tileSize-snakeBody.getHeight());
        }
    }

    public void reduceSnake(int dir) {
        double speed = (double)tileSize/Math.pow(2,(4-SnakeGame.level));
        if(dir == 0) {
            // right
            snakeBody.setWidth(snakeBody.getWidth()-speed);
            snakeBody.setHeight(tileSize);
            snakeBody.setX(xPos+tileSize-snakeBody.getWidth());
        } else if (dir == 1) {
            // down
            snakeBody.setWidth(tileSize);
            snakeBody.setHeight(snakeBody.getHeight()-speed);
            snakeBody.setY(yPos+tileSize-snakeBody.getHeight());
        } else if (dir == 2) {
            // left
            snakeBody.setWidth(snakeBody.getWidth()-speed);
            snakeBody.setHeight(tileSize);
        } else if (dir == 3) {
            snakeBody.setWidth(tileSize);
            snakeBody.setHeight(snakeBody.getHeight()-speed);
        }
    }


}
