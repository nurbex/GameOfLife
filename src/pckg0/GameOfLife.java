package pckg0;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends Application {
    private int screenSizeX=300;
    private int screenSizeY=screenSizeX;

    private int skipTime=1;
    private int skipTimeCounter=0;
    private int generation=0;
    private int maxGeneration=50000;
    private int cellLifeTime=1000000;
    private int cellPopulation=40;
    private int commandRange=15; //default 63
    private double foodChance=5.5;
    private double foodChanceCycle =foodChance;
    private int foodTimes=20;
    private int foodCycle=foodTimes;

    private Pane root;
    private Pane gameArea;

    private List<GameObject> foods=new ArrayList<>();
    private List<GameObject> poisons=new ArrayList<>();
    private List<GameObject> cellLife =new ArrayList<>();

    private Parent createContent() {

        root = new Pane();
        root.setPrefSize(screenSizeX, screenSizeY);
        root.setStyle("-fx-background-color: white; ");

        gameArea = new Pane();
        gameArea.setPrefSize(screenSizeX, screenSizeY);
        gameArea.setStyle("-fx-background-color: whitesmoke"); //-fx-border-color:lightgray; default "-fx-background-color: whitesmoke";

        // Adding default cellLife.
        while(cellLife.size()<cellPopulation) {
                int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 10) * 10;
                int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 10) * 10;
                addCellLife(new CellLife(), x, y);
                cellLife.get(cellLife.size()-1).setGeneration(generation);
                cellLife.get(cellLife.size()-1).setCellName(""+generation+"/"+cellLife.indexOf(cellLife.get(cellLife.size()-1)));
        }
        cellLife.get(0).setCellName("Nurbek");
        int[] nurbeksCommands={7, 13, 0, 5, 1, 2, 2, 2, 6, 9, 10, 12, 11, 2, 0, 5, 8, 9, 6, 1, 8, 10, 9, 6, 12, 1, 3, 1, 2, 13, 0, 5, 1, 3, 6, 10, 14, 8, 10, 3, 3, 6, 11, 6, 2, 4, 8, 10, 10, 5, 4, 14, 11, 4, 7, 3, 8, 0, 1, 9, 10, 9, 1, 12};
        cellLife.get(0).setCellLifeCommand(nurbeksCommands);
        //System.out.println(cellLife.get(0).getCellName());
        for(int i=0;i<64;i++){
           // System.out.print(cellLife.get(0).getCellLifeCommand()[i]+" ");
        }

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
                    foodCycle=foodTimes;
                    foodChanceCycle =foodChance;

                    //removing old food
                    for(GameObject food:foods){
                        food.setAlive(false);
                        gameArea.getChildren().removeAll(food.getView());
                    }
                    foods.removeIf(GameObject::isDead);

                    //removing old poison
                    for(GameObject poison:poisons){
                        poison.setAlive(false);
                        gameArea.getChildren().removeAll(poison.getView());
                    }
                    poisons.removeIf(GameObject::isDead);


                    generation++;
                    if(cellLife.size()!=0){
                        System.out.println();
                        System.out.println("********* generation: "+generation+" cell population survived: "+cellLife.size()+ " **********");

                        for(int i=0;i<cellLife.size();i++){
                            //System.out.print("Survived cell generation: "+cellLife.get(i).getGeneration()+". CellName: "+cellLife.get(i).getCellName()+". CellMutation: "+cellLife.get(i).getCellMutation()+ ". Cell Command Genes: ");

                            System.out.print("maxLifeTime: "+cellLife.get(i).getMaxLifeTime()+" cell CommandGenes: ");
                            for(int k=0;k<64;k++){
                                System.out.print(cellLife.get(i).getCellLifeCommand()[k]+" ");
                            }
                            System.out.println();
                        }

                        //creating copies of survived cells
                        int k=cellLife.size();
                        while(cellLife.size()<=32){
                            for(int i=0;i<k;i++){
                                int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 10) * 10;
                                int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 10) * 10;
                                addCellLife(new CellLife(), x, y);
                                for(int j=0;j<64;j++) {
                                    cellLife.get(i + k).getCellLifeCommand()[j] = cellLife.get(i).getCellLifeCommand()[j];
                                }
                                cellLife.get(cellLife.size()-1).setCellMutation(cellLife.get(i).getCellMutation());
                                cellLife.get(cellLife.size()-1).setGeneration(cellLife.get(i).getGeneration());
                                cellLife.get(cellLife.size()-1).setCellName("/"+i);

                            }
                        }
                        // nearly one third of survived cells mutated in one command(gene)
                        for(int i=16;i<cellLife.size();i++){
                            cellLife.get(i).getCellLifeCommand()[(int)((Math.random())*63)]=(int)((Math.random())*commandRange);
                            //System.out.println(" index "+((int)((Math.random())*63))+"/ mutation: "+(int)((Math.random())*63));
                            cellLife.get(i).setCellMutation(cellLife.get(i).getCellMutation()+"/"+generation);
                        }
                    }

                    //creating random population of cells
                    /*while(cellLife.size()!=cellPopulation){
                        int x = ((int) (Math.random() * gameArea.getPrefWidth()) / 10) * 10;
                        int y = ((int) (Math.random() * gameArea.getPrefHeight()) / 10) * 10;
                        addCellLife(new CellLife(), x, y);
                        cellLife.get(cellLife.size()-1).getView().setBlendMode(BlendMode.COLOR_BURN);
                        cellLife.get(cellLife.size()-1).setGeneration(generation);
                        cellLife.get(cellLife.size()-1).setCellName(""+generation+"/"+cellLife.indexOf(cellLife.get(cellLife.size()-1)));
                    }*/

                }


                if(generation==maxGeneration) {
                    stop();
                }
                System.out.print(".");

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
    private void addPoison(GameObject poison,double x,double y){
        poisons.add(poison);
        addGameObject(poison,x,y);
    }
    private void addCellLife(GameObject cell, double x, double y){
        int[] cellCommand=new int[64];
        for(int i=0;i<64;i++){
            cellCommand[i]=((int)(Math.random()*commandRange));
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

        //cell behavior according to command in genes
        for(int i=0;i<cellLife.size();i++){
            //System.out.print(" "+cellLife.get(i).getLifeTime());
            int commandIndex=cellLife.get(i).getCommandIndex();
            int command=cellLife.get(i).getCellLifeCommand()[commandIndex];
            if((command>=0)&&(command<8)){
                cellLife.get(i).look(command);

                //setting what cell saw when it looked
                cellLife.get(i).setRememberX((cellLife.get(i).getView().getTranslateX()+cellLife.get(i).getDirectionX()*10));
                cellLife.get(i).setRememberY((cellLife.get(i).getView().getTranslateY()+cellLife.get(i).getDirectionY()*10));

                //looking to direction, eating food if there is
                for(int k=0;k<foods.size();k++){
                    if((cellLife.get(i).getView().getTranslateX()+cellLife.get(i).getDirectionX()*10==foods.get(k).getView().getTranslateX())&&(cellLife.get(i).getView().getTranslateY()+cellLife.get(i).getDirectionY()*10==foods.get(k).getView().getTranslateY())) {
                        foods.get(k).isDead();
                        gameArea.getChildren().removeAll(foods.get(k).getView());
                        if(cellLife.get(i).getLifeTime()<cellLifeTime){
                        cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()+5);
                        }
                    }
                }
                //looking to direction, if there is poison converting it to food but not eating, if it looks second time it will eat it, or maybe i will change it later
                for(int k=0;k<poisons.size();k++){
                    if((cellLife.get(i).getView().getTranslateX()+cellLife.get(i).getDirectionX()*10==poisons.get(k).getView().getTranslateX())&&(cellLife.get(i).getView().getTranslateY()+cellLife.get(i).getDirectionY()*10==poisons.get(k).getView().getTranslateY())) {
                        poisons.get(k).setAlive(false);
                        //addFood(new Food(), poisons.get(k).getView().getTranslateX(),poisons.get(k).getView().getTranslateY());
                        if(cellLife.get(i).getLifeTime()<cellLifeTime){
                            cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()+5);
                        }
                        gameArea.getChildren().removeAll(poisons.get(k).getView());
                    }
                }
            }else if(command<16){

                //unsetting what cell saw
                cellLife.get(i).setRememberX(screenSizeX+1);
                cellLife.get(i).setRememberY(screenSizeY+1);


                cellLife.get(i).makeStep(command,screenSizeX,screenSizeY);
                //uses energy to move
                cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()-1);
                //cell dies if it steps ont poison
                for(int j=0;j<poisons.size();j++){
                    if((cellLife.get(i).getView().getTranslateX()+cellLife.get(i).getDirectionX()*10==poisons.get(j).getView().getTranslateX())&&(cellLife.get(i).getView().getTranslateY()+cellLife.get(i).getDirectionY()*10==poisons.get(j).getView().getTranslateY())) {
                        poisons.get(j).setAlive(false);
                        cellLife.get(i).setAlive(false);
                        gameArea.getChildren().removeAll(poisons.get(j).getView(),cellLife.get(i).getView());
                    }
                }
            }else if(command<64){

                //unsetting what cell saw
                cellLife.get(i).setRememberX(screenSizeX+1);
                cellLife.get(i).setRememberY(screenSizeY+1);

                cellLife.get(i).setCommandIndex((cellLife.get(i).getCommandIndex()+command)%63);
            }
            cellLife.get(i).setCommandIndex((cellLife.get(i).getCommandIndex()+1)%63);
        }

        //adding amount of food and poison at the begining
        for(int i=0; i<foodCycle;foodCycle--){
            //adding food
            if(Math.random()< foodChanceCycle){
                int x=((int)(Math.random()*gameArea.getPrefWidth())/10)*10;
                int y=((int)(Math.random()*gameArea.getPrefHeight())/10)*10;
                addFood(new Food(), x,y);
            }
            //adding poison
            if(Math.random()< foodChanceCycle){
                int x=((int)(Math.random()*gameArea.getPrefWidth())/10)*10;
                int y=((int)(Math.random()*gameArea.getPrefHeight())/10)*10;
                addPoison(new Poison(), x,y);
                for(int k = 0; k< cellLife.size(); k++) {
                    if(!((cellLife.get(k).getView().getTranslateX()==x)&&(cellLife.get(k).getView().getTranslateY()==y))){
                        addPoison(new Poison(), x,y);
                    }
                }
            }
        }
        //adding food each cycle
        if(Math.random()< foodChanceCycle){
            int x=((int)(Math.random()*gameArea.getPrefWidth())/10)*10;
            int y=((int)(Math.random()*gameArea.getPrefHeight())/10)*10;
            //not creating food on cell
            for(int k = 0; k< cellLife.size(); k++) {
                if(!((cellLife.get(k).getView().getTranslateX()==x)&&(cellLife.get(k).getView().getTranslateY()==y))){
                    addFood(new Food(), x,y);
                }
            }
        }
        //adding poison each cycle
        if(Math.random()< foodChanceCycle){
            int x=((int)(Math.random()*gameArea.getPrefWidth())/10)*10;
            int y=((int)(Math.random()*gameArea.getPrefHeight())/10)*10;
            //not killing cell from the begining
            for(int k = 0; k< cellLife.size(); k++) {
                if(!((cellLife.get(k).getView().getTranslateX()==x)&&(cellLife.get(k).getView().getTranslateY()==y))){
                    addPoison(new Poison(), x,y);
                }

                // not allowing poison appear there where it just looked
                if(!((cellLife.get(k).getRememberX()==x)&&(cellLife.get(k).getRememberY()==y))){
                    addPoison(new Poison(), x,y);

                }
            }
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

        //decrementing lifeTime of cell each cycle and removing dead cells from scene
        for(int i=0; i<cellLife.size();i++){
            cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()-1);
            if(cellLife.get(i).getLifeTime()==0){
                cellLife.get(i).setAlive(false);
                gameArea.getChildren().removeAll(cellLife.get(i).getView());
            }
            //System.out.println("generation: "+generation+" lifetime of"+i+" cell: "+cellLife.get(i).getLifeTime());
        }

        //removing dead objects from List
        foods.removeIf(GameObject::isDead);
        cellLife.removeIf(GameObject::isDead);

        //decrementing food chance with each cycle
        foodChanceCycle = foodChanceCycle -0.01;
    }

    private class Food extends GameObject{
        Food(){
            super(new Rectangle(8,8,Color.LIGHTGREEN));
            super.getView().setLayoutX(2);
            super.getView().setLayoutY(2);
            super.getView().setStyle("-fx-stroke: green");
        }
    }
    private class Poison extends GameObject{
        Poison(){
            super(new Rectangle(8,8,Color.CORAL));
            super.getView().setLayoutX(2);
            super.getView().setLayoutY(2);
            super.getView().setStyle("-fx-stroke: crimson");
        }
    }
    private class CellLife extends GameObject{
        CellLife(){
            super(new Rectangle(8,8,Color.DEEPSKYBLUE));
            super.getView().setLayoutX(2);
            super.getView().setLayoutY(2);
            super.getView().setStyle("-fx-stroke: blue");
        }
    }


    @Override
    public void start(Stage stage){
        stage.setScene(new Scene(createContent()));
        //stage.setMaxWidth(screenSizeX);
        //stage.setMaxHeight(screenSizeY);
        stage.setTitle("CellLife Game!");
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
