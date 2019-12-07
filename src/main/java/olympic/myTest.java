package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class myTest {

    public static void main(String[] args){
        //testAddAthlete();
        //testIsAthletePopular();
        //testIsAthletePopular();
        //testGetTotalNumberOfMedalsFromCountry();
        //testGetIncomeFromSport();
        //testGetBestCountry();
        //testGetMostPopularCity();
        //testGetAthleteMedals();
        //testGetMostRatedAthletes();
        testGetCloseAthletes();
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
        sol.clearTables();
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

    private static void testIsAthletePopular() {
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        for (int i=1; i <=10; i++) {
            Athlete a = startAthlete(i, "or", "isr", true);
            Sport s = startSport(i, "football", "Haifa");
            sol.addAthlete(a);
            sol.addSport(s);
        }

        //makes evrexone friends
        for(int i=1; i<=10; i++){
            for(int j=i+1; j<=10; j++)
                sol.makeFriends(i,j);
        }

        for(int i=1; i<=10; i++) {
            assertEquals(true, sol.isAthletePopular(i));
        }

        for(int i=1; i<=10; i++){
            sol.athleteJoinSport(1,i);
        }

        for(int i=1; i<=10; i++) {
            assertEquals(true, sol.isAthletePopular(i));
        }

        for(int i=1; i<=5; i++){
            sol.athleteLeftSport(1,i);
        }

        for(int i=1; i<5; i++){
            assertEquals(false, sol.isAthletePopular(i));
        }

        for(int i=6; i<=10; i++){
            assertEquals(true, sol.isAthletePopular(i));
        }

        for(int i=1; i<=5; i++){
            sol.athleteJoinSport(1,i);
        }

        Sport s2 = startSport(2, "basketball", "TLV");
        sol.addSport(s2);
        sol.athleteJoinSport(2,1);

        assertEquals(true, sol.isAthletePopular(1));
        for(int i=2; i<=10; i++){
            assertEquals(false, sol.isAthletePopular(i));
        }

        sol.athleteJoinSport(2,2);
        assertEquals(true, sol.isAthletePopular(2));
        for(int i=3; i<=10; i++)
            assertEquals(false, sol.isAthletePopular(i));

        sol.athleteLeftSport(2,1);
        assertEquals(true, sol.isAthletePopular(2));
        assertEquals(false, sol.isAthletePopular(1));

        sol.removeFriendship(2,3);
        assertEquals(true, sol.isAthletePopular(3));

        Athlete a = startAthlete(3, "fsda", "fdsa" , false);
        sol.deleteAthlete(a);
        a.setId(2);
        sol.deleteAthlete(a);
        for(int i=1; i<=10; i++)
            if(i != 3 && i != 2) {
                assertEquals(true, sol.isAthletePopular(i));
            }
    }

    private static void  testGetTotalNumberOfMedalsFromCountry(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        String[] countries = {"israel", "usa", "italy"};

        for(int i=1; i<=3; i++){
            Sport s = startSport(i,"fds", "fdsa");
            Athlete a = startAthlete(i, "fdsa,", countries[i-1], true);
            sol.addSport(s);
            sol.addAthlete(a);
        }
        for(int i=1; i<=3; i++){
            for(int j=1; j<=3; j++) {
                sol.athleteJoinSport(i, j);
                sol.confirmStanding(i,j,2);
            }
        }

        for(int i=0; i<=2; i++)
            assertEquals(3, sol.getTotalNumberOfMedalsFromCountry(countries[i]).longValue());

        sol.athleteDisqualified(1,1);
        assertEquals(2, sol.getTotalNumberOfMedalsFromCountry(countries[0]).longValue());
        sol.athleteDisqualified(2,1);
        assertEquals(1, sol.getTotalNumberOfMedalsFromCountry(countries[0]).longValue());
        sol.athleteDisqualified(3,1);
        assertEquals(0, sol.getTotalNumberOfMedalsFromCountry(countries[0]).longValue());

        assertEquals(0, sol.getTotalNumberOfMedalsFromCountry("Lebanon").longValue());

        Athlete a= startAthlete(4,"fsda", "usa", false);
        sol.addAthlete(a);
        sol.athleteJoinSport(1,4);
        sol.confirmStanding(1,4,2);
        assertEquals(3, sol.getTotalNumberOfMedalsFromCountry("usa").longValue());

        for(int i=1; i<=3; i++){
            for(int j=1; j<=3; j++) {
                sol.confirmStanding(i,j,1);
            }
        }
        for(int i=0; i<=2; i++)
            assertEquals(3, sol.getTotalNumberOfMedalsFromCountry(countries[i]).longValue());

        a.setId(1);
        sol.deleteAthlete(a);
        assertEquals(0, sol.getTotalNumberOfMedalsFromCountry("israel").longValue());

        for(int i=5; i<=7; i++){
            Sport s = startSport(i,"fds", "fdsa");
             a = startAthlete(i, "fdsa,", countries[i-5], true);
            sol.addSport(s);
            sol.addAthlete(a);
        }
        for(int i=5; i<=7; i++){
            for(int j=5; j<=7; j++) {
                sol.athleteJoinSport(i,j);
                sol.confirmStanding(i,j,2);
            }
        }

        assertEquals(3, sol.getTotalNumberOfMedalsFromCountry("israel").longValue());
        assertEquals(6, sol.getTotalNumberOfMedalsFromCountry("italy").longValue());
        assertEquals(6, sol.getTotalNumberOfMedalsFromCountry("usa").longValue());
    }

    private static void testGetIncomeFromSport(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        Sport s = startSport(1, "fsda", "fdsafsd");
        sol.addSport(s);
        for(int i=1; i<=3; i++){
            Athlete a = startAthlete(i, "fdsa", "Fsda", false);
            sol.addAthlete(a);
            sol.athleteJoinSport(1,i);
        }

        for(int i=4; i<=6; i++){
            Athlete a = startAthlete(i, "fdsa", "Fsda", true);
            sol.addAthlete(a);
            sol.athleteJoinSport(1,i);
        }

        assertEquals(300, sol.getIncomeFromSport(1).longValue());

        sol.athleteLeftSport(1,1);
        assertEquals(200, sol.getIncomeFromSport(1).longValue());

        sol.athleteLeftSport(1,2);
        assertEquals(100, sol.getIncomeFromSport(1).longValue());

        sol.athleteLeftSport(1,3);
        assertEquals(0, sol.getIncomeFromSport(1).longValue());

        for(int i=1; i<=3; i++){
            sol.athleteJoinSport(1,i);
        }

       sol.changePayment(1, 1, 450);
        assertEquals(650, sol.getIncomeFromSport(1).longValue());

        assertEquals(ReturnValue.NOT_EXISTS, sol.changePayment(4, 1, 200));
        assertEquals(650, sol.getIncomeFromSport(1).longValue());

        assertEquals(0, sol.getIncomeFromSport(2).longValue());

        sol.changePayment(1,1,0);
        assertEquals(200, sol.getIncomeFromSport(1).longValue());

        assertEquals(ReturnValue.BAD_PARAMS, sol.changePayment(3,1,-100));
        assertEquals(200, sol.getIncomeFromSport(1).longValue());

        sol.deleteAthlete(startAthlete(1,"fsda","fsda", false));
        assertEquals(200, sol.getIncomeFromSport(1).longValue());

        sol.deleteAthlete(startAthlete(2,"fsda","fsda", false));
        assertEquals(100, sol.getIncomeFromSport(1).longValue());

        sol.deleteAthlete(startAthlete(3,"fsda","fsda", false));
        assertEquals(0, sol.getIncomeFromSport(1).longValue());

    }

    private static void testGetBestCountry(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        assertEquals("", sol.getBestCountry());

        String[] countries = {"argentina","israel","italy","turkey", "usa" , "zimbabwe"};

        for(int i=1; i<=6; i++){
            sol.addAthlete(startAthlete(i,"gfd",countries[i-1], true));
            sol.addSport(startSport(i,"fdsa","fdsa"));
        }
        for(int i=1; i<=6; i++){
            for(int j=1; j<=6; j++)
                sol.athleteJoinSport(j,i);
        }

        assertEquals("", sol.getBestCountry());

        sol.confirmStanding(1,2,1);
        assertEquals("israel", sol.getBestCountry());

        sol.confirmStanding(1,1,2);
        assertEquals("argentina", sol.getBestCountry());

        sol.confirmStanding(1,3,3);
        sol.confirmStanding(2,3,2);
        assertEquals("italy", sol.getBestCountry());

        for(int i=7; i<=12; i++){
            sol.addAthlete(startAthlete(i,"gfd",countries[i-7], true));
        }
        for(int i=7; i<=12; i++){
            for(int j=1; j<=6; j++)
                assertEquals(ReturnValue.OK,sol.athleteJoinSport(j,i));
        }

        assertEquals("italy", sol.getBestCountry());

        sol.confirmStanding(3, 8,4);
        assertEquals("italy", sol.getBestCountry());
        sol.confirmStanding(3, 8,0);
        assertEquals("italy", sol.getBestCountry());
        sol.confirmStanding(3, 8,-1);
        assertEquals("italy", sol.getBestCountry());

        sol.confirmStanding(3, 8,1);
        assertEquals("israel", sol.getBestCountry());

        sol.confirmStanding(4, 7,3);
        assertEquals("argentina", sol.getBestCountry());

        sol.athleteDisqualified(4,7);
        assertEquals("israel", sol.getBestCountry());

        sol.deleteAthlete(startAthlete(2,"fds", "fds", false));
        assertEquals("italy", sol.getBestCountry());

        sol.deleteSport(startSport(1,"fdsa","fdsa"));
        assertEquals("israel", sol.getBestCountry());

        sol.athleteDisqualified(3,8);
        assertEquals("italy", sol.getBestCountry());

        sol.athleteDisqualified(2,3);
        assertEquals("", sol.getBestCountry());
    }

    private static void testGetMostPopularCity(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        assertEquals("", sol.getMostPopularCity());

        Sport s1 = startSport(1, "football", "haifa");
        sol.addSport(s1);
        assertEquals("haifa", sol.getMostPopularCity());
        sol.deleteSport(s1);

        String[] cities = {"haifa",  "paris", "raanana", "rome",  "tlv"};

        for(int i=1; i<=5; i++){
            Athlete a = startAthlete(i, "fds", "fdsa", true);
            Sport s = startSport(i, "football", cities[i-1]);
            sol.addAthlete(a);
            sol.addSport(s);
        }

        for(int i=1; i<=3; i++)
            for(int j=1; j<=3; j++)
                sol.athleteJoinSport(j,i);

         s1 = startSport(1, "football", "haifa");
        s1.setAthletesCount(3);
        assertEquals(s1, sol.getSport(1));

        assertEquals("raanana", sol.getMostPopularCity());

        Athlete a = startAthlete(4, "fsda", "fdsa", true);
        sol.addAthlete(a);
        sol.athleteJoinSport(1,4);
        assertEquals("haifa", sol.getMostPopularCity());

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        sol.clearTables();

        for(int i=1; i<=2; i++){
            a = startAthlete(i, "fds", "fdsa", true);
            Sport s = startSport(i, "football", cities[i-1]);
            Sport s2 = startSport(i+2, "fdsa", cities[i-1]);
            sol.addAthlete(a);
            sol.addSport(s);
        }
        sol.athleteJoinSport(1,1);
        sol.athleteJoinSport(2,1);
        sol.athleteJoinSport(3,1);
        sol.athleteJoinSport(4,1);
        sol.athleteJoinSport(1,2);
        sol.athleteJoinSport(2,2);
        sol.athleteJoinSport(3,2);
        sol.athleteJoinSport(4,2);
        assertEquals("paris", sol.getMostPopularCity());

        a = startAthlete(3, "fds", "fdsa", true);
        sol.addAthlete(a);
        sol.athleteJoinSport(1, 3);
        assertEquals("haifa", sol.getMostPopularCity());

        sol.deleteAthlete(a);
        assertEquals("haifa", sol.getMostPopularCity());

        ///////////////////////////////////////////
        sol.clearTables();

        for(int i=1; i<=5; i++){
            a = startAthlete(i, "fds", "fdsa", false);
            Sport s = startSport(i, "football", cities[i-1]);
            sol.addAthlete(a);
            sol.addSport(s);
        }

        for(int i=1; i<=5; i++)
            for(int j=1; j<=5; j++)
                sol.athleteJoinSport(j,i);

        assertEquals("tlv", sol.getMostPopularCity());

        a = startAthlete(6, "fds", "fdsa", true);
        sol.addAthlete(a);
        sol.athleteJoinSport(4,6);
        assertEquals("rome", sol.getMostPopularCity());
    }

    public static void testGetAthleteMedals(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        for(int i=1; i<=3; i++){
            Athlete a = startAthlete(i, "Fsda", "fds", true);
            Sport s = startSport(i, "Fsda", "fdsa");
            sol.addAthlete(a);
            sol.addSport(s);
        }

        for(int i=1; i<=3; i++)
            for(int j=1; j<=3; j++)
                sol.athleteJoinSport(j,i);

        ArrayList<Integer> medals = new ArrayList<>();
        medals.add(0);
        medals.add(0);
        medals.add(0);

        assertEquals(medals, sol.getAthleteMedals(4));
        assertEquals(medals, sol.getAthleteMedals(1));

        sol.confirmStanding(1,1,2);
        sol.confirmStanding(1,1,3);
        medals.clear();
        medals.add(0);
        medals.add(0);
        medals.add(1);
        assertEquals(medals, sol.getAthleteMedals(1));

        sol.confirmStanding(2,1,3);
        medals.clear();
        medals.add(0);
        medals.add(0);
        medals.add(2);
        assertEquals(medals, sol.getAthleteMedals(1));

        sol.confirmStanding(3,1,1);
        medals.clear();
        medals.add(1);
        medals.add(0);
        medals.add(2);
        assertEquals(medals, sol.getAthleteMedals(1));

        Athlete a= startAthlete(1,"fsda","fdsa", true);
        sol.deleteAthlete(a);
        medals.clear();
        medals.add(0);
        medals.add(0);
        medals.add(0);
        assertEquals(medals, sol.getAthleteMedals(1));

        assertEquals(medals, sol.getAthleteMedals(2));

        sol.confirmStanding(1,2,1);
        sol.confirmStanding(2,2,2);
        sol.confirmStanding(3,2,3);
        medals.clear();
        medals.add(1);
        medals.add(1);
        medals.add(1);
        assertEquals(medals, sol.getAthleteMedals(2));

        sol.athleteDisqualified(2,2);
        medals.clear();
        medals.add(1);
        medals.add(0);
        medals.add(1);
        assertEquals(medals, sol.getAthleteMedals(2));

        //////////////////////////////////////////////////////////////////////////
        sol.clearTables();
        for(int i=1; i<=3; i++){
             a = startAthlete(i, "Fsda", "fds", false);
            Sport s = startSport(i, "Fsda", "fdsa");
            sol.addAthlete(a);
            sol.addSport(s);
        }

        for(int i=1; i<=3; i++)
            for(int j=1; j<=3; j++)
                sol.athleteJoinSport(j,i);

        medals.clear();
        medals.add(0);
        medals.add(0);
        medals.add(0);
        assertEquals(medals, sol.getAthleteMedals(2));

        sol.confirmStanding(1,2,1);
        sol.confirmStanding(2,2,2);
        sol.confirmStanding(3,2,3);
        assertEquals(medals, sol.getAthleteMedals(2));
    }

    private static void testGetMostRatedAthletes(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        ArrayList<Integer> arr = new ArrayList<>();
        assertEquals(arr, sol.getMostRatedAthletes());

        for(int i=1; i<=20; i++){
            Athlete a = startAthlete(i, "Fsda", "fds", false);
            Sport s = startSport(i, "Fsda", "fdsa");
            sol.addAthlete(a);
            sol.addSport(s);
        }

        for(int i=21; i<=40; i++){
            Athlete a = startAthlete(i, "Fsda", "fds", true);
            sol.addAthlete(a);
        }


         for(int i=1; i<=10; i++)
             arr.add(i);

         assertEquals(arr, sol.getMostRatedAthletes());

        for(int i=21; i<=40; i++)
            for(int j=1; j<=20; j++)
                sol.athleteJoinSport(j,i);

         arr.clear();
         for(int i=21; i<=40; i+=2){
             sol.confirmStanding(1,i,2);
             sol.confirmStanding(2,i,2);
             sol.confirmStanding(3,i,2);
             sol.confirmStanding(4,i,2);
             sol.confirmStanding(5,i,2);
             sol.confirmStanding(6,i,2);
             sol.confirmStanding(7,i,2);
             sol.confirmStanding(8,i,2);
             sol.confirmStanding(9,i,2);
             sol.confirmStanding(10,i,2);
             sol.confirmStanding(11,i,2);
             sol.confirmStanding(12,i,2);
             sol.confirmStanding(13,i,2);
             arr.add(i);
         }
         assertEquals(arr, sol.getMostRatedAthletes());

         sol.deleteAthlete(startAthlete(21,"fdsa", "fsda", true));

         sol.confirmStanding(1,22,1);
         arr.clear();
         arr.add(23);
         arr.add(25);
         arr.add(27);
         arr.add(29);
         arr.add(31);
         arr.add(33);
         arr.add(35);
         arr.add(37);
         arr.add(39);
        arr.add(22);
         assertEquals(arr, sol.getMostRatedAthletes());

         sol.confirmStanding(1,24, 3);
        sol.confirmStanding(2,24, 3);
        assertEquals(arr, sol.getMostRatedAthletes());

        sol.confirmStanding(2,24, 2);
        arr.clear();
        arr.add(24);
        arr.add(22);
        arr.add(21);
        arr.add(23);
        arr.add(25);
        arr.add(27);
        arr.add(29);
        arr.add(31);
        arr.add(33);
        arr.add(35);
        assertEquals(arr, sol.getMostRatedAthletes());
    }

    private static void testGetCloseAthletes(){
        Solution sol = new Solution();
        sol.dropTables();
        sol.createTables();

        //add athletes 1-10 and sports 1-10
        for(int i=1; i<=10; i++){
            Athlete a = startAthlete(i, "Fsda", "fds", true);
            Sport s = startSport(i, "Fsda", "fdsa");
            sol.addAthlete(a);
            sol.addSport(s);
        }
        //add athletes 11-20
        for(int i=11; i<=20; i++){
            Athlete a = startAthlete(i, "Fsda", "fds", true);
            sol.addAthlete(a);
        }

        ArrayList<Integer> arr = new ArrayList<>();
        for(int i=2; i<=11; i++)
            arr.add(i);
        assertEquals(arr, sol.getCloseAthletes(1));//evreryone close to each other

        arr.clear();
        for(int i=1; i<=10; i++)
            arr.add(i);
        assertEquals(arr, sol.getCloseAthletes(11));//evreryone close to each other


        sol.athleteJoinSport(1,11);
        arr.clear();
        assertEquals(arr, sol.getCloseAthletes(11));// no one close to 11- he take sport
        assertEquals(arr, sol.getCloseAthletes(41));
        assertEquals(arr, sol.getCloseAthletes(-1));

        sol.athleteJoinSport(1,1);
        arr.add(1);
        assertEquals(arr, sol.getCloseAthletes(11));
        arr.clear();
        arr.add(11);
        assertEquals(arr, sol.getCloseAthletes(1));


        sol.athleteJoinSport(2,11);
        sol.athleteJoinSport(3,11);
        sol.athleteJoinSport(4,11);
        sol.athleteJoinSport(2,2);
        arr.clear();
        assertEquals(arr, sol.getCloseAthletes(11));
        sol.athleteJoinSport(2,1);
        arr.add(1);
        assertEquals(arr, sol.getCloseAthletes(11));
        arr.clear();
        arr.add(2);
        arr.add(11);
        assertEquals(arr, sol.getCloseAthletes(1));

        sol.athleteJoinSport(5,11);
        arr.clear();
        assertEquals(arr, sol.getCloseAthletes(11));

        for(int i=12; i<=20; i++){
            sol.athleteJoinSport(1,i);
            sol.athleteJoinSport(3,i);
            sol.athleteJoinSport(4,i);
            arr.add(i);
        }
        assertEquals(arr, sol.getCloseAthletes(11));

        sol.athleteLeftSport(4,12);
        arr.remove(0);
        assertEquals(arr, sol.getCloseAthletes(11));
    }
}
