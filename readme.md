This is a implementation of the Snake Game with added bonus features like level, time, sounds and etc (using JavaFx as the main design tool)
<br>

*Yilin Ding*<br>*openjdk version "11.0.8" 2020-07-14*<br>*macOS 10.15.6 (MacBook Pro 2019)*<br>

 **Game Description:**<br>

![](/START.png)

This Snake game starts with a splash screen showing the instructions on how to play the game (as well as author name, i.e. me). By following the instructions on this splash screen (i.e. press return/enter key), the game will begin.

```embeddedjs
LEFT: turn the snake left
RIGHT: turn the snake right
P: pause and un-pause the game
R: reset to the splash screen
1: start level 1
2: start level 2
3: start level 3
Q: quit and display the high score screen
```

 ![GAME](/GAME.png)

In the game, the snake is always in motion and it moves forward until Left/Right arrow key is pressed (unlike some version of snake game where all the arrow keys are used, this game is designed to only use left :arrow_left: and right :arrow_right: to turn). The snake will die if it eat itself or it hits the wall (the edge of the screen). The objective of the snake is to eat more fruits and get more scores. When the snake eats a piece of the fruit disappears and is immediately replaced by another piece of fruit randomly positioned. Every time the snake eats a piece of fruit, it gets one block longer. Also, there are three levels for this game, and each level starts with different number of fruits (not random, level 1 with 5 fruits, level 2 with 10 fruits and level 3 with 15 fruits). The speed of the snake increases when the level increases and the snakes begins at level 1. The player will play in the first two levels (level 1 and level 2) each for 30 seconds maximum and the level will automatically go up by one. When the player reaches level 3, the clock now count the number of seconds the player stayed in this level (instead of the 30-second count down like in level 1 and 2). The player can also go to corresponding levels by pressing digit 1,2,3. As instructions listed above, the player can press Q to quit, R to restart (return to the splash screen), and P to pause/unpause. When the snakes died, it will go to the final screen informing the player the fruits eaten, score, highest score for now.<br>

![END](/END.png)

**Functional Descriptions:**

- Animation rate of at least 25 frames-per-second (FPS)
- 1280x800 pixels game window
- Sound effects for game events (such as when the snake eats or when the game is over)
- Texture Graphics
- Splash screen shows how to play instruction and student name/id
- Game screen shows fruits eaten, score, time as well as instructions
- Final screen shows total fruits eaten, total score, highest record
- Game plays and progresses as described in the game description

**Technical Descriptions:**

- Only used the Java platform libraries and JavaFX toolkits for the GUI
- Use Gradle to manage builds
