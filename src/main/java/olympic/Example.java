package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;
import olympic.data.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class Example {

    public static void main(String[] args) {

        /*
        Solution sol=new Solution();
        sol.dropTables();
        sol.createTables();
        Athlete athlete = new Athlete();
        athlete.setId(30);
        athlete.setName("Or");
        athlete.setCountry("Israel");
        athlete.setIsActive(true);
        sol.addAthlete(athlete);

        athlete.setId(7);
        if(sol.addAthlete(athlete) == ReturnValue.BAD_PARAMS) System.out.println("OK\n");

        Athlete a = sol.getAthleteProfile(7) ;
        System.out.println(a.getId());


        Sport sport = new Sport();
        sport.setId(-1);
        sport.setName("football");
        sport.setCity("Haifa");
        if(sol.addSport(sport) == ReturnValue.BAD_PARAMS) System.out.println("Sport added\n");
        sport.setId(1);
        sol.addSport(sport);
        System.out.println(sol.athleteJoinSport(1,7));
        System.out.println(sol.athleteJoinSport(1,7));
        System.out.println(sol.athleteLeftSport(1,7));
        System.out.println(sol.athleteLeftSport(1,7));
*/
        /*
        //check athleteJoinSport/ athleteLeftSport
        Solution sol=new Solution();
        sol.dropTables();
        sol.createTables();
        Athlete athlete = new Athlete();
        athlete.setId(30);
        athlete.setName("Or");
        athlete.setCountry("Israel");
        athlete.setIsActive(false);
        assertEquals(sol.addAthlete(athlete), ReturnValue.OK);

        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("football");
        sport.setCity("Haifa");
        assertEquals(sol.addSport(sport), ReturnValue.OK);

        assertEquals(sol.athleteJoinSport(2,30), ReturnValue.NOT_EXISTS);
        assertEquals(sol.athleteJoinSport(1,2), ReturnValue.NOT_EXISTS);

        assertEquals(sol.athleteJoinSport(1,30), ReturnValue.OK);
        assertEquals(sol.athleteJoinSport(1,30), ReturnValue.ALREADY_EXISTS);
*/

        /*
        //check confirmStandings and athleteDisqualified
        Solution sol=new Solution();
        sol.dropTables();
        sol.createTables();
        Athlete athlete = new Athlete();
        athlete.setId(30);
        athlete.setName("Or");
        athlete.setCountry("Israel");
        athlete.setIsActive(false);
        sol.addAthlete(athlete);

        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("football");
        sport.setCity("Haifa");

        sol.addSport(sport);
        sol.athleteJoinSport(1,30);
        assertEquals(sol.confirmStanding(1,30, 4), ReturnValue.BAD_PARAMS);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 3), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 1), ReturnValue.OK);
        assertEquals( sol.confirmStanding(2,30, 2), ReturnValue.NOT_EXISTS);
        assertEquals( sol.confirmStanding(1,3, 2), ReturnValue.NOT_EXISTS);

        assertEquals(sol.athleteDisqualified(1,30), ReturnValue.OK);
        assertEquals(sol.athleteDisqualified(1,1), ReturnValue.NOT_EXISTS);
        assertEquals(sol.athleteDisqualified(2,30), ReturnValue.NOT_EXISTS);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);
        */

        //check makeFriends and removeFriendship
        Solution sol=new Solution();
        sol.dropTables();
        sol.createTables();

        Athlete athlete = new Athlete();
        athlete.setId(30);
        athlete.setName("Or");
        athlete.setCountry("Israel");
        athlete.setIsActive(false);
        sol.addAthlete(athlete);

        Athlete athlete2 = new Athlete();
        athlete.setId(31);
        athlete.setName("Or");
        athlete.setCountry("Israel");
        athlete.setIsActive(false);
        sol.addAthlete(athlete);

        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("football");
        sport.setCity("Haifa");

        sol.addSport(sport);
        sol.athleteJoinSport(1,30);
        assertEquals(sol.confirmStanding(1,30, 4), ReturnValue.BAD_PARAMS);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 3), ReturnValue.OK);
        assertEquals( sol.confirmStanding(1,30, 1), ReturnValue.OK);
        assertEquals( sol.confirmStanding(2,30, 2), ReturnValue.NOT_EXISTS);
        assertEquals( sol.confirmStanding(1,3, 2), ReturnValue.NOT_EXISTS);

        assertEquals(sol.athleteDisqualified(1,30), ReturnValue.OK);
        assertEquals(sol.athleteDisqualified(1,1), ReturnValue.NOT_EXISTS);
        assertEquals(sol.athleteDisqualified(2,30), ReturnValue.NOT_EXISTS);
        assertEquals( sol.confirmStanding(1,30, 2), ReturnValue.OK);

        assertEquals(sol.makeFriends(1,1), ReturnValue.BAD_PARAMS);
        assertEquals(sol.makeFriends(30,1), ReturnValue.NOT_EXISTS);
        assertEquals(sol.makeFriends(1,30), ReturnValue.NOT_EXISTS);

        assertEquals(sol.makeFriends(31,30), ReturnValue.OK);
        assertEquals(sol.makeFriends(31,30), ReturnValue.ALREADY_EXISTS);
        assertEquals(sol.makeFriends(30,31), ReturnValue.ALREADY_EXISTS);

