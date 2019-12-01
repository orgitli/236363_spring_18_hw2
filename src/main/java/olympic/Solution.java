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
                    "    athletes_counter INTEGER NOT NULL DEFAULT 0,\n" +
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
                    "    CHECK (medal >= 0 AND medal<4), \n" +
                    "PRIMARY KEY (sport_id, athlete_id)"+
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
                    " ON DELETE CASCADE,\n" +
                    "PRIMARY KEY(athlete_id1, athlete_id2), "+
                    "CHECK (athlete_id1 != athlete_id2)" +
                    ")");
            pstmt.execute();

            //create athlete_country_medal view
            pstmt = connection.prepareStatement("CREATE VIEW country_medal AS \n" +
                                                     "SELECT  athlete.country, athlete.athlete_id \n" +
                                                     "FROM athlete , participate " +
                                                      "WHERE athlete.athlete_id=participate.athlete_id AND participate.medal IS NOT NULL");
            pstmt.execute();

            //create athlete_sport view
            pstmt = connection.prepareStatement("CREATE VIEW athlete_sport AS \n" +
                                                      "SELECT  athlete.athlete_id, participate.sport_id \n" +
                                                      "FROM athlete , participate " +
                                                        "WHERE athlete.athlete_id=participate.athlete_id ");
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
            pstmt = connection.prepareStatement("DROP VIEW  IF EXISTS athlete_sport");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP VIEW  IF EXISTS country_medal");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS participate CASCADE");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS friends");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS athlete");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS sport");
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

    public static Sport getSport(Integer sportId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT * FROM sport" +
                    " WHERE sport_id = ?");
            pstmt.setInt(1,sportId);
            ResultSet res = pstmt.executeQuery();
            Sport sport = new Sport();
            while(res.next()) {
                sport.setId(res.getInt("sport_id"));
                sport.setName(res.getString("sport_name"));
                sport.setCity(res.getString("city"));
                sport.setAthletesCount(res.getInt("athlets_counter"));
            }
            return sport;
        }
        catch(SQLException e){
            return Sport.badSport();
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

    public static ReturnValue deleteSport(Sport sport) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("DELETE from sport" +
                    " WHERE sport_id = ?");
            pstmt.setInt(1,sport.getId());
            pstmt.execute();
        }
        catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.UNIQUE_VIOLATION.getValue())
                return NOT_EXISTS;
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

    public static ReturnValue athleteJoinSport(Integer sportId, Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT active FROM athlete "+
                                                        "WHERE athlete_id=?" );
            pstmt.setInt(1, athleteId);
            ResultSet result = pstmt.executeQuery();
            boolean is_active = false;
            if(result.next())
                is_active = result.getBoolean("active");
            int money;
            if(is_active)
                money = 0;
            else
                money = 100;
            pstmt = connection.prepareStatement("INSERT INTO participate" +
                    "    VALUES (?,?,null ,?)" );
            pstmt.setInt(1, sportId);
            pstmt.setInt(2, athleteId);
            pstmt.setInt(3, money);
            pstmt.execute();
            pstmt = connection.prepareStatement("UPDATE sport " +
                    "SET athletes_counter=athletes_counter+1 " +
                    "WHERE sport_id=?" );
            pstmt.setInt(1, sportId);
            pstmt.executeUpdate();
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.FOREIGN_KEY_VIOLATION.getValue() )
                return NOT_EXISTS;
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

    public static ReturnValue athleteLeftSport(Integer sportId, Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("select active from athlete where athlete_id=? ");
            pstmt.setInt(1, athleteId);
            ResultSet res = pstmt.executeQuery();
            if(!res.next()){
                return NOT_EXISTS;
            }
            boolean active = res.getBoolean("active");
            pstmt = connection.prepareStatement("DELETE FROM participate "+
                    "WHERE athlete_id=? AND sport_id = ?" );
            pstmt.setInt(1, athleteId);
            pstmt.setInt(2, sportId);
            pstmt.executeUpdate();
            if(active){
                pstmt = connection.prepareStatement("UPDATE sport " +
                        "SET athletes_counter=athletes_counter-1 " +
                        "WHERE sport_id=?" );
                pstmt.setInt(1, sportId);
                pstmt.executeUpdate();
            }

        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.FOREIGN_KEY_VIOLATION.getValue() || Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.UNIQUE_VIOLATION.getValue())
                return NOT_EXISTS;
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

    public static ReturnValue confirmStanding(Integer sportId, Integer athleteId, Integer place) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("UPDATE participate" +
                    "    SET medal=?" +
                    "   WHERE athlete_id = ? AND sport_id = ?");
            pstmt.setInt(1, place);
            pstmt.setInt(2, athleteId);
            pstmt.setInt(3,sportId);
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows == 0)
                return NOT_EXISTS;
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.FOREIGN_KEY_VIOLATION.getValue())
                return NOT_EXISTS;
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.CHECK_VIOLATION.getValue() || Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.NOT_NULL_VIOLATION.getValue())
                return BAD_PARAMS;
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

    public static ReturnValue athleteDisqualified(Integer sportId, Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("UPDATE participate" +
                    "    SET medal=null" +
                    "   WHERE athlete_id = ? AND sport_id = ?");
            pstmt.setInt(1, athleteId);
            pstmt.setInt(2,sportId);
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows == 0)
                return NOT_EXISTS;
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.FOREIGN_KEY_VIOLATION.getValue())
                return NOT_EXISTS;
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

    public static ReturnValue makeFriends(Integer athleteId1, Integer athleteId2) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            //check if symetric friendship exists
            pstmt = connection.prepareStatement("SELECT FROM friends" +
                    "    WHERE athlete_id1=? AND athlete_id2=?" );
            pstmt.setInt(1, athleteId2);
            pstmt.setInt(2, athleteId1);
            ResultSet results = pstmt.executeQuery();
            if(results.next())
                return ALREADY_EXISTS;
            ////////////////////////////////////////////////////////////

            pstmt = connection.prepareStatement("INSERT INTO friends" +
                    "    VALUES (?,?)" );
            pstmt.setInt(1, athleteId1);
            pstmt.setInt(2, athleteId2);
            pstmt.execute();
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.CHECK_VIOLATION.getValue() )
                return BAD_PARAMS;
            if( Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.FOREIGN_KEY_VIOLATION.getValue())
                return NOT_EXISTS;
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

    public static ReturnValue removeFriendship(Integer athleteId1, Integer athleteId2) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            int r1 = 1;
            int r2 = 1;
            //check if symetric friendship exists
            pstmt = connection.prepareStatement("SELECT FROM friends" +
                    "    WHERE athlete_id1=? AND athlete_id2=?" );
            pstmt.setInt(1, athleteId2);
            pstmt.setInt(2, athleteId1);
            ResultSet results = pstmt.executeQuery();
            if(!results.next())
                r1 = 0;
            pstmt = connection.prepareStatement("SELECT FROM friends" +
                    "    WHERE athlete_id1=? AND athlete_id2=?" );
            pstmt.setInt(1, athleteId1);
            pstmt.setInt(2, athleteId2);
            results = pstmt.executeQuery();
            if(!results.next())
                r2 = 0;
            if(r1+r2==0){
                return NOT_EXISTS;
            }
            ////////////////////////////////////////////////////////////

            pstmt = connection.prepareStatement("DELETE FROM friends" +
                    "  WHERE athlete_id1=? and athlete_id2=?" );
            if(r1==1){
                pstmt.setInt(1, athleteId2);
                pstmt.setInt(2, athleteId1);
            }
            if(r2==1){
                pstmt.setInt(1, athleteId1);
                pstmt.setInt(2, athleteId2);
            }

            pstmt.executeUpdate();

        }catch(SQLException e){


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

    public static ReturnValue changePayment(Integer athleteId, Integer sportId, Integer payment) {
        if(payment<0){
            return BAD_PARAMS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{

            // check if athlete
            pstmt = connection.prepareStatement("SELECT active FROM athlete" +
                    "    WHERE athlete_id=?" );
            pstmt.setInt(1, athleteId);

            ResultSet results = pstmt.executeQuery();
            if(!results.next()){
                return NOT_EXISTS;
            }
            if(results.next()==true) {
                return NOT_EXISTS;
            }


            pstmt = connection.prepareStatement("SELECT * FROM participate" +
                    "    WHERE sport_id=? AND athlete_id=?" );
            pstmt.setInt(1, sportId);
            pstmt.setInt(2, athleteId);
            results = pstmt.executeQuery();
            if(!results.next()){
                return NOT_EXISTS;
            }
            pstmt = connection.prepareStatement("UPDATE participate" +
                    " SET payment=?   WHERE sport_id=? AND athlete_id=?" );
            pstmt.setInt(1, payment);
            pstmt.setInt(2, sportId);
            pstmt.setInt(3, athleteId);



            pstmt.executeUpdate();

        }catch(SQLException e){


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

    public static Boolean isAthletePopular(Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            /*
            pstmt = connection.prepareStatement( "SELECT sport_id FROM athlete_sport " +
                                                      "WHERE athlete_id=? AND " +
                                                     "sport_id NOT IN(SELECT sport_id FROM (SELECT athlete_id1 FROM friends " +
                                                     "WHERE athlete_id2=? " +
                                                     "UNION ALL " +
                                                     "SELECT athlete_id2 FROM friends " +
                                                     "WHERE athlete_id2=? ) AS friends_sports )" +
                                                      "" );

             */
            pstmt = connection.prepareStatement("SELECT sport_id FROM ((SELECT athlete_id1 AS athlete_id FROM friends " +
                    "WHERE athlete_id2=? " +
                    "UNION ALL " +
                    "SELECT athlete_id2 AS athlete_id FROM friends " +
                    "WHERE athlete_id1=? ) AS athletesFriends " +
                    "INNER JOIN participate ON participate.athlete_id = athletesFriends.athlete_id )  " +
                    "WHERE sport_id NOT IN ( SELECT sport_id FROM  athlete_sport " +
                    "WHERE athlete_id=? )  " );
            pstmt.setInt(1, athleteId);
            pstmt.setInt(2, athleteId);
            pstmt.setInt(3, athleteId);
            ResultSet results = pstmt.executeQuery();
            if(results.next())
                return false;
        }catch(SQLException e){
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
        return true;
    }

    public static Integer getTotalNumberOfMedalsFromCountry(String country) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT FROM country_medal" +
                    "    WHERE country=? " );
            pstmt.setString(1, country);
            ResultSet results = pstmt.executeQuery();
            int medals_counter = 0;
            while(results.next())
                medals_counter++;
            return medals_counter;
        }catch(SQLException e){
            return 0;
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



    public static Integer getIncomeFromSport(Integer sportId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{

            // check if athlete
            pstmt = connection.prepareStatement("SELECT * FROM sport" +
                    "    WHERE sport_id=?" );
            pstmt.setInt(1, sportId);

            ResultSet results = pstmt.executeQuery();
            if(!results.next()){
                return 0;
            }



            pstmt = connection.prepareStatement("select sum(participate.payment) from athlete, participate\n" +
                    "where athlete.athlete_id=participate.athlete_id and\n" +
                    "athlete.active=false and \n" +
                    "participate.sport_id=?");
            pstmt.setInt(1, sportId);

            results = pstmt.executeQuery();
            if(!results.next()){
                return 0;
            }else{
                return results.getInt("sum");
            }



        }catch(SQLException e){

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
        return 0;
    }

    public static String getBestCountry() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT country, COUNT(athlete_id) AS medals FROM country_medal" +
                                                "    GROUP BY country " +
                                                       " ORDER BY medals DESC, country ASC " );
            ResultSet results = pstmt.executeQuery();
            if(results.next())
                return results.getString("country");

        }catch(SQLException e){
            return null;
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
        return "";
    }

    public static String getMostPopularCity() {
        return "";
    }

    public static ArrayList<Integer> getAthleteMedals(Integer athleteId) {
        ArrayList<Integer> medals = new ArrayList<>();
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT  COUNT(*) AS gold " +
                    "FROM participate " +
                    "WHERE athlete_id=? AND medal=1 " );
            pstmt.setInt(1, athleteId);
            ResultSet results = pstmt.executeQuery();
            if(results.next())
                medals.add(results.getInt("gold"));

            pstmt = connection.prepareStatement("SELECT  COUNT(*) AS silver " +
                    "FROM participate " +
                    "WHERE athlete_id=? AND medal=2 " );
            pstmt.setInt(1, athleteId);
            results = pstmt.executeQuery();
            if(results.next())
                medals.add(results.getInt("silver"));

            pstmt = connection.prepareStatement("SELECT  COUNT(*) AS bronze " +
                    "FROM participate " +
                    "WHERE athlete_id=? AND medal=3 " );
            pstmt.setInt(1, athleteId);
            results = pstmt.executeQuery();
            if(results.next())
                medals.add(results.getInt("bronze"));

            return medals;

        }catch(SQLException e){
            e.printStackTrace();
            medals.add(0,0);
            medals.add(1,0);
            medals.add(2,0);
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

