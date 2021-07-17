
/*
 * Copyright (c) 2021 Thisaru Dasith. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package controller;

import com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource;
import javafx.beans.value.ChangeListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.FXDesign;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFormController {
    public TextArea txtEditor;
    public TextField txtSearch;
    public Button btnFindNext;
    public Button btnFindPrevious;
    public TextField txtReplace;
    public AnchorPane pneReplace;
    public AnchorPane pneFind;
    public TextField txtFind;
    public MenuItem mnuFileOpen;
    private String filepath;


    private int findOfSet = -1;
    private final List<Index> index = new ArrayList<>();
    private PrinterJob printerJob;




    public  void initialize(){

        this.printerJob = PrinterJob.createPrinterJob();

        ChangeListener t = (ChangeListener<String>) (observable, oldValue, newValue) -> searchMatch(newValue);

        txtSearch.textProperty().addListener(t);
        txtFind.textProperty().addListener(t);

        pneReplace.setVisible(false);
        pneFind.setVisible(false);



    }

    private void searchMatch(String value){

        FXDesign.highlightOnTextArea(txtEditor,value, Color.web("yellow", 0.8));

        Pattern patter = Pattern.compile(value);
        Matcher matcher = patter.matcher(txtEditor.getText());

        index.clear();

        while (matcher.find()){
            index.add(new Index(matcher.start(),matcher.end()));
        }

    }

    public void btnFindNext_OnAction(ActionEvent actionEvent) {

        if(!index.isEmpty()){
            if (findOfSet == -1){
                findOfSet = 0;
            }
            txtEditor.selectRange(index.get(findOfSet).startIndex,index.get(findOfSet).endIndex);
            findOfSet ++;

            if(index.size()<= findOfSet){
                findOfSet = 0;
            }
        }
    }
    public void btnFindPrevious_OnAction(ActionEvent actionEvent) {

        if(!index.isEmpty()){
            if (findOfSet ==-1){
                findOfSet = index.size()-1;
            }
            txtEditor.selectRange(index.get(findOfSet).startIndex,index.get(findOfSet).endIndex);
            findOfSet --;

            if(index.size()<=0){
                findOfSet = index.size()-1;
            }
        }

    }

    public void mnuItemFind_OnAction(ActionEvent actionEvent) {

        if (pneReplace.isVisible()){
            pneReplace.setVisible(false);
        }

        pneFind.setVisible(true);
        txtFind.requestFocus();

    }

    public void mnuItemReplace_OnAction(ActionEvent actionEvent) {

        if (pneFind.isVisible()){
            pneFind.setVisible(false);
        }

        pneReplace.setVisible(true);
        txtSearch.requestFocus();

    }

    public void mnuItemSelectAll_OnAction(ActionEvent actionEvent) {

        if (!txtEditor.getText().isEmpty()){
            txtEditor.selectAll();
        }
    }

    public void mnuItemNew_OnAction(ActionEvent actionEvent) {

        txtEditor.clear();
        txtEditor.requestFocus();

    }

    public void mnuItemExit_OnAction(ActionEvent actionEvent) {

        System.exit(0);

    }

    public void btnReplace_OnAction(ActionEvent actionEvent) {

        String textReplace = txtReplace.getText();
        String text = txtEditor.getText();
        int startIndex = index.get(findOfSet-1).startIndex;
        int endIndex = index.get(findOfSet-1).endIndex;

        if (findOfSet == -1) return;
        txtEditor.replaceText(startIndex,endIndex, txtReplace.getText());
        searchMatch(txtSearch.getText());

    }

    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {

        String textReplace = txtReplace.getText();
        String textSearch = txtSearch.getText();
        String text = txtEditor.getText();
        String s = text.replaceAll(textSearch, textReplace);
        txtEditor.setText(s);

    }


    public void mnuItemSavaAs_OnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File As");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
        filepath = file.getAbsolutePath();
        System.out.println(filepath);

        if (file == null) return;

        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bfw = new BufferedWriter(fileWriter)) {

            bfw.write(txtEditor.getText());


        } catch (IOException e) {

            e.printStackTrace();

        }


    }

    public void mnuFileOpen_OnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All text files","*.txt","*.html"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files","*"));
        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        System.out.println(file);

        if(fileChooser == null){

            return;
        }

        txtEditor.clear();

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String line = null;
            while ((line =bufferedReader.readLine()) != null){

                txtEditor.appendText(line + '\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void mnuItemSave_OnAction(ActionEvent actionEvent) {

        if(filepath == null){
            try {
                mnuItemSavaAs_OnAction(null);
            }catch (NullPointerException e){

            }
        }else {
            File file = new File(filepath);
            try(FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bfw = new BufferedWriter(fileWriter)) {

                bfw.write(txtEditor.getText());


            } catch (IOException e) {

                e.printStackTrace();

            }
        }




    }

    public void mnuItemPrint_OnAction(ActionEvent actionEvent) {

        boolean printDialog = printerJob.showPrintDialog(txtEditor.getScene().getWindow());
        if(printDialog){

            printerJob.printPage(txtEditor.lookup("Text"));
            
        }

    }

    public void mnuItemPageSetup_OnAction(ActionEvent actionEvent) {

        printerJob.showPageSetupDialog(txtEditor.getScene().getWindow());

    }


    public void mnuItemCut_OnAction(ActionEvent actionEvent) {

        txtEditor.cut();

    }

    public void mnuItemCopy_OnAction(ActionEvent actionEvent) {

        txtEditor.copy();

    }

    public void mnuItemPaste_OnAction(ActionEvent actionEvent) {

        txtEditor.paste();


    }


    public void textSelect_OnMousePressed(MouseEvent mouseEvent) {
        System.out.println("hi");
        String selectedText = txtEditor.getSelectedText();
        System.out.println(selectedText);

    }

    public void mnuItemNewWindow_OnAction(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("Simple Text Editor");
        newStage.setResizable(false);
        newStage.centerOnScreen();
        newStage.show();

    }
}

class Index{

    int startIndex;
    int endIndex;

    public Index(int startIndex, int endIndex) {

        this.startIndex = startIndex;
        this.endIndex = endIndex;

    }

}

