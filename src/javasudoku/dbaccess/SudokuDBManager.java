package javasudoku.dbaccess;

import javasudoku.model.SudokuGameDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javasudoku.model.SudokuGame;
import javasudoku.model.SudokuStringifier;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

/**
 * Initializes and maintains a connection with a database used to store SudokuGame objects.
 * This class realizes the Singleton design pattern, and as such only one SudokuDBManager instance
 * exists at a time. Using the Singleton design pattern allows the implementation of the
 * SudokuGameDAO interface for performing database operations using SudokuGame objects.
 * @author Ishaiah Cross
 */
public class SudokuDBManager implements SudokuGameDAO {
    //Database configuration
    private static final String USER = "SudokuGame";
    private static final String PASS = "pdc";
    private static final String URL = "jdbc:derby:SudokuDB;create=true";
    private static final String TABLE_NAME = "SudokuGames";
    
    //Instance information
    private static SudokuDBManager dbManagerInstance = null;
    private Connection dbConnection;
    
    
    private SudokuDBManager() {
        connectDatabase();
        initializeTable();
    }
    
    public static SudokuDBManager getInstance() {
        if(dbManagerInstance == null) {
            dbManagerInstance = new SudokuDBManager();
        }
        
        return dbManagerInstance;
    }
    
    /**
     * Attempts to connect to the Sudoku database using the configured URL, username, and password.
     */
    private void connectDatabase() {
        if(this.dbConnection == null) {
            try {
                this.dbConnection = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException ex) {
                System.out.println("Failed to set up database connection.");
                System.out.println(ex.getMessage());
                System.out.println(ex.getNextException());
            }
        }
    }
    
    /**
     * Attempts to close the connection to the Sudoku database.
     */
     public void closeConnection() {
        if (dbConnection != null) {
            try {
                //In embedded mode, shutdown the database using shutdown=true
                this.dbConnection.close();
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ex) {
                if(ex.getSQLState().equals("XJ015")) {
                    //Shut down with no errors
                    System.out.println("Closed Derby connection");
                } else {
                    System.out.println("Failed to safely close Derby connection.");
                    System.out.println(ex.getMessage());
                    System.out.println(ex.getNextException());
                }
            }
        }
    }
     
     /**
      * Initializes the table that the SudokuDB needs to store game data
      */
     private void initializeTable() {
         try {
             String updateStr = "CREATE TABLE " + TABLE_NAME + " ("
                     + "UID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                     + "SaveDate VARCHAR(50) NOT NULL,"
                     + "FilledCells FLOAT,"
                     + "PlayerName VARCHAR(50) NOT NULL,"
                     + "GameData VARCHAR(500) NOT NULL,"
                     + "PRIMARY KEY (UID))";
             
             Statement sqlStatement = dbConnection.createStatement();
             sqlStatement.executeUpdate(updateStr);
         } catch (SQLException se) {
             //Error code XOY32 means that the table already existed, so no error needs to be thrown in such a case
             if(!se.getSQLState().equals("X0Y32"))
                 System.out.println("Failed to initialize tables.");
         }
     }
         
     /**
      * Runs an SQL query on the Sudoku database
      * @param sqlString The SQL to run on the database
      * @return A ResultSet object containing any matches for the query, or null if the query failed
      */
    private ResultSet runQuery(String sqlString) {
        ResultSet results = null;
    
        try {
            Statement sqlStatement = dbConnection.createStatement();
            results = sqlStatement.executeQuery(sqlString);
        } catch (SQLException se) {
            System.out.println("Failed to run query: " + sqlString);
        }
        
        return results;
    }
    
    /**
     * Runs an SQL update statement on the Sudoku database. This will modify the database.
     * @param sqlString The SQL to run on the database
     */
    private void runUpdate(String sqlString) {

        try {
            Statement sqlStatement = dbConnection.createStatement();
            sqlStatement.executeUpdate(sqlString);
        } catch (SQLException se) {
            System.out.println("Failed to run update: " + sqlString);
            System.out.println(se.getMessage());
            System.out.println(se.getNextException().getMessage());
        }
    }
    
    /**
     * Retrieves a game from the database according to its UID
     * @param id 
     * @return 
     */
    @Override
    public SudokuGame getGame(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Queries the Sudoku database for SudokuGame objects based on the attached user name
     * @param name The name to search for
     * @return An ArrayList containing the results
     */
    @Override
    public ArrayList<SudokuGame> getGamesByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Adds a SudokuGame to the database
     * @param game The game to add
     */
    @Override
    public void addGame(SudokuGame game) {
        //Get the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime curTime = LocalDateTime.now();  
        String dateAsString = formatter.format(curTime);
        
        //Get the current completion status
        float completion = game.completionPercentage;
        
        //Get the board formatted as a string
        String boardData = SudokuStringifier.boardToString(game.boardState);

        //Create the SQL statement
        String sql = "INSERT INTO " + TABLE_NAME + "(SaveDate, FilledCells, PlayerName, GameData) VALUES ("
                + "'" + dateAsString + "',"
                + "" + completion + ","
                + "'" + game.playerName + "',"
                + "'" + boardData + "')";
        
        runUpdate(sql);
    }

    @Override
    public void removeGame(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGame(int id, SudokuGame newBoard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
