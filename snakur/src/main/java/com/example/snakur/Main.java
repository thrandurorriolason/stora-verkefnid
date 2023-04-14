package com.example.snakur;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/******************************************************************************
 *  Nafn    : Þrándur Orri Ólason
 *  T-póstur: too4@hi.is
 *
 *  Lýsing  : Stóra verkefnið. Strætó-Snákur er endurgerð af sívinsæla leiknum
 *            snákur, settur í strætó búning. Snákurinn er núna strætisvagn sem
 *            pikkar upp farþega og fær spilarinn stig fyrir það.
 *
 *
 *****************************************************************************/


public class Main extends Application{

    static int speed = 5;
    static int foodcolor = 0;
    static int width = 18;
    static int height = 18;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();
    static List<Integer> scores = new ArrayList<>();


    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }



    public void start(Stage primaryStage){
        try {
            newFood();

            GridPane root = new GridPane();

            Canvas t = new Canvas(450, 125);
            GraphicsContext gt = t.getGraphicsContext2D();
            gt.setFill(Color.rgb(255, 199, 0));
            gt.fillRect(0, 0, t.getWidth(), t.getHeight());

            Canvas d = new Canvas(430, 100);
            GraphicsContext gd = d.getGraphicsContext2D();
            gd.setFill(Color.rgb(22,22,22));
            gd.fillRoundRect(0, 0, d.getWidth(), d.getHeight(), 30, 30);

            Canvas c = new Canvas(width * cornersize, height * cornersize);
            GraphicsContext gc = c.getGraphicsContext2D();

            Canvas n = new Canvas(450, 175);
            GraphicsContext gn = n.getGraphicsContext2D();
            gn.setFill(Color.rgb(255, 199, 0));
            gn.fillRect(0, 0, n.getWidth(), n.getHeight());

            Canvas v = new Canvas(140, 70);
            GraphicsContext gv = v.getGraphicsContext2D();
            gv.setFill(Color.web("EBEBEB"));
            gv.fillRoundRect(0, 0, v.getWidth(), v.getHeight(), 20, 20);

            v.setTranslateX((v.getWidth()/2)-(n.getWidth()/2));
            v.setTranslateY(30);

            Canvas h = new Canvas(140, 70);
            GraphicsContext gh = h.getGraphicsContext2D();
            gh.setFill(Color.web("EBEBEB"));
            gh.fillRoundRect(0, 0, h.getWidth(), h.getHeight(), 20, 20);

            h.setTranslateX((n.getWidth()/2)-(h.getWidth()/2));
            h.setTranslateY(30);



            Canvas l1 = new Canvas(55, 55);
            GraphicsContext gl1 = l1.getGraphicsContext2D();
            gl1.setFill(Color.web("FFFFFE"));
            gl1.fillOval(0, 0, l1.getWidth(), l1.getHeight());

            l1.setTranslateX(((v.getWidth()/2)-(n.getWidth()/2))-30);
            l1.setTranslateY(30);

            Canvas l2 = new Canvas(55, 55);
            GraphicsContext gl2 = l2.getGraphicsContext2D();
            gl2.setFill(Color.web("FFFB99"));
            gl2.fillOval(0, 0, l2.getWidth(), l2.getHeight());

            l2.setTranslateX(((v.getWidth()/2)-(n.getWidth()/2))+30);
            l2.setTranslateY(30);

            Canvas l3 = new Canvas(55, 55);
            GraphicsContext gl3 = l3.getGraphicsContext2D();
            gl3.setFill(Color.web("FFFB99"));
            gl3.fillOval(0, 0, l3.getWidth(), l3.getHeight());

            l3.setTranslateX(((n.getWidth()/2)-(h.getWidth()/2))-30);
            l3.setTranslateY(30);

            Canvas l4 = new Canvas(55, 55);
            GraphicsContext gl4 = l4.getGraphicsContext2D();
            gl4.setFill(Color.web("FFFFFE"));
            gl4.fillOval(0, 0, l4.getWidth(), l4.getHeight());

            l4.setTranslateX(((n.getWidth()/2)-(h.getWidth()/2))+30);
            l4.setTranslateY(30);



            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(t, d);

            StackPane stackPane1 = new StackPane();
            stackPane1.getChildren().addAll(n, v, h, l1, l2, l3, l4);


            root.add(stackPane, 0, 0);
            root.add(c, 0, 1);
            root.add(stackPane1, 0, 2);



            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc, gt, gn, gd);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc, gt, gn, gd);
                    }
                }

            }.start();



            Scene scene = new Scene(root, 450, 750);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.UP) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.LEFT) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.DOWN) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.RIGHT) {
                    direction = Dir.right;
                }
                if (key.getCode() == KeyCode.SPACE){
                    speed = 5;
                    snake.removeAll(snake);
                    snake.add(new Corner(9, 7));
                    snake.add(new Corner(9, 8));
                    gameOver = false;
                    newFood();
                }
                tick(gc, gt, gn, gd);
            });


            snake.add(new Corner(9, 7));
            snake.add(new Corner(9, 8));

            primaryStage.setScene(scene);
            primaryStage.setTitle("Strætó Snákur");
            primaryStage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }




    public static void tick(GraphicsContext gc, GraphicsContext gt, GraphicsContext gn, GraphicsContext gd) {

        scores.add(speed - 6);

        if (gameOver) {
            gc.setFill(Color.rgb(30, 30, 30, 0.25));
            gc.fillRect(0, 0, 450, 450);
            gc.setFill(Color.web("D5FF7C"));
            gc.setFont(new Font("System", 50));
            gc.fillText("Leik Lokið", 115, 210);
            gc.fillText("● "+(speed - 6), 100, 280);
            gc.fillText("☆ "+ Collections.max(scores), 230, 280);

            return;
        }


        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }


        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y >= height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x >= width) {
                    gameOver = true;
                }
                break;

        }


        if(snake.get(0).x < 0 || snake.get(0).x >= width * cornersize
                || snake.get(0).y < 0 || snake.get(0).y >= height * cornersize) {
            gameOver = true;
        }


        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }



        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        gd.setFill(Color.rgb(22, 22, 22));
        gd.fillRect(20, 0, 160, 100);

        gd.setFill(Color.rgb(255, 136, 50));
        gd.setFont(new Font("System", 64));
        gd.fillText(String.valueOf(speed - 6), 47, 73);
        gd.fillText("Stiganes", 130, 73);

        gn.setFill(Color.rgb(22, 22, 22));
        gn.setFont(new Font("System", 26));
        gn.fillText("Ýttu á *bil* til að spila aftur!", 80, 45);



        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0:
                cc = Color.rgb(255, 175, 175);
                break;
            case 1:
                cc = Color.rgb(255, 175, 255);
                break;
            case 2:
                cc = Color.rgb(175, 255, 255);
                break;
            case 3:
                cc = Color.rgb(175, 255, 175);
                break;
            case 4:
                cc = Color.rgb(255, 255, 175);
                break;
        }

        gc.setFill(cc);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);


        for (Corner c : snake) {
            gc.setFill(Color.rgb(255, 199, 0));
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            //gc.setFill(Color.BLUE);
            //gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);

        }
    }


    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;

        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
