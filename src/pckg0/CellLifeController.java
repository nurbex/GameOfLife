package pckg0;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CellLifeController implements Initializable {
    private int screenSizeX=300;
    private int screenSizeY=screenSizeX;

    private int skipTime=1;
    private int skipTimeCounter=0;
    private int generation=0;
    private int maxGeneration=50000;
    private int cellLifeTime=1000000;
    private int cellPopulation=40;
    private int commandRange=15; //default 63
    private double foodChance=55.5;
    private double foodChanceCycle =foodChance;
    private int foodTimes=20;
    private int foodCycle=foodTimes;


    private int totalMaxLife;



    @FXML
    private Pane gameArena =new Pane();

    @FXML
    private Button startButton=new Button();

    @FXML
    private Button button11=new Button();

    @FXML
    private NumberAxis generationOfCells = new NumberAxis();

    @FXML
    private NumberAxis maxLifeTotal = new NumberAxis();

    @FXML
    private AreaChart<Number, Number> GenStatistic = new AreaChart<Number, Number>(generationOfCells, maxLifeTotal);



    XYChart.Series<Number, Number> generationSeries = new XYChart.Series<Number, Number>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    private List<GameObject> foods=new ArrayList<>();
    private List<GameObject> poisons=new ArrayList<>();
    private List<GameObject> cellLife =new ArrayList<>();









    @FXML
    private void createContent() {



        startButton.setDisable(true);

        System.out.println("Button Clicked!");
        // Series data of generations

        generationSeries.setName("generations");
        GenStatistic.setCreateSymbols(false);
        GenStatistic.getData().add(generationSeries);







        // Adding default cellLife.
        while(cellLife.size()<cellPopulation) {
            int x = ((int) (Math.random() * gameArena.getPrefWidth()) / 10) * 10;
            int y = ((int) (Math.random() * gameArena.getPrefHeight()) / 10) * 10;
            addCellLife(new CellLife(), x, y);
            cellLife.get(cellLife.size()-1).setGeneration(generation);
            cellLife.get(cellLife.size()-1).setCellName(""+generation+"/"+cellLife.indexOf(cellLife.get(cellLife.size()-1)));
        }
        cellLife.get(0).setCellName("Nurbek");
        int[] nurbeksCommands={0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 2, 10};
        cellLife.get(0).setCellLifeCommand(nurbeksCommands);
        //System.out.println(cellLife.get(0).getCellName());


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
                    totalMaxLife=0;
                    //GenStatistic.getData().remove(generationSeries);

                    //sum of cell max Lifetime;

                    for(GameObject cell:cellLife){
                        totalMaxLife=totalMaxLife+cell.getMaxLifeTime();
                    }

                    generationSeries.getData().add(new XYChart.Data<Number, Number>(generation,totalMaxLife));

                    /*for(int i=0;i<generationSeries.getData().size();i++){
                        System.out.println(generationSeries.getData().get(i).getXValue());
                        System.out.println(generationSeries.getData().get(i).getYValue());
                        System.out.println(GenStatistic.getData().get(0).getData().get(i).getXValue());
                    }
                    System.out.println(totalMaxLife+"/"+generation);*/



                    //removing old food
                    for(GameObject food:foods){
                        food.setAlive(false);
                        gameArena.getChildren().removeAll(food.getView());
                    }
                    foods.removeIf(GameObject::isDead);

                    //removing old poison
                    for(GameObject poison:poisons){
                        poison.setAlive(false);
                        gameArena.getChildren().removeAll(poison.getView());
                    }
                    poisons.removeIf(GameObject::isDead);


                    generation++;
                    if(cellLife.size()!=0){
                        System.out.println();
                        System.out.println("********* generation: "+generation+" cell population survived: "+cellLife.size()+ " **********");

                        for(int i=0;i<cellLife.size();i++){
                            //System.out.print("Survived cell generation: "+cellLife.get(i).getGeneration()+". CellName: "+cellLife.get(i).getCellName()+". CellMutation: "+cellLife.get(i).getCellMutation()+ ". Cell Command Genes: ");

                            System.out.print("maxLifeTime: "+cellLife.get(i).getMaxLifeTime()+" Commands: ");
                            for(int k=0;k<64;k++){
                                System.out.print(cellLife.get(i).getCellLifeCommand()[k]+",");
                            }
                            System.out.println();
                        }

                        //creating copies of survived cells
                        int k=cellLife.size();
                        int xzy=0;
                        while(cellLife.size()<=32){

                            int x = ((int) (Math.random() * gameArena.getPrefWidth()) / 10) * 10;
                            int y = ((int) (Math.random() * gameArena.getPrefHeight()) / 10) * 10;
                            addCellLife(new CellLife(), x, y);
                            for(int j=0;j<64;j++) {
                                cellLife.get(cellLife.size()-1).getCellLifeCommand()[j] = cellLife.get(xzy).getCellLifeCommand()[j];
                            }
                            cellLife.get(cellLife.size()-1).setCellMutation(cellLife.get(xzy).getCellMutation());
                            cellLife.get(cellLife.size()-1).setGeneration(cellLife.get(xzy).getGeneration());
                            cellLife.get(cellLife.size()-1).setCellName("/"+xzy);
                            xzy++;
                            if(xzy==k){xzy=0;}
                        }

                        // nearly one third of survived cells mutated in one command(gene)
                        for(int i=16;i<cellLife.size();i++){
                            cellLife.get(i).getCellLifeCommand()[(int)((Math.random())*63)]=(int)((Math.random())*commandRange);
                            //System.out.println(" index "+((int)((Math.random())*63))+"/ mutation: "+(int)((Math.random())*63));
                            cellLife.get(i).setCellMutation(cellLife.get(i).getCellMutation()+"/"+generation);
                        }
                    }

                    System.out.println();
                }


                if(generation==maxGeneration) {
                    stop();

                }



            }
        };
        timer.start();



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
        gameArena.getChildren().add(object.getView());
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
                        gameArena.getChildren().removeAll(foods.get(k).getView());
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
                        gameArena.getChildren().removeAll(poisons.get(k).getView());
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
                        gameArena.getChildren().removeAll(poisons.get(j).getView(),cellLife.get(i).getView());
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
                int x=((int)(Math.random()* gameArena.getPrefWidth())/10)*10;
                int y=((int)(Math.random()* gameArena.getPrefHeight())/10)*10;
                addFood(new Food(), x,y);
            }
            //adding poison
            if(Math.random()< foodChanceCycle){
                int x=((int)(Math.random()* gameArena.getPrefWidth())/10)*10;
                int y=((int)(Math.random()* gameArena.getPrefHeight())/10)*10;
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
            int x=((int)(Math.random()* gameArena.getPrefWidth())/10)*10;
            int y=((int)(Math.random()* gameArena.getPrefHeight())/10)*10;
            //not creating food on cell
            for(int k = 0; k< cellLife.size(); k++) {
                if(!((cellLife.get(k).getView().getTranslateX()==x)&&(cellLife.get(k).getView().getTranslateY()==y))){
                    addFood(new Food(), x,y);
                }
            }
        }
        //adding poison each cycle
        if(Math.random()< foodChanceCycle){
            int x=((int)(Math.random()* gameArena.getPrefWidth())/10)*10;
            int y=((int)(Math.random()* gameArena.getPrefHeight())/10)*10;
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
                    gameArena.getChildren().removeAll(foods.get(i).getView());
                }
            }
        }

        //decrementing lifeTime of cell each cycle and removing dead cells from scene
        for(int i=0; i<cellLife.size();i++){
            cellLife.get(i).setLifeTime(cellLife.get(i).getLifeTime()-1);
            if(cellLife.get(i).getLifeTime()==0){
                cellLife.get(i).setAlive(false);
                gameArena.getChildren().removeAll(cellLife.get(i).getView());
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


}