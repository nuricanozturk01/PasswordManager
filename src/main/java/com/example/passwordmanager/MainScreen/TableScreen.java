package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.Crypthology.AES256;
import com.example.passwordmanager.DB.App;
import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.MainScreen.MainScreenExceptions.NullContextMenuException;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableScreen implements EventHandler<ActionEvent>
{
    private Stage stage;

    private Connection connection;
    private Statement statement;

    private User user;

    private Label searchLbl;
    private TextField searchField;

    private TableView<App> table;

    private ObservableList<App> appList;
    private ObservableList<App> secretAppList;

    private ContextMenu cm;
    private MenuItem update;
    private MenuItem delete;


    public TableScreen(Stage stage, User user)
    {
        this.stage = stage;
        this.user = user;

        initComponents();
        initTable();
    }


    public ObservableList<App> getAppList() {
        return appList;
    }

    private void initComponents()
    {
        try
        {
            searchLbl = new Label("Search:  ");

            searchField = new TextField();
            searchField.setPrefWidth(300);
            searchField.setOnAction(e->{filter();});

            appList = FXCollections.observableArrayList();
            secretAppList = FXCollections.observableArrayList();

            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    private void filter()
    {
        ObservableList<App> lst;
        if (MainScreen.isShow)
            lst = appList;
        else lst = secretAppList;
        FilteredList<App> list = new FilteredList<>(lst,b->true);
        searchField.textProperty().addListener((observable,oldValue,newValue)->
        {
            list.setPredicate(app->
            {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String keyword = newValue.toLowerCase();
                if (app.getAppName().toLowerCase().indexOf(keyword) > -1)
                    return true;
                else if (app.getAppUsername().toLowerCase().indexOf(keyword) > -1)
                    return true;
                else return false;
            });
        });

        SortedList<App> sortedList = new SortedList<>(list);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private void initTable()
    {
        table = new TableView<App>();
        table.setPlaceholder(new Label("Empty"));
        table.setPrefHeight(table.getMinHeight()+ 570);
        table.setEditable(false);

        table.getStylesheets().add(MainMenu.class.getResource("table_css.css").toExternalForm());


        TableColumn<App,String> col1 = new TableColumn<>("App Name");
        TableColumn<App,String> col2 = new TableColumn<>("Username");
        TableColumn<App,String> col3 = new TableColumn<>("Password");


        table.getColumns().add(col1);
        table.getColumns().add(col2);
        table.getColumns().add(col3);


        col1.setCellValueFactory(new PropertyValueFactory<App,String>("appName"));
        col2.setCellValueFactory(new PropertyValueFactory<App,String >("appUsername"));
        col3.setCellValueFactory(new PropertyValueFactory<App,String >("appPassword"));

        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setCellFactory(TextFieldTableCell.forTableColumn());

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initContextMenu();
        table.setContextMenu(cm);


        getList();

        if (!MainScreen.isShow)
            listApp(secretAppList);
        else listApp(appList);

    }


    private void initContextMenu()
    {
        cm = new ContextMenu();

        delete = new MenuItem("Delete");
        update = new MenuItem("Update");

        cm.getItems().addAll(delete,update);

        delete.setOnAction(e->
        {
            try
            {
                if (table.getSelectionModel().getSelectedItem() == null)
                    throw new NullContextMenuException();

                delete(table.getSelectionModel().getSelectedItem());

            }
            catch (NullContextMenuException exception)
            {
                Methods.alertScreen(Alert.AlertType.WARNING,exception.getMessage(),ButtonType.OK);
            }
        });
        update.setOnAction(e->
        {
            try
            {
                if (table.getSelectionModel().getSelectedItem() == null)
                    throw new NullContextMenuException();

                    new UpdateScreen(user,table.getSelectionModel().getSelectedItem(),this).start(new Stage());
            } catch (NullContextMenuException exception)
            {
                Methods.alertScreen(Alert.AlertType.WARNING,exception.getMessage(),ButtonType.OK);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        });
    }


    private void delete(App app)
    {
        new DeleteScreen(user,app,this).delete(app);
    }


    public VBox getVBox()
    {

        FlowPane searchFp = new FlowPane(searchLbl,searchField);
        searchFp.setHgap(10);
        searchFp.setVgap(10);
        VBox leftSideVBox = new VBox(searchFp,table);
        leftSideVBox.setSpacing(15);
        return leftSideVBox;
    }


    public int getAppIndex(App app, ObservableList<App> list)
    {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getIterator());
        }
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).equals(app))
                return i;
        }
        return -1;
    }
    public int getAppIndex(String name, ObservableList<App> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getAppUsername().equals(name))
                return i;
        }
        return -1;
    }

    public ObservableList<App> getSecretAppList() {
        return secretAppList;
    }


    private void getList()
    {
        try
        {
            AES256 aes256 = new AES256(AES256.generateSecretKey(user.getUsername()));

            String uname = user.getUsername();
            String password = user.getPassword();

            String listQuery = "CALL getList(\""+uname+"\",\""+password+"\");";

            uname = null;
            password = null;
            ResultSet rset = statement.executeQuery(listQuery);
            appList.clear();

            for(int i = 1; rset.next() ; ++i)
            {
                App app = new App(
                                        i,
                                        rset.getInt("appID"),
                                        rset.getString("appName"),
                                        rset.getString("appUsername"),
                                        aes256.decrypt(rset.getString("appPassword"))
                                );

                appList.add(app);

                App app2 = new App(app.getAppID(),app.getAppName(),app.getAppUsername(),app.getAppPassword());
                app2.setAppPassword(Constants.MASK);

                secretAppList.add(app2);

            }


            rset.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void update()
    {
        updateListIterator();

        if (MainScreen.isShow)
            listApp(appList);
        else
            listApp(secretAppList);

    }

    private void updateListIterator() {
        for (int i = 0; i < appList.size(); i++)
            appList.get(i).setIterator(i+1);

    }
    private void listApp(ObservableList<App> list)
    {
        table.setItems(list);
    }

    @Override
    public void handle(ActionEvent actionEvent)
    {

    }
}