/*
        javaStringExample();
        arrayListExample();
        dropTable();
        System.out.println("Creating hello_world Table");
        createTable();
        selectFromTable();
        System.out.println("inserting main.data into table");
        insertIntoTable();
        selectFromTable();
        System.out.println("updating main.data in table");
        updateTable();
        selectFromTable();
        System.out.println("deleting main.data from table");
        deleteFromTable();
        selectFromTable();
        dropTable();
*/
    }

    private static void deleteFromTable() {
        //Hello greetings.
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(
                    "DELETE FROM hello_world " +

                            "where id = ?");
            pstmt.setInt(1,1);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("deleted " + affectedRows + " rows");
        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
    }

    private static void javaStringExample() {
        System.out.println("in order to concat two strings you can use the + operator, for example:");
        System.out.println("    String hello = \"hello\";\n" +
                "   String world = \" world\";\n" +
                "   System.out.println(hello + world);");
        System.out.println("will yield:");
        String hello = "hello";
        String world = " world";
        System.out.println(hello + world);
        System.out.println("you can concat any object to a string, for example, an integer:");
        System.out.println("    System.out.println(hello + world + 1);");
        System.out.println("will yield:");
        System.out.println(hello + world + 1);
        System.out.println("good luck!");
        System.out.println();
    }

    private static void arrayListExample() {
        System.out.println("in order to create a new arraylist, for example, an arraylist of Integers you need to call");
        System.out.println("    ArrayList<Integer> myArrayList = new ArrayList<>();");
        ArrayList<Integer> myArrayList = new ArrayList<>();
        System.out.println("This is how it looks like: " +myArrayList);
        System.out.println("in order to add an item to the arraylist you need to call the add function");
        System.out.println("    myArrayList.add(1);");
        myArrayList.add(1);
        System.out.println("This is how it looks like: " +myArrayList);
        System.out.println("calling myArrayList.add(2); \nwill yield: ");
        myArrayList.add(2);
        System.out.println(myArrayList);
        System.out.println("please note that arraylist keeps insertion order, and allows duplications");
        System.out.println("calling\n   myArrayList.add(2); \nwill yield: ");
        myArrayList.add(2);
        System.out.println(myArrayList);
        System.out.println("in order to get a value from an array list you need to use the function\n get(int index) ");
        System.out.println("recall that array start with 0 :)");
        System.out.println("    int i = myArrayList.get(0);\nwill yield:");
        int i = myArrayList.get(0);
        System.out.println("    i = " + i );
        System.out.println("good luck!");
        System.out.println();

    }

    private static void updateTable() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(
                    "UPDATE hello_world " +
                            "SET short_text = ? " +
                            "where id = ?");
            pstmt.setInt(2,1);
            pstmt.setString(1, "hi world!");
            int affectedRows = pstmt.executeUpdate();
            System.out.println("changed " + affectedRows + " rows");
        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
    }

    private static void dropTable()
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS participate");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS friends");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS sport");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS athlete");
            pstmt.execute();
        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
    }

    private static void createTable()
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {

            pstmt = connection.prepareStatement("CREATE TABLE hello_world\n" +
                    "(\n" +
                    "    id integer,\n" +
                    "    short_text text ,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    CHECK (id > 0)\n" +
                    ")");
            pstmt.execute();
        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }

    }

    private static void insertIntoTable()
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO hello_world" +
                    " VALUES (?, ?), (?, ?)");
            pstmt.setInt(1,1);
            pstmt.setString(2, "hello world!");
            pstmt.setInt(3,2);
            pstmt.setString(4,"goodbye world!");


            pstmt.execute();

        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
    }

    private static void selectFromTable()
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("SELECT * FROM hello_world");
            ResultSet results = pstmt.executeQuery();
            DBConnector.printResults(results);
            results.close();

        } catch (SQLException e) {
            //e.printStackTrace()();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
    }

}
