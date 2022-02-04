package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.PasswordGenerator.PasswordGenerator;
import com.example.passwordmanager.Util.Methods;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import com.jfoenix.controls.JFXRadioButton;



public class GeneratePasswordScreen implements EventHandler<ActionEvent>
{

    private Label passwordGeneratorLbl;

    private JFXRadioButton lowerRBtn;
    private JFXRadioButton upperRBtn;
    private JFXRadioButton numberRBtn;
    private JFXRadioButton autoBtn;
    private JFXRadioButton specialRBtn;

    private JFXButton generateBtn;
    private JFXButton clearBtn;

    private TextField passwordGeneratorTxt;
    private TextField autoSizeTxt;
    private TextField lowerTxt;
    private TextField upperTxt;
    private TextField numberTxt;
    private TextField specialTxt;


    public GeneratePasswordScreen()
    {
        passwordGeneratorLbl = new Label("\t\tPasswordGenerator");
        passwordGeneratorLbl.setFont(new Font("Arial",15));
        passwordGeneratorLbl.setStyle("-fx-font-weight: bold");

        passwordGeneratorTxt = new TextField();
        passwordGeneratorTxt.setPrefWidth(300);
        passwordGeneratorTxt.setMaxWidth(300);
        passwordGeneratorTxt.setEditable(false);


        lowerTxt    = new TextField();
        upperTxt    = new TextField();
        numberTxt   = new TextField();
        specialTxt  = new TextField();
        autoSizeTxt = new TextField();

        lowerTxt.setPrefWidth(40);
        upperTxt.setPrefWidth(40);
        numberTxt.setPrefWidth(40);
        specialTxt.setPrefWidth(40);
        autoSizeTxt.setPrefWidth(40);

        lowerRBtn   = new JFXRadioButton("[a-z]");
        upperRBtn   = new JFXRadioButton("[A-Z]");
        numberRBtn  = new JFXRadioButton("[0-9]");
        specialRBtn = new JFXRadioButton("[#-?]");

        autoBtn     = new JFXRadioButton("Auto");
        generateBtn = new JFXButton("Generate");
        clearBtn    = new JFXButton("Clear");

        setActions();

    }

    public VBox getVBox()
    {
        VBox passwordGeneratorVBox = new VBox(passwordGeneratorLbl, passwordGeneratorTxt);
        passwordGeneratorVBox.setSpacing(10);

        HBox PasswordGeneratorHBoxC1    = new HBox(lowerRBtn,lowerTxt,upperRBtn,upperTxt);
        HBox PasswordGeneratorHBoxC2    = new HBox(numberRBtn,numberTxt,specialRBtn,specialTxt);
        HBox PasswordGeneratorHBoxA     = new HBox(autoBtn, autoSizeTxt);
        HBox btn                        = new HBox(generateBtn,clearBtn);

        btn.setSpacing(160);
        btn.setPadding(new Insets(5));

        PasswordGeneratorHBoxC1.setSpacing(10);
        PasswordGeneratorHBoxC1.setPadding(new Insets(5));
        PasswordGeneratorHBoxC2.setPadding(new Insets(5));
        PasswordGeneratorHBoxA.setPadding(new Insets(5));
        PasswordGeneratorHBoxC2.setSpacing(10);
        PasswordGeneratorHBoxA.setSpacing(9);

        VBox passwordGeneratorVBox1     = new VBox(PasswordGeneratorHBoxA,btn);
        passwordGeneratorVBox1.setSpacing(10);

        VBox passwordGeneratorVBox2     = new VBox(PasswordGeneratorHBoxC1,PasswordGeneratorHBoxC2);
        passwordGeneratorVBox2.setSpacing(5);

        VBox passwordGeneratorVBox3     = new VBox(passwordGeneratorVBox2,passwordGeneratorVBox1);
        passwordGeneratorVBox3.setSpacing(15);

        return new VBox(passwordGeneratorVBox,passwordGeneratorVBox3);
    }

