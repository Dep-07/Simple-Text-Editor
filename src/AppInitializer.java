/*
 * Copyright (c) 2021 Thisaru Dasith. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.Properties;

public class AppInitializer extends Application {

     public static Properties prop = new Properties();
     public static File propFile =new File("application.properties");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root  = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Simple Text Editor");
        //primaryStage.setMaximized(true);
        readProperties();
        double width;
        double height;
        double xPos;
        double yPos;

        height =Double.parseDouble(prop.getProperty("height", "-1"));
        width = Double.parseDouble(prop.getProperty("width","-1"));

        if(height ==-1 || width ==-1){

            primaryStage.setMaximized(true);

        }else{

            primaryStage.setHeight(height);
            primaryStage.setWidth(width);
        }

        xPos = Double.parseDouble(prop.getProperty("xpos","-1"));
        yPos = Double.parseDouble(prop.getProperty("ypos","-1"));

        if (xPos == -1 || yPos == -1){

            primaryStage.centerOnScreen();
        }else {

            primaryStage.setX(xPos);
            primaryStage.setY(yPos);
        }


        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {

             if(!primaryStage.isMaximized()){
                 prop.put("height",primaryStage.getHeight()+ "");
                 prop.put("width",primaryStage.getWidth() + "") ;
             }else {
                 prop.put("height","-1");
                 prop.put("width","-1");
             }

             prop.put("xpos",primaryStage.getX() + " ");
             prop.put("ypos",primaryStage.getY()+ " ");

             try(FileOutputStream fis = new FileOutputStream(propFile);
                 BufferedOutputStream bis = new BufferedOutputStream(fis)) {

                 prop.store(bis,null);

             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
        });


    }

    private void readProperties(){

        if (!propFile.exists()) return;

        try(FileInputStream fis = new FileInputStream(propFile);
            BufferedInputStream bis = new BufferedInputStream(fis)){

            prop.load(bis);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
