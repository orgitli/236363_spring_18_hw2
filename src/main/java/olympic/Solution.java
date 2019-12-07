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
                    "    athlete_name TEXT NOT NULL,\n" +
                    "    country TEXT NOT NULL,\n" +
                    "    active bool NOT NULL,\n" +
                    "    PRIMARY KEY (athlete_id),\n" +
                    "    CHECK (athlete_id > 0)\n" +
                    ")");
            pstmt.execute();

            //create sport
            pstmt = connection.prepareStatement("CREATE TABLE sport\n" +
                    "(\n" +
                    "    sport_id INTEGER NOT NULL,\n" +
                    "    sport_name TEXT NOT NULL,\n" +
                    "    city TEXT NOT NULL,\n" +
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
                    "    active bool NOT NULL, " +
                    "    payment INTEGER NOT NULL DEFAULT 0,\n" +
                    "   FOREIGN KEY (sport_id)\n" +
                    " REFERENCES sport(sport_id)\n" +
                    " ON DELETE CASCADE,\n" +
                    "   FOREIGN KEY (athlete_id)\n" +
                    " REFERENCES athlete(athlete_id)\n" +
                    " ON DELETE CASCADE,\n" +
                    "    CHECK (medal > 0 AND medal<4), \n" +
                    "CHECK (payment>=0), "+
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
            pstmt = connection.prepareStatement("CREATE VIEW country_medals AS " +
                    "SELECT COUNT(g1.athlete_id) AS medals, g2.country FROM " +
                    "(SELECT participate.athlete_id FROM participate WHERE medal IS NOT NULL) g1 " +
                    "INNER JOIN " +
                    "(SELECT athlete_id, country FROM athlete) g2 ON g1.athlete_id=g2.athlete_id " +
                    "GROUP BY g2.country " +
                    "ORDER BY COUNT(g1.athlete_id) DESC, g2.country ASC");
            pstmt.execute();

            //create athlete_sport view
            pstmt = connection.prepareStatement("CREATE VIEW athlete_sport AS \n" +
                                                      "SELECT  athlete.athlete_id, participate.sport_id \n" +
                                                      "FROM athlete , participate " +
                                                        "WHERE athlete.athlete_id=participate.athlete_id ");
            pstmt.execute();

            //crate view athlete_medals
            pstmt = connection.prepareStatement("CREATE VIEW athlete_medals AS " +
                    "select athlete.athlete_id, COALESCE(g1.gold, 0) AS gold, COALESCE(g2.silver,0) AS silver, COALESCE(g3.bronze,0) AS bronze, COALESCE(g1.gold, 0)*3+COALESCE(g2.silver,0)*2+COALESCE(g3.bronze,0) AS rating  from " +
                    "((SELECT athlete_id FROM athlete) as athlete " +
                    "FULL JOIN " +
                    "(SELECT athlete_id, COUNT(medal) AS gold " +
                    "FROM participate\n" +
                    "WHERE medal=1 \n" +
                    "GROUP BY athlete_id) g1 ON (g1.athlete_id=athlete.athlete_id) " +
                    "FULL JOIN\n" +
                    "(SELECT athlete_id, COUNT(medal)  AS silver " +
                    "FROM participate " +
                    "WHERE medal=2 " +
                    "GROUP BY athlete_id) g2 ON (athlete.athlete_id=g2.athlete_id) " +
                    "FULL JOIN " +
                    "(SELECT athlete_id, COUNT(medal)  AS bronze " +
                    "FROM participate " +
                    "WHERE medal=3 " +
                    "GROUP BY athlete_id) g3 ON (athlete.athlete_id=g3.athlete_id)) " +
                    "ORDER BY rating DESC, athlete_id ASC");
            pstmt.execute();

            pstmt = connection.prepareStatement("CREATE VIEW popular_city AS " +
                                                    "SELECT pop.city FROM "+
                                                     "(select t1.city, num_athletes, num_sports, num_athletes/num_sports as popular_city from " +
                                                     " (select city,sum(athletes_counter) as num_athletes from sport group by city) t1 " +
                                                     "inner join" +
                                                    "(select city,count(sport_id) as num_sports from sport group by city) t2 "+
                                                    "on t1.city = t2.city order by popular_city desc, city desc) AS pop ");

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

    public static void clearTables() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            //create athlet
            pstmt = connection.prepareStatement("DELETE FROM sport");
            pstmt.execute();

            //create sport
            pstmt = connection.prepareStatement("DELETE FROM athlete");
            pstmt.execute();

            //create participate
            pstmt = connection.prepareStatement("DELETE FROM participate");
            pstmt.execute();

            //create friends
            pstmt = connection.prepareStatement("DELETE FROM friends");
            pstmt.execute();

        } catch (SQLException e) {
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
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS friends CASCADE");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS athlete CASCADE");
            pstmt.execute();
            pstmt = connection.prepareStatement("DROP TABLE IF EXISTS sport CASCADE");
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
                    " WHERE athlete_id = ? ");
            pstmt.setInt(1,athlete.getId());
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
                sport.setAthletesCount(res.getInt("athletes_counter"));
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
                    "    VALUES (?,?,null,? ,?)" );
            pstmt.setInt(1, sportId);
            pstmt.setInt(2, athleteId);
            pstmt.setBoolean(3, is_active);
            pstmt.setInt(4, money);
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
            if(pstmt.executeUpdate()<1)
                return NOT_EXISTS;
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
                    "   WHERE athlete_id = ? AND sport_id = ? AND active = true");
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
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = pstmt = connection.prepareStatement("SELECT * FROM participate " +
                    "WHERE athlete_id=? AND sport_id=? AND active=false ");
            pstmt.setInt(1,athleteId);
            pstmt.setInt(2, sportId);
            ResultSet res = pstmt.executeQuery();
            if(!res.next())
                return NOT_EXISTS;
            pstmt = connection.prepareStatement("UPDATE participate " +
                    "SET payment=? " +
                    "    WHERE sport_id=? AND athlete_id=? AND active=false" );
            pstmt.setInt(1, payment);
            pstmt.setInt(2, sportId);
            pstmt.setInt(3, athleteId);
            if(pstmt.executeUpdate()<1){
                return NOT_EXISTS;
            }
        }catch(SQLException e){
            if(Integer.valueOf(e.getSQLState()) == PostgreSQLErrorCodes.CHECK_VIOLATION.getValue())
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

    public static Boolean isAthletePopular(Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT * FROM athlete WHERE athlete_id=?");
            pstmt.setInt(1,athleteId);
            ResultSet res = pstmt.executeQuery();
            if(!res.next())
                return false;


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
            return false;
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
            pstmt = connection.prepareStatement("SELECT medals FROM country_medals" +
                    "    WHERE country=? " );
            pstmt.setString(1, country);
            ResultSet results = pstmt.executeQuery();
            int medals = 0;
            if(results.next())
                medals = results.getInt("medals");
            return medals;
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
            pstmt = connection.prepareStatement("SELECT country FROM country_medals LIMIT 1 " );
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
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT city FROM popular_city LIMIT 1 " );
            ResultSet results = pstmt.executeQuery();
            if(results.next())
                return results.getString("city");

        }catch(SQLException e){
           return "";
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

    public static ArrayList<Integer> getAthleteMedals(Integer athleteId) {
        ArrayList<Integer> medals = new ArrayList<>();
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT gold, silver, bronze FROM athlete_medals WHERE athlete_id=?" );
            pstmt.setInt(1, athleteId);
            ResultSet results = pstmt.executeQuery();
            if(results.next()) {
                medals.add(results.getInt("gold"));
                medals.add(results.getInt("silver"));
                medals.add(results.getInt("bronze"));
            }
            else{
                medals.add(0);
                medals.add(0);
                medals.add(0);
            }
            return medals;

        }catch(SQLException e){
            medals.add(0,0);
            medals.add(1,0);
            medals.add(2,0);
            return medals;
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

    public static ArrayList<Integer> getMostRatedAthletes() {
        ArrayList<Integer> rating_athletes = new ArrayList<>();
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("SELECT  athlete_id FROM athlete_medals " );
            ResultSet results = pstmt.executeQuery();
            Integer counter = 1;
            while(results.next() && counter<=10) {
                rating_athletes.add(results.getInt("athlete_id"));
                counter++;
            }
            return rating_athletes;

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
        return rating_athletes;
    }

    public static ArrayList<Integer> getCloseAthletes(Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement( "SELECT athlete_id FROM\n" +
                    "((SELECT t2.athlete_id FROM\n" +
                    "(SELECT t1.athlete_id FROM\n" +
                    "(SELECT t1.athlete_id, COUNT(t1.sport_id) AS sports_count  FROM\n" +
                    "(SELECT athlete_id, sport_id FROM participate WHERE athlete_id!=?) AS t1\n" +
                    "INNER JOIN \n" +
                    "(SELECT athlete_id, sport_id from participate t2 WHERE athlete_id=?)AS t2\n" +
                    "ON t1.sport_id=t2.sport_id \n" +
                    "GROUP BY t1.athlete_id) AS t1\n" +
                    "WHERE t1.sports_count*2 >= (SELECT COUNT(sport_id) FROM participate WHERE athlete_id=?)\n" +
                    "ORDER BY t1.athlete_id ASC LIMIT 10) AS t2)\n" +
                    "UNION\n" +
                    "(SELECT athlete_id FROM athlete \n" +
                    "WHERE athlete_id!=? AND NOT EXISTS(SELECT athlete_id FROM participate WHERE athlete_id=? ) AND EXISTS (SELECT athlete_id FROM athlete WHERE athlete_id=?))) AS t3\n" +
                    "ORDER BY t3.athlete_id ASC LIMIT 10"
            );
            pstmt.setInt(1,athleteId);
            pstmt.setInt(2,athleteId);
            pstmt.setInt(3,athleteId);
            pstmt.setInt(4, athleteId);
            pstmt.setInt(5, athleteId);
            pstmt.setInt(6, athleteId);
            ResultSet results = pstmt.executeQuery();
            ArrayList<Integer> aths = new ArrayList();

            while(results.next()){

                aths.add(results.getInt("athlete_id"));
            }
            return aths;

        }catch(SQLException e){
            return new ArrayList<>();
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

    public static ArrayList<Integer> getSportsRecommendation(Integer athleteId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("select sport.sport_name,count(participate.sport_id) as most_played,participate.sport_id  from participate\n" +
                    "join sport on sport.sport_id=participate.sport_id\n" +
                    "join (SELECT athlete_id FROM\n" +
                    "((SELECT t2.athlete_id FROM\n" +
                    "(SELECT t1.athlete_id FROM\n" +
                    "(SELECT t1.athlete_id, COUNT(t1.sport_id) AS sports_count  FROM\n" +
                    "(SELECT athlete_id, sport_id FROM participate WHERE athlete_id!=1) AS t1\n" +
                    "INNER JOIN \n" +
                    "(SELECT athlete_id, sport_id from participate t2 WHERE athlete_id=1)AS t2\n" +
                    "ON t1.sport_id=t2.sport_id \n" +
                    "GROUP BY t1.athlete_id) AS t1\n" +
                    "WHERE t1.sports_count*2 >= (SELECT COUNT(sport_id) FROM participate WHERE athlete_id=1)\n" +
                    "ORDER BY t1.athlete_id ASC LIMIT 10) AS t2)\n" +
                    "UNION\n" +
                    "(SELECT athlete_id FROM athlete \n" +
                    "WHERE athlete_id!=1 AND NOT EXISTS(SELECT athlete_id FROM participate WHERE athlete_id=1) AND EXISTS (SELECT athlete_id FROM athlete WHERE athlete_id=1) )) AS t3\n" +
                    "ORDER BY t3.athlete_id ASC LIMIT 10)as t1 on t1.athlete_id=participate.athlete_id\n" +
                    "where participate.sport_id not in (select participate.sport_id from participate where athlete_id=?)\n" +
                    "group by sport.sport_name, participate.sport_id\n" +
                    "order by most_played desc, participate.sport_id ASC  limit 3\n" );

            pstmt.setInt(1,athleteId);



            ResultSet results = pstmt.executeQuery();
            ArrayList<Integer> sports_ids = new ArrayList();

            while(results.next()){

                sports_ids.add(results.getInt("sport_id"));


            }
            return sports_ids;

        }catch(SQLException e){
            return new ArrayList<>();
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


