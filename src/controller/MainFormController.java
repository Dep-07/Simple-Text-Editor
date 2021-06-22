package controller;

import javafx.beans.value.ChangeListener;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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


    private int findOfSet = -1;
    private final List<Index> index = new ArrayList<>();


    public  void initialize(){


        ChangeListener t = (ChangeListener<String>) (observable, oldValue, newValue) -> searchMatch(newValue);

        txtSearch.textProperty().addListener(t);
        txtFind.textProperty().addListener(t);


        pneReplace.setVisible(false);
        pneFind.setVisible(false);
    }

    private void searchMatch(String value){

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



}

class Index{
    int startIndex;
    int endIndex;

    public Index(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

}

