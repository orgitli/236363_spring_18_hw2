package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;

import java.util.ArrayList;

import static olympic.business.ReturnValue.OK;

public class Solution {
    public static void createTables() { }

    public static void clearTables() { }

    public static void dropTables() { }

    public static ReturnValue addAthlete(Athlete athlete) { return OK; }

    public static Athlete getAthleteProfile(Integer athleteId) {
        return Athlete.badAthlete();
    }

    public static ReturnValue deleteAthlete(Athlete athlete) {
        return OK;
    }

    public static ReturnValue addSport(Sport sport) {
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