    private void setActions() {
        clearBtn.setOnAction(this);
        lowerRBtn.setOnAction(this);
        upperRBtn.setOnAction(this);
        numberRBtn.setOnAction(this);
        specialRBtn.setOnAction(this);
        autoBtn.setOnAction(this);
        generateBtn.setOnAction(this);
        lowerTxt.setOnAction(this);
        upperTxt.setOnAction(this);
        numberTxt.setOnAction(this);
        specialTxt.setOnAction(this);
        passwordGeneratorTxt.setOnAction(this);
        autoSizeTxt.setOnAction(this);
    }


    private void setActiveOrDisabled(boolean state)
    {
        lowerRBtn.setDisable(state);
        upperRBtn.setDisable(state);
        numberRBtn.setDisable(state);
        specialRBtn.setDisable(state);

        lowerTxt.setDisable(state);
        upperTxt.setDisable(state);
        numberTxt.setDisable(state);
        specialTxt.setDisable(state);
    }


    private void clearCustomizedTexts()
    {
        passwordGeneratorTxt.clear();
        lowerRBtn.setSelected(false);
        upperRBtn.setSelected(false);
        numberRBtn.setSelected(false);
        specialRBtn.setSelected(false);
        lowerTxt.clear();
        upperTxt.clear();
        numberTxt.clear();
        specialTxt.clear();
    }

    public void randomGenerate()
    {
        try
        {
            String size = autoSizeTxt.getText();
            int intSize = Integer.parseInt(size);
            PasswordGenerator pg = new PasswordGenerator(intSize);
            String pass = pg.randomGenerate();
            passwordGeneratorTxt.setText(pass);
        }
        catch (NumberFormatException e)
        {
            Methods.alertScreen(Alert.AlertType.ERROR,"Please enter the number!", ButtonType.OK);
        }
    }
    public void customizedGenerate()
    {
        try
        {
            int lowerCount, upperCount, numberCount, specialCount;

            lowerCount = (lowerTxt.getText().isBlank() || lowerTxt.getText().isEmpty()) ||
                    !lowerRBtn.isSelected() ? 0: Integer.parseInt(lowerTxt.getText());
            upperCount = (upperTxt.getText().isBlank() || upperTxt.getText().isEmpty()) ||
                    !upperRBtn.isSelected() ? 0: Integer.parseInt(upperTxt.getText());
            numberCount = (numberTxt.getText().isBlank() || numberTxt.getText().isEmpty()) ||
                    !numberRBtn.isSelected() ? 0: Integer.parseInt(numberTxt.getText());
            specialCount = (specialTxt.getText().isBlank() || specialTxt.getText().isEmpty()) ||
                    !specialRBtn.isSelected() ? 0: Integer.parseInt(specialTxt.getText());

            PasswordGenerator pg = new PasswordGenerator(upperCount,lowerCount,numberCount,specialCount);
            String password = pg.customizedGenerate();

            passwordGeneratorTxt.setText(password);

        }
        catch (NumberFormatException e)
        {
            Methods.alertScreen(Alert.AlertType.ERROR,"Please enter the number!",ButtonType.OK);
        }

    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (autoBtn.isArmed())
        {
            clearCustomizedTexts();
            setActiveOrDisabled(!lowerRBtn.isDisabled());
        }

        if (generateBtn.isArmed())
        {
            if (autoBtn.isSelected())
                randomGenerate();
            else if (lowerRBtn.isSelected() || upperRBtn.isSelected() || numberRBtn.isSelected() || specialRBtn.isSelected())
                customizedGenerate();
            else Methods.alertScreen(Alert.AlertType.WARNING,"Please enter the least one button!",ButtonType.OK);
        }

        if (clearBtn.isArmed())
        {
            clearCustomizedTexts();
            autoBtn.setSelected(false);
            autoSizeTxt.clear();
            setActiveOrDisabled(false);
        }

    }


}
