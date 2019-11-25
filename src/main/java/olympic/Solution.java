package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;
import olympic.data.DBConnector;
import olympic.data.PostgreSQLErrorCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static olympic.business.ReturnValue.*;

public class Solution {
    public static void createTables() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            //create athlet
            pstmt = connection.prepareStatement("CREATE TABLE athlete\n" +
                    "(\n" +
                    "    athlete_id integer NOT NULL,\n" +
                    "    athlete_name VARCHAR(255) NOT NULL,\n" +
                    "    country VARCHAR(255) NOT NULL,\n" +
                    "    active bool NOT NULL,\n" +
                    "    PRIMARY KEY (athlete_id),\n" +
                    "    CHECK (athlete_id > 0)\n" +
                    ")");
            pstmt.execute();

            //create sport
            pstmt = connection.prepareStatement("CREATE TABLE sport\n" +
                    "(\n" +
                    "    sport_id INTEGER NOT NULL,\n" +
                    "    sport_name VARCHAR(255) NOT NULL,\n" +
                    "    city VARCHAR(255) NOT NULL,\n" +
                    "    athlets_counter INTEGER NOT NULL DEFAULT 0,\n" +
                    "    PRIMARY KEY (sport_id),\n" +
                    "    CHECK (sport_id > 0)\n" +
                    ")");
            pstmt.execute();

            //create participate
            pstmt = connection.prepareStatement("CREATE TABLE participate\n" +
                    "(\n" +
                    "    sport_id INTEGER NOT NULL ,\n" +
                    "    athlete_id INTEGER NOT NULL,\n" +
                    "    medal INTEGER ,\n" +
                    "    payment INTEGER NOT NULL DEFAULT 0,\n" +
                    "   FOREIGN KEY (sport_id)\n" +
                    " REFERENCES sport(sport_id)\n" +
                    " ON DELETE CASCADE,\n" +
                    "   FOREIGN KEY (athlete_id)\n" +
                    " REFERENCES athlete(athlete_id)\n" +
                    " ON DELETE CASCADE,\n" +
                    "    CHECK (medal > 0 AND medal<4) \n" +
                    ")");
            pstmt.execute();

            //create friends
            pstmt = connection.prepareStatement("CREATE TABLE friends\n" +
                    "(\n" +
                    "    athlete_id1 INTEGER NOT NULL ,\n" +
                    "    athlete_id2 INTEGER NOT NULL,\n" +
                    "   FOREIGN KEY (athlete_id1)\n" +
                    " REFERENCES athlete(athlete_id)\n" +
                    " ON DELETE CASCADE,\n" +
                    "   FOREIGN KEY (athlete_id2)\n" +
                    " REFERENCES athlete(athlete_id)\n" +
                    " ON DELETE CASCADE\n" +
                    ")");
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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

    public static void clearTables() { }

    public static void dropTables() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS participate");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS friends");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS athlete");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS sport");
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

    public static ReturnValue addAthlete(Athlete athlete) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("INSERT INTO athlete" +
                    "    VALUES (?,?,?,?)" );
            pstmt.setInt(1, athlete.getId());
            pstmt.setString(2, athlete.getName());
            pstmt.setString(3,athlete.getCountry());
            pstmt.setBoolean(4, athlete.getIsActive());
            pstmt.execute();
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.CHECK_VIOLATION.getValue() || Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.NOT_NULL_VIOLATION.getValue())
                return BAD_PARAMS;
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.UNIQUE_VIOLATION.getValue())
                return ALREADY_EXISTS;
            return ERROR;
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

        return OK;
    }

    public static Athlete getAthleteProfile(Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT * FROM athlete" +
                    " WHERE athlete_id = ?");
            pstmt.setInt(1,athleteId);
            ResultSet res = pstmt.executeQuery();
            Athlete athlete = new Athlete();
            while(res.next()) {
                athlete.setId(res.getInt("athlete_id"));
                athlete.setName(res.getString("athlete_name"));
                athlete.setCountry(res.getString("country"));
                athlete.setIsActive(res.getBoolean("active"));
            }
            return athlete;
        }
        catch(SQLException e){
            return Athlete.badAthlete();
        }
        finally{
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

    public static ReturnValue deleteAthlete(Athlete athlete) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("DELETE FROM athlete" +
                    " WHERE athlete_id = ? AND athlete_name = ? AND country = ? AND active =?");
            pstmt.setInt(1,athlete.getId());
            pstmt.setString(2, athlete.getName());
            pstmt.setString(3, athlete.getCountry());
            pstmt.setBoolean(4, athlete.getIsActive());
            pstmt.executeUpdate();
        }
        catch(SQLException e){
            if (Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.UNIQUE_VIOLATION.getValue())
                return NOT_EXISTS;
            e.printStackTrace();
            return ERROR;
        }
        finally{
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
        return OK;
    }

    public static ReturnValue addSport(Sport sport) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("INSERT INTO sport" +
                    "    VALUES (?,?,?)" );
            pstmt.setInt(1, sport.getId());
            pstmt.setString(2, sport.getName());
            pstmt.setString(3,sport.getCity());
            pstmt.execute();
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.CHECK_VIOLATION.getValue() || Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.NOT_NULL_VIOLATION.getValue())
                return BAD_PARAMS;
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.UNIQUE_VIOLATION.getValue())
                return ALREADY_EXISTS;
            return ERROR;
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
        return OK;
    }

    public static Sport getSport(Integer sportId) { return Sport.badSport(); }

    public static ReturnValue deleteSport(Sport sport) {
        return OK;
    }

    public static ReturnValue athleteJoinSport(Integer sportId, Integer athleteId) {
        return OK;
    }

    public static ReturnValue athleteLeftSport(Integer sportId, Integer athleteId) {
        return OK;
    }

    public static ReturnValue confirmStanding(Integer sportId, Integer athleteId, Integer place) {
        return OK;
    }

    public static ReturnValue athleteDisqualified(Integer sportId, Integer athleteId) {
        return OK;
    }

    public static ReturnValue makeFriends(Integer athleteId1, Integer athleteId2) {
        return OK;
    }

    public static ReturnValue removeFriendship(Integer athleteId1, Integer athleteId2) {
        return OK;
    }

    public static ReturnValue changePayment(Integer athleteId, Integer sportId, Integer payment) {
        return OK;
    }

    public static Boolean isAthletePopular(Integer athleteId) {
        return true;
    }

    public static Integer getTotalNumberOfMedalsFromCountry(String country) {
        return 0;
    }

    public static Integer getIncomeFromSport(Integer sportId) {
        return 0;
    }

    public static String getBestCountry() {
        return "";
    }

    public static String getMostPopularCity() {
        return "";
    }

    public static ArrayList<Integer> getAthleteMedals(Integer athleteId) {
        return new ArrayList<>();
    }

    public static ArrayList<Integer> getMostRatedAthletes() {
        return new ArrayList<>();
    }

    public static ArrayList<Integer> getCloseAthletes(Integer athleteId) {
        return new ArrayList<>();
    }

    public static ArrayList<Integer> getSportsRecommendation(Integer athleteId) {
        return new ArrayList<>();
    }
}

