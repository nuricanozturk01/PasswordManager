package com.example.passwordmanager.MainScreen;


import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.Log;
import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.sql.*;

public class LogScreen extends Application
{
    private Connection connection;
    private Statement statement;

    private User user;

    private TableView<Log> logTable;

    private ObservableList<Log> logList;

    public LogScreen(User user)
    {
       this.user = user;

       verifyConnection();
    }



    private void verifyConnection()
    {
        try
        {
            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        logList = FXCollections.observableArrayList();

        initCompenents(stage);

        Scene scene = new Scene(logTable, Constants.Log_SC_X,Constants.Log_SC_Y);

        Methods.initStage("Log",stage,scene,false, Constants.ICON_IMG);
    }

    private void initCompenents(Stage stage)
    {
        logTable = new TableView<Log>();
        logTable.setEditable(true);
        logTable.getStylesheets().add(MainMenu.class.getResource("table_css.css").toExternalForm());

        TableColumn<Log,String> logCol1 = new TableColumn<>("Date");
        TableColumn<Log,String> logCol2 = new TableColumn<>("Situation");


        logTable.getColumns().add(logCol1);
        logTable.getColumns().add(logCol2);


        logCol1.setCellValueFactory(new PropertyValueFactory<Log,String>("date"));
        logCol2.setCellValueFactory(new PropertyValueFactory<Log,String >("verify"));

        logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getLogList();
        listLog();

    }

    public void addLog(boolean valid)
    {

        try
        {
            String query = "CALL addLog("+user.getUserID()+","+valid+");";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    private void getLogList()
    {
        try
        {
            int uID = user.getUserID();

            String listQuery = "CALL getLog("+uID+");";

            ResultSet rset = statement.executeQuery(listQuery);
            logList.clear();

            while(rset.next())
                logList.add(new Log(rset.getString("date"),Boolean.toString(rset.getBoolean("verify"))));
            rset.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    private void listLog()
    {
        logTable.setItems(logList);
    }


    public static void main(String[] args) {

    }
}
