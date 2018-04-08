package pckg0;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends Application {
    private int screenSizeX=700;
    private int screenSizeY=screenSizeX;

    private int skipTime=2;
    private int skipTimeCounter=0;
    private int generation=0;
    private int maxGeneration=50;
    private int cellLifeTime=1000;
    private int cellPopulation=40;
    private double foodChance=1.0;

    private Pane root;
    private Pane gameArea;

    private List<GameObject> foods=new ArrayList<>();
    private List<GameObject> cellLife =new ArrayList<>();

    private Parent createContent() {

        root = new Pane();
        root.setPrefSize(screenSizeX, screenSizeY);
        root.setStyle("-fx-background-color: white; ");

        gameArea = new Pane();
        gameArea.setPrefSize(screenSizeX, screenSizeY);
        gameArea.setStyle("-fx-background-color: whitesmoke"); //-fx-border-color:lightgray;

        // Adding default cellLife.
        while(cellLife.size()<cellPopulation) {
                int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 20) * 20;
                int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 20) * 20;
                addCellLife(new CellLife(), x, y);
        }
        System.out.println(cellLife.size());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (skipTimeCounter == 0) {
                    onUpdate();
                }
                if (skipTimeCounter == skipTime) {
                    skipTimeCounter = -1;
                }
                skipTimeCounter++;

                if(cellLife.size()<=8){
                    foodChance=1.0;
                    for(GameObject food:foods){
                        food.setAlive(false);
                        gameArea.getChildren().removeAll(food.getView());
                    }
                    foods.removeIf(GameObject::isDead);
                    generation++;
                    if(cellLife.size()!=0){
                        System.out.println("********* generation: "+generation+" cell population survived: "+cellLife.size()+ " **********");
                        int k=cellLife.size();
                        while(cellLife.size()<=30){
                            for(int i=0;i<k;i++){
                                int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 20) * 20;
                                int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 20) * 20;
                                addCellLife(new CellLife(), x, y);
                                for(int j=0;j<64;j++) {
                                    cellLife.get(i + k).getCellLifeCommand()[j] = cellLife.get(i).getCellLifeCommand()[j];
                                }
                            }
                        }
                    }

                    while(cellLife.size()!=cellPopulation){
                        int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 20) * 20;
                        int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 20) * 20;
                        addCellLife(new CellLife(), x, y);
                    }

                    /*for(int i=0;i<cellLife.size();i++){
                        cellLife.get(i).setLifeTime(90);
                        for(int j=0;j<64;j++){
                            System.out.print(" "+cellLife.get(i).getCellLifeCommand()[j]);
                        }
                        System.out.println(" ");
                    }*/
                }

                if(generation==maxGeneration) {
                    stop();
                }

            }
        };
        timer.start();

        root.getChildren().addAll(gameArea); //uiArea,
        return root;
    }
    private void addFood(GameObject food,double x,double y){
        foods.add(food);
        addGameObject(food,x,y);
    }
    private void addCellLife(GameObject cell, double x, double y){
        int[] cellCommand=new int[64];
        for(int i=0;i<64;i++){
            cellCommand[i]=((int)(Math.random()*63))%63;
        }
        cell.setCellLifeCommand(cellCommand);
        cellLife.add(cell);
        addGameObject(cell,x,y);
    }
    private void addGameObject(GameObject object, double x, double y){
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        gameArea.getChildren().add(object.getView());
    }

    private void onUpdate(){

        for(int i=0;i<cellLife.size();i++){
            int commandIndex=cellLife.get(i).getCommandIndex();
            int command=cellLife.get(i).getCellLifeCommand()[commandIndex];
            if((command>=0)&&(command<8)){
                cellLife.get(i).look(command);
                for(int k=0;k<foods.size();k++){
                    if((cellLife.get(i).getView().getTranslateX()+cellLife.get(i).getDirectionX()*20==foods.get(k).getView().getTranslateX())&&(cellLife.get(i).getView().getTranslateY()+cellLife.get(i).getDirectionY()*20==foods.get(k).getView().getTranslateY())) {
                        foods.get(k).isDead();
                        gameArea.getChildren().removeAll(foods.get(k).getView());
                        if(cellLife.get(i).getLifeTime()<cellLifeTime){
                        cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()+10);
                        }
                    }
                }
            }else if(command<16){
                cellLife.get(i).makeStep(command,screenSizeX,screenSizeY);
            }else if(command<64){
                cellLife.get(i).setCommandIndex((cellLife.get(i).getCommandIndex()+command)%63);
            }
            cellLife.get(i).setCommandIndex((cellLife.get(i).getCommandIndex()+1)%63);
        }



        if(Math.random()<foodChance){
            int x=((int)(Math.random()*gameArea.getPrefWidth())/20)*20;
            int y=((int)(Math.random()*gameArea.getPrefHeight())/20)*20;
            addFood(new Food(), x,y);
        }

        // no food and cell in one place
        for(int k = 0; k< cellLife.size(); k++) {
            for(int i=0;i<foods.size();i++){
                if (cellLife.get(k).isColliding(foods.get(i))) {
                    foods.get(i).setAlive(false);
                    gameArea.getChildren().removeAll(foods.get(i).getView());
                }
            }
        }
        for(int i=0; i<cellLife.size();i++){
            cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()-1);
            if(cellLife.get(i).getLifeTime()==0){
                cellLife.get(i).setAlive(false);
                gameArea.getChildren().removeAll(cellLife.get(i).getView());
            }
            //System.out.println("generation: "+generation+" lifetime of"+i+" cell: "+cellLife.get(i).getLifeTime());
        }

        foods.removeIf(GameObject::isDead);
        cellLife.removeIf(GameObject::isDead);
        foodChance=foodChance-0.01;
    }


    private class Food extends GameObject{
        Food(){
            super(new Rectangle(18,18,Color.LIGHTGREEN));
            super.getView().setLayoutX(2);
            super.getView().setLayoutY(2);
            super.getView().setStyle("-fx-stroke: green");
        }
    }
    private class CellLife extends GameObject{
        CellLife(){
            super(new Rectangle(18,18,Color.DEEPSKYBLUE));
            super.getView().setLayoutX(2);
            super.getView().setLayoutY(2);
            super.getView().setStyle("-fx-stroke: blue");
        }

    }


    @Override
    public void start(Stage stage){
        stage.setScene(new Scene(createContent()));
        stage.setMaxWidth(screenSizeX);
        stage.setMaxHeight(screenSizeY);
        stage.setTitle("CellLife Game!");
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
