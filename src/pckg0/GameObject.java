package pckg0;

import javafx.scene.Node;

public class GameObject {
    private Node view;
    private boolean isAlive=true;

    private int lifeTime=90;

    private int commandIndex=0;
    private int[] cellLifeCommand=new int[64];

    private int velocity=20;
    private int directionX;
    private int directionY;

    public GameObject(Node view){
        this.view=view;
    }
    public Node getView(){
        return view;
    }
    public boolean isDead(){
        return !isAlive;
    }
    public void setAlive(boolean isAlive){
        this.isAlive=isAlive;
    }

    public int getLifeTime(){
        return lifeTime;
    }
    public void setLifeTime(int lifeTime){
        this.lifeTime=lifeTime;
    }

    public void setDirectionX(int x){
        directionX=x;
    }
    public void setDirectionY(int y){
        directionY=y;
    }

    public int getDirectionX(){
        return directionX;
    }
    public int getDirectionY(){
        return directionY;
    }

    public int getCommandIndex(){
        return commandIndex;
    }
    public void setCommandIndex(int commandIndex){
        this.commandIndex=commandIndex;
    }

    public int[] getCellLifeCommand(){
        return cellLifeCommand;
    }
    public void setCellLifeCommand(int[] cellLifeCommand){
        this.cellLifeCommand=cellLifeCommand;
    }


    //method look from 0 to 7, sets direction
    public void look(int l){
        switch(l){
            case 0: {
                directionX=-1;
                directionY=1;
                break;
            }
            case 1: {
                directionX=0;
                directionY=1;
                break;
            }
            case 2: {
                directionX=1;
                directionY=1;
                break;
            }
            case 3: {
                directionX=1;
                directionY=0;
                break;
            }
            case 4: {
                directionX=1;
                directionY=-1;
                break;
            }
            case 5: {
                directionX=0;
                directionY=-1;
                break;
            }
            case 6: {
                directionX=-1;
                directionY=-1;
                break;
            }
            case 7: {
                directionX=-1;
                directionY=0;
                break;
            }
        }
    }

    //makes 1 step, to directions its looking, from 8 to 15.
    public void makeStep(int m, int screenX, int screenY){

        switch(m){
            case 8: {
                directionX=-1;
                directionY=1;
                break;
            }
            case 9: {
                directionX=0;
                directionY=1;
                break;
            }
            case 10: {
                directionX=1;
                directionY=1;
                break;
            }
            case 11: {
                directionX=1;
                directionY=0;
                break;
            }
            case 12: {
                directionX=1;
                directionY=-1;
                break;
            }
            case 13: {
                directionX=0;
                directionY=-1;
                break;
            }
            case 14: {
                directionX=-1;
                directionY=-1;
                break;
            }
            case 15: {
                directionX=-1;
                directionY=0;
                break;
            }
        }
        view.setTranslateX(view.getTranslateX() + directionX*velocity);
        view.setTranslateY(view.getTranslateY() + directionY*velocity);

        if((view.getTranslateX() + directionX*velocity)>screenX){
            view.setTranslateX(-20 + directionX*velocity);
        }
        else if(((view.getTranslateX() + directionX*velocity)<-20)){
            view.setTranslateX(screenX + directionX*velocity);
        }


        if((view.getTranslateY() + directionY*velocity)>screenY){
            view.setTranslateY(-20 + directionY*velocity);
        }
        else if(((view.getTranslateY() + directionY*velocity)<-20)){
            view.setTranslateY(screenY + directionY*velocity);
        }
    }

    public boolean isColliding(GameObject other){
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }


}
