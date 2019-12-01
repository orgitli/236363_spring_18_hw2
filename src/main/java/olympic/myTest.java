package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;

import static org.junit.Assert.assertEquals;

public class myTest {

    public static void main(String[] args){
        testAddAthlete();
        testIsAthletePopular();
    }


    public static Athlete startAthlete(Integer id, String name, String country, boolean active){
        Athlete athlete = new Athlete();
        athlete.setId(id);
        athlete.setName(name);
        athlete.setCountry(country);
        athlete.setIsActive(active);
        return athlete;
    }

    private static Sport startSport(Integer id, String name, String city){
        Sport sport = new Sport();
        sport.setId(id);
        sport.setName(name);
        sport.setCity(city);
        sport.setAthletesCount(0);

        return sport;
    }

    private static void testAddAthlete(){
        Solution sol = new Solution();
        Athlete or = startAthlete(1,"or", "israel", true);
        Athlete dan = startAthlete(1,"or", "israel", true);
        Athlete lior = startAthlete(-1,"or", "israel", true);
        Athlete mor = startAthlete(1,null, "israel", true);
        Athlete moran = startAthlete(1,"israel", null, true);

        sol.dropTables();
        sol.createTables();

        assertEquals(sol.addAthlete(or), ReturnValue.OK);
        assertEquals(sol.addAthlete(dan), ReturnValue.ALREADY_EXISTS);
        assertEquals(sol.addAthlete(lior), ReturnValue.BAD_PARAMS);
        assertEquals(sol.addAthlete(mor), ReturnValue.BAD_PARAMS);
        assertEquals(sol.addAthlete(moran), ReturnValue.BAD_PARAMS);

        sol.dropTables();
    }

    private static void testGetAthleteProfile(){
        Solution sol = new Solution();
        Athlete or = startAthlete(1,"or", "israel", true);
        Athlete dan = startAthlete(2,"dan", "italy", false);

        sol.dropTables();
        sol.createTables();

        assertEquals(sol.addAthlete(or), ReturnValue.OK);
        assertEquals(sol.addAthlete(dan), ReturnValue.OK);

        Athlete athlete = sol.getAthleteProfile(1);
        assertEquals(athlete,or);

        assertEquals(sol.getAthleteProfile(2), dan);
        assertEquals(sol.getAthleteProfile(1), or);

        assertEquals(sol.getAthleteProfile(-1), Athlete.badAthlete());

    }

    private static void testIsAthletePopular(){
        Solution sol = new Solution();
        Athlete or = startAthlete(1,"or", "israel", true);
        Athlete dan = startAthlete(2,"dan", "italy", false);

        sol.dropTables();
        sol.createTables();

        sol.addAthlete(or);
        assertEquals(sol.isAthletePopular(1), true);
        sol.addAthlete(dan);

        assertEquals(sol.isAthletePopular(1), true);
        assertEquals(sol.isAthletePopular(2), true);

        //make dani and or friends, dani take basketball. now dani popular, and or doesnt
        Sport basketball = startSport(1, "basketbll", "haifa");
        assertEquals(sol.addSport(basketball), ReturnValue.OK);
        assertEquals(sol.athleteJoinSport(1,2), ReturnValue.OK);
        assertEquals(sol.makeFriends(1,2), ReturnValue.OK);

        assertEquals(sol.isAthletePopular(1), false);
        assertEquals(sol.isAthletePopular(2), true);

        //or also take basketball. both are populars
        assertEquals(sol.athleteJoinSport(1,1), ReturnValue.OK);
        assertEquals(sol.isAthletePopular(1), true);
        assertEquals(sol.isAthletePopular(2), true);

        Sport football = startSport(2,"football", "haifa");
        sol.addSport(football);

        //or takes football. or is popular, dani isnt
        sol.athleteJoinSport(2,1);
        assertEquals(sol.isAthletePopular(1), true);
        assertEquals(sol.isAthletePopular(2), false);
    }
}
