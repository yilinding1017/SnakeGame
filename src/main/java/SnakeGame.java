import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SnakeGame extends Application {
    Scene gameScene;
    Group gameRoot;
    int level = 1;
    int fruitNum = 0;
    int score = 0;
    Tile[][] gameBoard;
    Group tileGroup;
    Text lvlText = new Text(1070, 88, null);
    Text fruitText = new Text(1070, 158, "Fruits:  " + fruitNum);
    Text scoreText = new Text(1070, 228, "Score:  " + score);
    Text timeText = new Text(1070, 298, null);
    int timeCount;

    Timeline timeline = new Timeline();

    public void setGameScene() {
        gameBoard = new Tile[30][30];
        tileGroup = new Group();
        // Add background 800 x 800
        Rectangle background = new Rectangle(240,0,800,800);
        background.setFill(Color.valueOf("#45a5b5"));
        gameRoot.getChildren().add(background);
        // Add game board
        // 780 x 780
        int startX = 250;
        int startY = 10;
        int height = 30;
        int width = 30;
        int tileSize = 26;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile(x, y,tileSize,(x + y) % 2 == 0,startX,startY);
                gameBoard[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }
        gameRoot.getChildren().add(tileGroup);


        // Add fruits
        for(int i = 0; i < level; ++i) {
            gameRoot.getChildren().add(gameBoard[0+i][0+i].addFruit());
            gameRoot.getChildren().add(gameBoard[29-i][0+i].addFruit());
            gameRoot.getChildren().add(gameBoard[0+i][29-i].addFruit());
            gameRoot.getChildren().add(gameBoard[29-i][29-i].addFruit());
            gameRoot.getChildren().add(gameBoard[15+i][29-i].addFruit());
        }
        // Add Text
        Group textGroup = new Group();
        // level
        lvlText.setText("Level  " + level);
        lvlText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,40));
        lvlText.setFill(Color.WHITE);
        // fruits
        fruitText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,35));
        fruitText.setFill(Color.WHITE);
        // score
        scoreText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,35));
        scoreText.setFill(Color.WHITE);

        // time
        if (level == 3) {
            timeText.setText("Time:  0");
            timeCount = 0;
        } else {
            timeText.setText("Time:  30");
            timeCount = 10;
        }
        timeText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,35));
        timeText.setFill(Color.WHITE);
        textGroup.getChildren().addAll(lvlText,fruitText,scoreText,timeText);
        gameRoot.getChildren().add(textGroup);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // intro scene
        Image introImage = new Image("intro.png", 1300, 805, true, true);
        ImageView introImageView = new ImageView(introImage);
        Group introRoot = new Group();
        introRoot.getChildren().add(introImageView);
        Scene introScene = new Scene(introRoot,1280,800);

        // terminal scene
        Image terImage = new Image("terminal.png", 1300, 805, true, true);
        ImageView terImageView = new ImageView(terImage);
        Group terRoot = new Group();
        terRoot.getChildren().add(terImageView);
        Scene terScene = new Scene(terRoot,1280,800);

        // game scene
        gameRoot = new Group();
        setGameScene();
        gameScene = new Scene(gameRoot,1280,800, Color.valueOf("#4595a5"));

        // clock
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(1), event -> {
                    if(level == 3) {
                        ++timeCount;
                        timeText.setText("Time:  " + timeCount);
                    } else {
                        if(timeCount == 1) {
                            ++level;
                            setGameScene();
                        } else {
                            --timeCount;
                            timeText.setText("Time:  " + timeCount);
                        }
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);

        // Set Key events
        introScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    primaryStage.setScene(gameScene);
                    timeline.play();
                }
            }
        });

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.P) {
                    if(timeline.getStatus() == Animation.Status.PAUSED) {
                        timeline.play();
                    } else {
                        timeline.pause();
                    }
                } else if(event.getCode() == KeyCode.R) {
                    timeline.pause();
                    primaryStage.setScene(introScene);
                    level = 1;
                    score = 0;
                    fruitNum = 0;
                    setGameScene();
                } else if(event.getCode() == KeyCode.DIGIT1) {
                    level = 1;
                    setGameScene();
                } else if(event.getCode() == KeyCode.DIGIT2) {
                    level = 2;
                    setGameScene();
                } else if(event.getCode() == KeyCode.DIGIT3) {
                    level = 3;
                    setGameScene();
                } else if(event.getCode() == KeyCode.Q) {
                    timeline.pause();
                    primaryStage.setScene(terScene);
                }
            }
        });

        terScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.R) {
                    primaryStage.setScene(introScene);
                    level = 1;
                    score = 0;
                    fruitNum = 0;
                    setGameScene();
                } else if(event.getCode() == KeyCode.Q) {
                    System.exit(0);
                }
            }
        });

        // start here
        primaryStage.setScene(introScene);
        primaryStage.show();
    }
}
