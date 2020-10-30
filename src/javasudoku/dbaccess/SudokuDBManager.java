package javasudoku.dbaccess;

import javasudoku.model.SudokuGameDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javasudoku.model.SudokuGame;
import javasudoku.model.SudokuStringifier;

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
    public static final int MAX_NAME_LENGTH = 15;
    
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
             PreparedStatement sql = dbConnection.prepareStatement("CREATE TABLE " + TABLE_NAME + " ("
                     + "UID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                     + "SaveDate VARCHAR(50) NOT NULL,"
                     + "FilledCells FLOAT,"
                     + "PlayerName VARCHAR(" + MAX_NAME_LENGTH + ") NOT NULL,"
                     + "GameData VARCHAR(500) NOT NULL,"
                     + "PRIMARY KEY (UID))");
             
             sql.executeUpdate();
         } catch (SQLException se) {
             //Error code XOY32 means that the table already existed, so no error needs to be thrown in such a case
             if(!se.getSQLState().equals("X0Y32")) {
                 System.out.println("Failed to initialize tables.");
                 System.out.println(se.getMessage());
             }
         }
     }
         
     /**
      * Runs an SQL query on the Sudoku database
      * @param statement The SQL to run on the database
      * @return A ResultSet object containing any matches for the query, or null if the query failed
      */
    private ResultSet runQuery(PreparedStatement statement) {
        ResultSet results = null;
    
        try {
            results = statement.executeQuery();
        } catch (SQLException se) {
            System.out.println("Failed to run query: " + statement);
        }
        
        return results;
    }
    
    /**
     * Gets a list of all of the unique user names stored in the database
     * @return An ArrayList containing all of the unique user names stored in the database
     */
    @Override
    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<>();
        
        try {
            //Create the SQL statement
            PreparedStatement sql = dbConnection.prepareStatement("SELECT DISTINCT PlayerName FROM " + TABLE_NAME);
            ResultSet query = sql.executeQuery();
            
            //Process the results to get all of the player names
            while(query.next()) {
                String name = query.getString("PlayerName");
                names.add(name);
            }
            
        } catch(SQLException SE) {
            System.out.println("Failed to retrieve column PlayerName from the database");
        }
        
        return names;
    }
    
    /**
     * Retrieves a game from the database according to its UID
     * @param id The ID to search for in the database
     * @return a SudokuGame object representing the game that was found, or null if none was found
     */
    @Override
    public SudokuGame getGame(int id) {
        SudokuGame result = null;
        
        try {
            //Create the SQL statement
            PreparedStatement sql = dbConnection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE UID = ?");
            sql.setInt(1, id);

            ResultSet query = sql.executeQuery();
            
            //Process the results to initialize a SudokuGame object
            if(query.next()) {
                String saveDate = query.getString("SaveDate");
                String playerName = query.getString("PlayerName");
                String boardData = query.getString("GameData");
                
                //Create the object by parsing the raw board data and setting the date manually
                result = new SudokuGame(playerName, SudokuStringifier.parseBoard(boardData));
                result.setDate(saveDate);
            } else {
                //Throw an exception if there was no game with the provided ID
                throw new SQLException();
            }
            
        } catch (SQLException se) {
            System.out.println("Failed to remove game with UID " + id);
            System.out.println(se);
        }
        
        return result;
    }
    
    /**
     * Queries the Sudoku database for SudokuGame objects based on the attached user name
     * @param name The name to search for
     * @return An ArrayList containing the results
     */
    @Override
    public ArrayList<SudokuGame> getGamesByName(String name) {
        ArrayList<SudokuGame> results = new ArrayList<>();
        
        try {
            //Create the SQL statement
            PreparedStatement sql = dbConnection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE PlayerName LIKE ?");
            sql.setString(1, name);

            ResultSet query = sql.executeQuery();
            
            //Iterate over all of the results
            while(query.next()) {
                Integer gameID = query.getInt("UID");
                String saveDate = query.getString("SaveDate");
                String playerName = query.getString("PlayerName");
                String boardData = query.getString("GameData");
                
                //Create the object by parsing the raw board data and setting the date manually
                SudokuGame resultGame = new SudokuGame(playerName, SudokuStringifier.parseBoard(boardData));
                resultGame.setDate(saveDate);
                resultGame.setGameID(gameID);
                
                //Add the result to the arraylist
                results.add(resultGame);
            }
            
        } catch (SQLException se) {
            System.out.println("Failed to run query when supplied name " + name);
            System.out.println(se);
        }
        
        return results;
    }
    
    /**
     * Adds a SudokuGame to the database
     * @param game The game to add
     */
    @Override
    public void addGame(SudokuGame game) {
        //Get the game, completion status, and the board data
        String dateAsString = game.lastPlayedDate;
        float completion = game.completionPercentage;
        String boardData = SudokuStringifier.boardToString(game.boardState);

        //Create the SQL statement
        try {
 
            PreparedStatement sql = dbConnection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (SaveDate, FilledCells, PlayerName, GameData) VALUES ("
                        + "?,?,?,?)");

            //Append the parameters
            sql.setString(1, dateAsString);
            sql.setFloat(2, completion);
            sql.setString(3, game.playerName);
            sql.setString(4, boardData);

            //Run the update
            sql.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Failed to add game to table");
            System.out.println(se);
        }
    }
    
    /**
     * Removes an existing game from the database
     * @param id The ID of the game to remove
     */
    @Override
    public void removeGame(int id) {
        try {
            //Create the SQL statement
            PreparedStatement sql = dbConnection.prepareStatement(
                    "DELETE FROM " + TABLE_NAME + " WHERE UID = ?");
            sql.setInt(1, id);

            sql.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Failed to remove game with UID " + id);
            System.out.println(se);
        }
    }
}
