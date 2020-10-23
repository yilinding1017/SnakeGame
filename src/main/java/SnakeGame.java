import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;


public class SnakeGame extends Application {
    Scene gameScene;
    Group gameRoot;
    static int level = 1;
    int fruitNum = 0;
    int score = 0;
    Tile[][] gameBoard;
    static ImageView[][] fruitTrack;
    Group tileGroup;

    static ArrayList<Tile> snake;
    static Group snakeGroup;
    int direction; // 0 not set, 1 right, 2 left
    int currDir;

    Text lvlText = new Text(1070, 88, null);
    Text fruitText = new Text(1070, 158, null);
    Text scoreText = new Text(1070, 228, null);
    Text timeText = new Text(1070, 298, null);
    int timeCount;
    int countDown = 25;
    boolean eaten = false;


    Timeline timeline = new Timeline();

    public void setGameScene() {
        gameBoard = new Tile[30][30];
        fruitTrack = new ImageView[30][30];
        tileGroup = new Group();
        snake = new ArrayList<Tile>();
        snakeGroup = new Group();
        direction = 0;
        currDir = 0;
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

        // Add fruits
        for(int i = 0; i < level; ++i) {
            tileGroup.getChildren().add(gameBoard[0+i][0+i].addFruit());
            tileGroup.getChildren().add(gameBoard[29-i][0+i].addFruit());
            tileGroup.getChildren().add(gameBoard[0+i][29-i].addFruit());
            tileGroup.getChildren().add(gameBoard[29-i][29-i].addFruit());
            tileGroup.getChildren().add(gameBoard[15-i][29-i].addFruit());
        }
        gameRoot.getChildren().add(tileGroup);
        // Add snake
        gameBoard[14][14].initSnake();
        gameBoard[15][14].initSnake();
        gameRoot.getChildren().add(snakeGroup);

        // Add Text
        Group textGroup = new Group();
        // level
        lvlText.setText("Level  " + level);
        lvlText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,40));
        lvlText.setFill(Color.WHITE);
        // fruits
        fruitText.setText("Fruits:  " + fruitNum);
        fruitText.setFont(Font.font("Ariel",FontWeight.EXTRA_BOLD, FontPosture.ITALIC,35));
        fruitText.setFill(Color.WHITE);
        // score
        scoreText.setText("Score:  " + score);
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
        String eatSound = getClass().getClassLoader().getResource("click.mp3").toString();
        AudioClip eatClip = new AudioClip(eatSound);

        String crushSound = getClass().getClassLoader().getResource("jab.mp3").toString();
        AudioClip crushClip = new AudioClip(crushSound);
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
                new KeyFrame(Duration.millis(40), event -> {
                    Tile head = snake.get(snake.size()-1);
                    Tile tail = snake.get(0);
                    try {
                        // if the body is full, change currDir based on direction
                        if(head.snakeBody.getWidth() == head.tileSize && head.snakeBody.getHeight() == head.tileSize) {
                            // Fruit update
                            if(head.isFruit == true) head.isFruit = false;
                            eaten = false;
                            // Remove the tail if it is already empty
                            if(tail.snakeBody.getWidth() == 0 || tail.snakeBody.getHeight() == 0) {
                                snake.get(0).snakeBody.setHeight(0);
                                snake.get(0).snakeBody.setWidth(0);
                                snake.get(0).snakeBody.setX(snake.get(0).xPos);
                                snake.get(0).snakeBody.setY(snake.get(0).yPos);
                                snake.get(0).isSnake = false;
                                snake.remove(0);
                            }
                            // Determine what is the next tile to go, which throws exception if the wall is hit
                            Tile nextHead = head;
                            if(direction == 0) {
                                // not set
                                if(currDir == 0) {
                                    nextHead = gameBoard[1+head.x][head.y];
                                } else if(currDir == 1) {
                                    nextHead = gameBoard[head.x][1+head.y];
                                } else if(currDir == 2) {
                                    nextHead = gameBoard[head.x-1][head.y];
                                } else if(currDir == 3) {
                                    nextHead = gameBoard[head.x][head.y-1];
                                }
                                // currDir does not change
                            } else if(direction == 1) {
                                // turn right
                                if(currDir == 0) {
                                    nextHead = gameBoard[head.x][head.y+1];
                                } else if(currDir == 1) {
                                    nextHead = gameBoard[head.x-1][head.y];
                                } else if(currDir == 2) {
                                    nextHead = gameBoard[head.x][head.y-1];
                                } else if(currDir == 3) {
                                    nextHead = gameBoard[head.x+1][head.y];
                                }
                                if(currDir == 3) currDir = 0;
                                else currDir += 1;

                            } else if(direction == 2) {
                                // turn left
                                if(currDir == 0) {
                                    nextHead = gameBoard[head.x][head.y-1];
                                } else if(currDir == 1) {
                                    nextHead = gameBoard[head.x+1][head.y];
                                } else if(currDir == 2) {
                                    nextHead = gameBoard[head.x][head.y+1];
                                } else if(currDir == 3) {
                                    nextHead = gameBoard[head.x-1][head.y];
                                }
                                if(currDir == 0) currDir = 3;
                                else currDir -= 1;
                            }
                            direction = 0;
                            // Check if it hit itself
                            if(snake.contains(nextHead)) {
                                Exception e = new Exception();
                                throw e;
                            }
                            // if nothing is in front, we just go ahead
                            snake.add(nextHead);
                        }
                        //  Update head and tail)
                        head = snake.get(snake.size()-1);
                        tail = snake.get(0);
                        // increase the head
                        head.addSnake(currDir);
                        // Check if there is a fruit
                        if(head.isFruit) {
                            if(eaten == false) {
                                // remove the fruit
                                eatClip.play();
                                tileGroup.getChildren().remove(fruitTrack[head.x][head.y]);
                                // Add fruit number and score
                                fruitNum++;
                                fruitText.setText("Fruit:  " + fruitNum);
                                score+=level*10;
                                scoreText.setText("Score:  " + score);

                                // randomly spawn a fruit
                                Random rand = new Random();
                                int newX = rand.nextInt(30);
                                int newY = rand.nextInt(30);
                                while(snake.contains(gameBoard[newX][newY]) || gameBoard[newX][newY].isFruit == true) {
                                    newX = rand.nextInt(30);
                                    newY = rand.nextInt(30);
                                }
                                tileGroup.getChildren().add(gameBoard[newX][newY].addFruit());
                            }
                            eaten = true;
                        } else {
                            // cut the tail
                            if(snake.get(1).x == tail.x) {
                                tail.reduceSnake(2+tail.y-snake.get(1).y);
                            } else if(snake.get(1).y == tail.y) {
                                tail.reduceSnake(1+tail.x-snake.get(1).x);
                            }
                        }

                    } catch (Exception e) {
                        timeline.pause();
                        crushClip.play();
                        primaryStage.setScene(terScene);
                    }
                }),
                new KeyFrame(Duration.millis(40), event -> {
                    --countDown;
                    if(countDown == 0) {
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
                        countDown = 25;
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
                } else if(event.getCode() == KeyCode.RIGHT) {
                    if(direction == 0) direction = 1;
                } else if(event.getCode() == KeyCode.LEFT) {
                    if(direction == 0) direction = 2;
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
