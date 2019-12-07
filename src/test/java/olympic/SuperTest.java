package olympic;

import olympic.business.Athlete;
import olympic.business.ReturnValue;
import olympic.business.Sport;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SuperTest extends AbstractTest {
    private Athlete[] athletes;

    private Sport[] sports;

    @Before
    public void setUp() {
        athletes = new Athlete[10];
        athletes[0] = new Athlete();
        athletes[0].setId(10);
        athletes[0].setName("Alice");
        athletes[0].setCountry("Argentina");
        athletes[0].setIsActive(false);

        athletes[1] = new Athlete();
        athletes[1].setId(20);
        athletes[1].setName("Bob");
        athletes[1].setCountry("Belgium");
        athletes[1].setIsActive(false);

        athletes[2] = new Athlete();
        athletes[2].setId(30);
        athletes[2].setName("Carol");
        athletes[2].setCountry("Canada");
        athletes[2].setIsActive(false);

        athletes[3] = new Athlete();
        athletes[3].setId(40);
        athletes[3].setName("David");
        athletes[3].setCountry("Denmark");
        athletes[3].setIsActive(false);

        athletes[4] = new Athlete();
        athletes[4].setId(50);
        athletes[4].setName("Eve");
        athletes[4].setCountry("Egypt");
        athletes[4].setIsActive(false);

        athletes[5] = new Athlete();
        athletes[5].setId(60);
        athletes[5].setName("Frank");
        athletes[5].setCountry("France");
        athletes[5].setIsActive(false);

        athletes[6] = new Athlete();
        athletes[6].setId(70);
        athletes[6].setName("Grace");
        athletes[6].setCountry("Germany");
        athletes[6].setIsActive(false);

        athletes[7] = new Athlete();
        athletes[7].setId(80);
        athletes[7].setName("Heidi");
        athletes[7].setCountry("Hungary");
        athletes[7].setIsActive(false);

        athletes[8] = new Athlete();
        athletes[8].setId(90);
        athletes[8].setName("Ivan");
        athletes[8].setCountry("Israel");
        athletes[8].setIsActive(false);

        athletes[9] = new Athlete();
        athletes[9].setId(100);
        athletes[9].setName("Judy");
        athletes[9].setCountry("Japan");
        athletes[9].setIsActive(false);


        sports = new Sport[10];

        sports[0] = new Sport();
        sports[0].setId(11);
        sports[0].setName("Archery");
        sports[0].setCity("Austin");
        sports[0].setAthletesCount(0);

        sports[1] = new Sport();
        sports[1].setId(21);
        sports[1].setName("Badminton");
        sports[1].setCity("Beijing");
        sports[1].setAthletesCount(0);

        sports[2] = new Sport();
        sports[2].setId(31);
        sports[2].setName("Curling");
        sports[2].setCity("Cape Town");
        sports[2].setAthletesCount(0);

        sports[3] = new Sport();
        sports[3].setId(41);
        sports[3].setName("Decathlon");
        sports[3].setCity("Delhi");
        sports[3].setAthletesCount(0);

        sports[4] = new Sport();
        sports[4].setId(51);
        sports[4].setName("E-Sports");
        sports[4].setCity("Edinburgh");
        sports[4].setAthletesCount(0);

        sports[5] = new Sport();
        sports[5].setId(61);
        sports[5].setName("Fencing");
        sports[5].setCity("Fukuoka");
        sports[5].setAthletesCount(0);

        sports[6] = new Sport();
        sports[6].setId(71);
        sports[6].setName("Golf");
        sports[6].setCity("Glasgow");
        sports[6].setAthletesCount(0);

        sports[7] = new Sport();
        sports[7].setId(81);
        sports[7].setName("Hockey");
        sports[7].setCity("Hong Kong");
        sports[7].setAthletesCount(0);

        sports[8] = new Sport();
        sports[8].setId(91);
        sports[8].setName("Ice Skating");
        sports[8].setCity("Istanbul");
        sports[8].setAthletesCount(0);

        sports[9] = new Sport();
        sports[9].setId(101);
        sports[9].setName("Javelin");
        sports[9].setCity("Jakarta");
        sports[9].setAthletesCount(0);
    }

    private void addAthletesAndSports()
    {
        for(Athlete a : athletes)
            Solution.addAthlete(a);

        for(Sport s : sports)
            Solution.addSport(s);
    }

    @Test
    public void TestAthleteCRUD()
    {
        //doesn't exist
        assertEquals("getAthleteProfile nonexistent athlete", Athlete.badAthlete(),Solution.getAthleteProfile(0));

        Athlete badAthlete = new Athlete();
        badAthlete.setId(-123);
        badAthlete.setCountry("Switzerland");
        badAthlete.setName("Bob");
        badAthlete.setIsActive(true);

        assertEquals("cant delete nonexistent athlete (negative id)", ReturnValue.NOT_EXISTS, Solution.deleteAthlete(badAthlete));

        assertEquals("negative id", ReturnValue.BAD_PARAMS, Solution.addAthlete(badAthlete));
        assertEquals("negative id should not be added", Athlete.badAthlete(), Solution.getAthleteProfile(badAthlete.getId()));

        badAthlete.setId(123);
        badAthlete.setCountry(null);

        assertEquals("cant delete nonexistent athlete (no athletes yet)", ReturnValue.NOT_EXISTS, Solution.deleteAthlete(badAthlete));
        assertEquals("null country", ReturnValue.BAD_PARAMS, Solution.addAthlete(badAthlete));
        assertEquals("null country should not be added", Athlete.badAthlete(), Solution.getAthleteProfile(badAthlete.getId()));

        badAthlete.setCountry("Switzerland");
        badAthlete.setName(null);
        assertEquals("null name", ReturnValue.BAD_PARAMS, Solution.addAthlete(badAthlete));
        assertEquals("null name should not be added", Athlete.badAthlete(), Solution.getAthleteProfile(badAthlete.getId()));

        Athlete goodAthlete = new Athlete();
        goodAthlete.setId(123);
        goodAthlete.setCountry("Switzerland");
        goodAthlete.setName("Bob");
        goodAthlete.setIsActive(true);

        assertEquals("normal athlete should not cause errors", ReturnValue.OK, Solution.addAthlete(goodAthlete));
        assertEquals("getAthleteProfile should return the same athlete", goodAthlete, Solution.getAthleteProfile(goodAthlete.getId()));

        badAthlete.setName("Totally not Bob");
        assertEquals("addAthlete with same id", ReturnValue.ALREADY_EXISTS, Solution.addAthlete(badAthlete));
        assertEquals("good athlete shouldn't be overridden", goodAthlete, Solution.getAthleteProfile(goodAthlete.getId()));

        badAthlete.setId(456);
        assertEquals("cant delete nonexistent athlete (no athlete with this id)", ReturnValue.NOT_EXISTS, Solution.deleteAthlete(badAthlete));

        assertEquals("should delete existing athlete", ReturnValue.OK, Solution.deleteAthlete(goodAthlete));
        assertEquals("getAthleteProfile should fail after delete", Athlete.badAthlete(), Solution.getAthleteProfile(goodAthlete.getId()));
    }

    @Test
    public void TestSportCRUD()
    {
        assertEquals("getAthleteProfile nonexistent sport", Sport.badSport(),Solution.getSport(0));

        Sport badSport = new Sport();
        badSport.setId(-123);
        badSport.setCity("Auckland");
        badSport.setName("Curling");
        badSport.setAthletesCount(10);

        assertEquals("cant delete nonexistent sport (negative id)", ReturnValue.NOT_EXISTS, Solution.deleteSport(badSport));

        assertEquals("negative id", ReturnValue.BAD_PARAMS, Solution.addSport(badSport));
        assertEquals("negative id should not be added", Sport.badSport(), Solution.getSport(badSport.getId()));

        badSport.setId(123);
        badSport.setCity(null);

        assertEquals("cant delete nonexistent sport (no sports yet)", ReturnValue.NOT_EXISTS, Solution.deleteSport(badSport));
        assertEquals("null city", ReturnValue.BAD_PARAMS, Solution.addSport(badSport));
        assertEquals("null city should not be added", Sport.badSport(), Solution.getSport(badSport.getId()));

        badSport.setCity("Auckland");
        badSport.setName(null);
        assertEquals("null name", ReturnValue.BAD_PARAMS, Solution.addSport(badSport));
        assertEquals("null name should not be added", Sport.badSport(), Solution.getSport(badSport.getId()));

        badSport.setName("Curling");
        badSport.setAthletesCount(-15);
        assertEquals("negative athletes count", ReturnValue.BAD_PARAMS, Solution.addSport(badSport));
        assertEquals("negative athletes count should not be added", Sport.badSport(), Solution.getSport(badSport.getId()));
        badSport.setAthletesCount(0);

        Sport goodSport = new Sport();
        goodSport.setId(123);
        goodSport.setCity("Auckland");
        goodSport.setName("Curling");
        goodSport.setAthletesCount(0);

        assertEquals("normal sport should not cause errors", ReturnValue.OK, Solution.addSport(goodSport));
        assertEquals("getSport should return the same sport", goodSport, Solution.getSport(goodSport.getId()));

        badSport.setName("Totally not curling");
        assertEquals("addSport() with same id", ReturnValue.ALREADY_EXISTS, Solution.addSport(badSport));
        assertEquals("good sport shouldn't be overridden", goodSport, Solution.getSport(goodSport.getId()));

        badSport.setId(456);
        assertEquals("cant delete nonexistent sport (no sport with this id)", ReturnValue.NOT_EXISTS, Solution.deleteSport(badSport));

        assertEquals("should delete existing sport", ReturnValue.OK, Solution.deleteSport(goodSport));
        assertEquals("getSport should fail after delete", Sport.badSport(), Solution.getSport(goodSport.getId()));

        //not sure about this part

        //Sport goodSport2 = new Sport();
        //goodSport2.setId(456);
        //goodSport2.setCity("Auckland");
        //goodSport2.setName("Curling");
        //goodSport2.setAthletesCount(10);
        //
        //assertEquals("new sport with different id", ReturnValue.OK, Solution.addSport(goodSport2));
        //assertNotEquals("getSport should not return badAthlete for a good sport", Sport.badSport(), Solution.getSport(goodSport2.getId()));
        //assertEquals("(not sure about this) new sport's counters should be zero", 0, Solution.getSport(goodSport2.getId()).getAthletesCount());
    }

    @Test
    public void TestJoinAndLeaveSport()
    {
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);

        addAthletesAndSports();

        assertEquals("non existent athlete cant join sport", ReturnValue.NOT_EXISTS, Solution.athleteJoinSport(sports[0].getId(), 999));
        assertEquals("athlete cant join non existent sport", ReturnValue.NOT_EXISTS, Solution.athleteJoinSport(999, athletes[0].getId()));
        assertEquals("joinSport: both athlete and sport dont exist", ReturnValue.NOT_EXISTS, Solution.athleteJoinSport(999, 999));

        assertEquals("athlete can join sport", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("athlete cant join sport twice", ReturnValue.ALREADY_EXISTS, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));

        assertEquals("athlete counter should increase after join", 1, Solution.getSport(sports[0].getId()).getAthletesCount());

        assertEquals("other athlete can join same sport", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));

        assertEquals("athlete counter should increase after another join", 2, Solution.getSport(sports[0].getId()).getAthletesCount());

        assertEquals("inactive athlete joins", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("another inactive athlete joins", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));

        assertEquals("athlete counter should remain the same if athletes are inactive", 2, Solution.getSport(sports[0].getId()).getAthletesCount());

        assertEquals("deleting a participating athlete", ReturnValue.OK, Solution.deleteAthlete(athletes[0]));
        assertEquals("athlete counter should remain the same if an athlete is deleted", 2, Solution.getSport(sports[0].getId()).getAthletesCount());


        assertEquals("non existent athlete cant leave sport", ReturnValue.NOT_EXISTS, Solution.athleteJoinSport(sports[0].getId(), 999));
        assertEquals("cant leave nonexistent sport", ReturnValue.NOT_EXISTS, Solution.athleteLeftSport(999, athletes[0].getId()));
        assertEquals("leftSport: both athlete and sport dont exist", ReturnValue.NOT_EXISTS, Solution.athleteLeftSport(999, 999));
        assertEquals("athlete can't leave sport they didn't join ", ReturnValue.NOT_EXISTS, Solution.athleteLeftSport(sports[0].getId(), athletes[5].getId()));

        assertEquals("active athlete can leave a sport", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("athlete counter should decrease if an active athlete leaves", 1, Solution.getSport(sports[0].getId()).getAthletesCount());

        assertEquals("inactive athlete can leave a sport", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("inactive athlete can leave a sport", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[3].getId()));
        assertEquals("athlete counter should remain the same if inactive athletes leave", 1, Solution.getSport(sports[0].getId()).getAthletesCount());
    }

    @Test
    public void TestStandingAndDisqualified()
    {
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);

        addAthletesAndSports();

        assertEquals("athlete0 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));

        assertEquals("confirmStanding: athlete doesn't exist", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), 999, 3));
        assertEquals("confirmStanding: sport doesn't exist", ReturnValue.NOT_EXISTS, Solution.confirmStanding(999, athletes[0].getId(), 3));
        assertEquals("confirmStanding: athlete didn't join", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), athletes[1].getId(), 3));
        assertEquals("confirmStanding: place > 3", ReturnValue.BAD_PARAMS, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(), 5));
        assertEquals("confirmStanding: place < 1", ReturnValue.BAD_PARAMS, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(), 0));
        assertEquals("confirmStanding: NOT_EXISTS before BAD_PARAMS (place > 3 && athlete didn't join)", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), athletes[1].getId(), 5));

        assertEquals("athlete3 (inactive) joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));
        assertEquals("confirmStanding: athlete joined but is inactive", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), athletes[3].getId(), 3));


        assertEquals("athleteDisqualified: athlete doesn't exist", ReturnValue.NOT_EXISTS, Solution.athleteDisqualified(sports[0].getId(), 999));
        assertEquals("athleteDisqualified: sport doesn't exist", ReturnValue.NOT_EXISTS, Solution.athleteDisqualified(999, athletes[0].getId()));
        assertEquals("athleteDisqualified: athlete didn't join", ReturnValue.NOT_EXISTS, Solution.athleteDisqualified(sports[0].getId(), athletes[1].getId()));

        assertEquals("athleteDisqualified: disqualifying an athlete that didn't win anything should work (in FAQ)", ReturnValue.OK, Solution.athleteDisqualified(sports[0].getId(), athletes[0].getId()));
        assertEquals("confirmStanding: set place to 1", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(),1));
        assertEquals("confirmStanding: set place to 2", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(),2));
        assertEquals("confirmStanding: set place to 3", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(),3));
        assertEquals("athleteDisqualified: disqualifying an athlete", ReturnValue.OK, Solution.athleteDisqualified(sports[0].getId(), athletes[0].getId()));
    }

    @Test
    public void TestMakeFriends()
    {
        addAthletesAndSports();

        //the id's aren't equal => not bad_params
        //one of the athletes doesn't exist => not_exists
        assertEquals("athleteID2 < 0", ReturnValue.NOT_EXISTS, Solution.makeFriends(athletes[0].getId(), -99));
        assertEquals("athleteID1 < 0", ReturnValue.NOT_EXISTS, Solution.makeFriends(-99, athletes[0].getId()));
        assertEquals("both ids < 0", ReturnValue.NOT_EXISTS, Solution.makeFriends(-99, -98));

        assertEquals("athleteID2 doesnt exist", ReturnValue.NOT_EXISTS, Solution.makeFriends(athletes[0].getId(), 999));
        assertEquals("athleteID1 doesnt exist", ReturnValue.NOT_EXISTS, Solution.makeFriends(999, athletes[0].getId()));
        assertEquals("both ids don't exist", ReturnValue.NOT_EXISTS, Solution.makeFriends(999, 998));

        assertEquals("ids are the same", ReturnValue.BAD_PARAMS, Solution.makeFriends(athletes[0].getId(), athletes[0].getId()));
        //BAD_PARAMS > NOT_EXISTS
        assertEquals("ids are the same but dont exist", ReturnValue.BAD_PARAMS, Solution.makeFriends(999, 999));

        assertEquals("athlete0 + athlete1", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[1].getId()));
        assertEquals("athlete0 + athlete1 was just added", ReturnValue.ALREADY_EXISTS, Solution.makeFriends(athletes[0].getId(), athletes[1].getId()));
        assertEquals("friendships are symmetrical", ReturnValue.ALREADY_EXISTS, Solution.makeFriends(athletes[1].getId(), athletes[0].getId()));



        assertEquals("remove athlete0 + athlete1", ReturnValue.OK, Solution.removeFriendship(athletes[0].getId(), athletes[1].getId()));
        assertEquals("athlete0 + athlete1 was just removed", ReturnValue.NOT_EXISTS, Solution.removeFriendship(athletes[0].getId(), athletes[1].getId()));

        assertEquals("remove: athleteID2 < 0", ReturnValue.NOT_EXISTS, Solution.removeFriendship(athletes[0].getId(), -99));
        assertEquals("remove: athleteID1 < 0", ReturnValue.NOT_EXISTS, Solution.removeFriendship(-99, athletes[0].getId()));
        assertEquals("remove: both ids < 0", ReturnValue.NOT_EXISTS, Solution.removeFriendship(-99, -98));

        assertEquals("remove: athleteID2 doesnt exist", ReturnValue.NOT_EXISTS, Solution.removeFriendship(athletes[0].getId(), 999));
        assertEquals("remove: athleteID1 doesnt exist", ReturnValue.NOT_EXISTS, Solution.removeFriendship(999, athletes[0].getId()));
        assertEquals("remove: both ids don't exist", ReturnValue.NOT_EXISTS, Solution.removeFriendship(999, 998));

        assertEquals("athlete0 + athlete1 again", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[1].getId()));
        assertEquals("removing athlete0", ReturnValue.OK, Solution.deleteAthlete(athletes[0]));
        assertEquals("athlete0 was removed", ReturnValue.NOT_EXISTS, Solution.makeFriends(athletes[0].getId(), athletes[1].getId()));
    }

    @Test
    public void TestPopular()
    {
        addAthletesAndSports();

        assertEquals("nonexistent athlete", false, Solution.isAthletePopular(999));
        assertEquals("negative id", false, Solution.isAthletePopular(-999));

        assertEquals("athlete with no friends and doesn't participate in any sports", true, Solution.isAthletePopular(athletes[0].getId()));

        assertEquals("athlete0 <-> athlete1", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[1].getId()));
        assertEquals("athlete with 1 friend that all don't participate in any sports", true, Solution.isAthletePopular(athletes[0].getId()));
        assertEquals("athlete0 <-> athlete2", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[2].getId()));
        assertEquals("athlete with 2 friends that all don't participate in any sports", true, Solution.isAthletePopular(athletes[0].getId()));
        assertEquals("athlete0 <-> athlete3", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[3].getId()));
        assertEquals("athlete with 3 friends that all don't participate in any sports", true, Solution.isAthletePopular(athletes[0].getId()));
        assertEquals("athlete0 <-> athlete4", ReturnValue.OK, Solution.makeFriends(athletes[0].getId(), athletes[4].getId()));
        assertEquals("athlete with 4 friends that all don't participate in any sports", true, Solution.isAthletePopular(athletes[0].getId()));

        assertEquals("athlete1 participates at sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("athlete1 in sport0, athlete0 is not", false, Solution.isAthletePopular(athletes[0].getId()));

        assertEquals("athlete0 participates at sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("athlete0 & 1 in sport0, others is not", true, Solution.isAthletePopular(athletes[0].getId()));

        assertEquals("athlete2 participates at sport2", ReturnValue.OK, Solution.athleteJoinSport(sports[2].getId(), athletes[2].getId()));
        assertEquals("athlete0 not in sport2", false, Solution.isAthletePopular(athletes[0].getId()));
        assertEquals("athlete0 is in sport1 => athlete2 is not popular", false, Solution.isAthletePopular(athletes[2].getId()));

        assertEquals("athlete0 participates at sport2", ReturnValue.OK, Solution.athleteJoinSport(sports[2].getId(), athletes[0].getId()));
        assertEquals("athlete0: sport 0,2; athlete1:sport0; athlete2: sport2", true, Solution.isAthletePopular(athletes[0].getId()));

        assertEquals("athlete0 leaves sport2", ReturnValue.OK, Solution.athleteLeftSport(sports[2].getId(), athletes[0].getId()));
        assertEquals("athlete0 is in sport1 => athlete2 is not popular", false, Solution.isAthletePopular(athletes[0].getId()));
    }

    @Test
    public void TestChangePayment()
    {
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);

        addAthletesAndSports();

        assertEquals("athlete0 participates in sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("athlete3 observes sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));

        assertEquals("nonexistent athlete", ReturnValue.NOT_EXISTS, Solution.changePayment(999, sports[0].getId(), 50));
        assertEquals("nonexistent sport", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[0].getId(), 999, 50));
        assertEquals("athlete doesn't observe/participate in sport", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[5].getId(), sports[5].getId(), 50));
        assertEquals("athlete participates in but doesn't observe sport", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[0].getId(), sports[0].getId(), 50));
        assertEquals("negative payment", ReturnValue.BAD_PARAMS, Solution.changePayment(athletes[3].getId(), sports[0].getId(), -99));

        //NOT_EXISTS > BAD_PARAMS
        assertEquals("negative payment & bad relationship", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[5].getId(), sports[5].getId(), -99));

        assertEquals("should be able to change", ReturnValue.OK, Solution.changePayment(athletes[3].getId(), sports[0].getId(), 123));
    }

    @Test
    public void TestGetIncome()
    {
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);

        addAthletesAndSports();

        assertEquals("nonexistent sport", 0, Solution.getIncomeFromSport(999).intValue());
        assertEquals("negative id", 0, Solution.getIncomeFromSport(-999).intValue());

        assertEquals("empty sport", 0, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete0 participates in sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("athlete1 participates in sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("athlete2 participates in sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("sport with only participants", 0, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("trying to change payment of participant", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[0].getId(), sports[0].getId(), 99));
        assertEquals("trying to change payment of participant", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[1].getId(), sports[0].getId(), 99));
        assertEquals("trying to change payment of participant", ReturnValue.NOT_EXISTS, Solution.changePayment(athletes[2].getId(), sports[0].getId(), 99));
        assertEquals("payment of participants should stay 0", 0, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete2 leaves sport0", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("payment should stay the same after participant leaves", 0, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete3 observes sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));
        assertEquals("new athlete observing => income += 100", 100, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete4 observes sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[4].getId()));
        assertEquals("new athlete observing => income += 100", 200, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete5 observes sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[5].getId()));
        assertEquals("new athlete observing => income += 100", 300, Solution.getIncomeFromSport(sports[0].getId()).intValue());


        assertEquals("athlete5 leaves sport0", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[5].getId()));
        assertEquals("payment should decrease after observer leaves", 200, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("athlete4 pays 123", ReturnValue.OK, Solution.changePayment(athletes[4].getId(), sports[0].getId(), 123));
        assertEquals("income should change after changePayment", 223, Solution.getIncomeFromSport(sports[0].getId()).intValue());


        assertEquals("athlete1 (participant) leaves sport0", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("athlete4 (observer) is deleted", ReturnValue.OK, Solution.deleteAthlete(athletes[4]));
        assertEquals("income should decrease by 123 + 0", 100, Solution.getIncomeFromSport(sports[0].getId()).intValue());

        assertEquals("sport0 deleted", ReturnValue.OK, Solution.deleteSport(sports[0]));
        assertEquals("getIncome after sport was deleted", 0, Solution.getIncomeFromSport(sports[0].getId()).intValue());
    }

    @Test
    public void TestNumMedals()
    {
        //3 from different countries
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);

        //3 from same country
        athletes[3].setIsActive(true);
        athletes[4].setIsActive(true);
        athletes[4].setCountry(athletes[3].getCountry());
        athletes[5].setIsActive(true);
        athletes[5].setCountry(athletes[3].getCountry());

        addAthletesAndSports();

        assertEquals("fake country (glory to Arstotzka!)", 0, Solution.getTotalNumberOfMedalsFromCountry("Arstotzka").intValue());

        assertEquals("real country, no participants", 0, Solution.getTotalNumberOfMedalsFromCountry(athletes[6].getCountry()).intValue());
        assertEquals("observer joins sport", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[6].getId()));
        assertEquals("observer doesn't change medals", 0, Solution.getTotalNumberOfMedalsFromCountry(athletes[6].getCountry()).intValue());

        assertEquals("observer tries to get medal", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), athletes[6].getId(), 2));
        assertEquals("observer can't get medals", 0, Solution.getTotalNumberOfMedalsFromCountry(athletes[6].getCountry()).intValue());

        assertEquals("ath0 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("ath1 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("ath2 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("ath3 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));
        assertEquals("ath4 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[4].getId()));
        assertEquals("ath5 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[5].getId()));

        assertEquals("ath0 => 1st place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(), 1));
        assertEquals("ath1 => 2nd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[1].getId(), 2));
        assertEquals("ath2 => 3rd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[2].getId(), 3));
        assertEquals("ath3 => 1st place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[3].getId(), 1));
        assertEquals("ath4 => 2nd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[4].getId(), 2));
        assertEquals("ath5 => 3rd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[5].getId(), 3));

        assertEquals("only athlete in country", 1, Solution.getTotalNumberOfMedalsFromCountry(athletes[0].getCountry()).intValue());
        assertEquals("only athlete in country", 1, Solution.getTotalNumberOfMedalsFromCountry(athletes[1].getCountry()).intValue());
        assertEquals("only athlete in country", 1, Solution.getTotalNumberOfMedalsFromCountry(athletes[2].getCountry()).intValue());
        assertEquals("3 athletes in country", 3, Solution.getTotalNumberOfMedalsFromCountry(athletes[3].getCountry()).intValue());

        assertEquals("ath0 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[0].getId()));
        assertEquals("ath0 => 1st place in sport1", ReturnValue.OK, Solution.confirmStanding(sports[1].getId(), athletes[0].getId(), 1));
        assertEquals("ath0 joins sport2", ReturnValue.OK, Solution.athleteJoinSport(sports[2].getId(), athletes[0].getId()));
        assertEquals("ath0 => 1st place in sport2", ReturnValue.OK, Solution.confirmStanding(sports[2].getId(), athletes[0].getId(), 1));
        assertEquals("only athlete in country, won in 3 sports", 3, Solution.getTotalNumberOfMedalsFromCountry(athletes[0].getCountry()).intValue());

        assertEquals("sport2 deleted", ReturnValue.OK, Solution.deleteSport(sports[2]));
        assertEquals("ath0 medal in sport1 disqualified", ReturnValue.OK, Solution.athleteDisqualified(sports[1].getId(), athletes[0].getId()));
        assertEquals("athlete0 lost 2 medals", 1, Solution.getTotalNumberOfMedalsFromCountry(athletes[0].getCountry()).intValue());

        assertEquals("ath4 deleted", ReturnValue.OK, Solution.deleteAthlete(athletes[4]));
        assertEquals("2 athletes in country, 1 deleted", 2, Solution.getTotalNumberOfMedalsFromCountry(athletes[3].getCountry()).intValue());

        assertEquals("ath5 leaves sport0", ReturnValue.OK, Solution.athleteLeftSport(sports[0].getId(), athletes[5].getId()));
        assertEquals("medals -= 1 after athlete leaves sport", 1, Solution.getTotalNumberOfMedalsFromCountry(athletes[3].getCountry()).intValue());
    }

    @Test
    public void TestBestCountry()
    {
        assertEquals("no countries (athletes) yet", "", Solution.getBestCountry());

        //3 from same country
        athletes[0].setIsActive(true);
        athletes[7].setIsActive(true);
        athletes[7].setCountry(athletes[0].getCountry());
        athletes[8].setIsActive(true);
        athletes[8].setCountry(athletes[0].getCountry());

        //2 from different countries
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);

        //3 from same country
        athletes[3].setIsActive(true);
        athletes[4].setIsActive(true);
        athletes[4].setCountry(athletes[3].getCountry());
        athletes[5].setIsActive(true);
        athletes[5].setCountry(athletes[3].getCountry());

        addAthletesAndSports();

        assertEquals("all countries have 0 medals", "", Solution.getBestCountry());

        assertEquals("observer joins sport", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[6].getId()));


        assertEquals("ath0 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("ath1 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));
        assertEquals("ath2 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[2].getId()));
        assertEquals("ath3 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[3].getId()));
        assertEquals("ath4 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[4].getId()));
        assertEquals("ath5 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[5].getId()));

        assertEquals("ath1 => 2nd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[1].getId(), 2));
        assertEquals("one country with medals", athletes[1].getCountry(), Solution.getBestCountry());
        assertEquals("ath0 => 1st place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(), 1));
        assertEquals("smaller country lexicographically", athletes[0].getCountry(), Solution.getBestCountry());

        assertEquals("ath2 => 3rd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[2].getId(), 3));
        assertEquals("ath3 => 1st place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[3].getId(), 1));
        assertEquals("ath4 => 2nd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[4].getId(), 2));
        assertEquals("ath5 => 3rd place", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[5].getId(), 3));
        assertEquals("one country with 3 medals, and 3 with 1", athletes[3].getCountry(), Solution.getBestCountry());

        assertEquals("ath7 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[7].getId()));
        assertEquals("ath8 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[8].getId()));
        assertEquals("ath7 => 2nd place", ReturnValue.OK, Solution.confirmStanding(sports[1].getId(), athletes[7].getId(), 2));
        assertEquals("ath8 => 3rd place", ReturnValue.OK, Solution.confirmStanding(sports[1].getId(), athletes[8].getId(), 3));
        assertEquals("2 countries with 3 medals, and 2 with 1", athletes[0].getCountry(), Solution.getBestCountry());
    }

    @Test
    public void TestMostPopularCity()
    {
        assertEquals("no cities (sports) yet", "", Solution.getMostPopularCity());

        //3 from same city
        sports[1].setCity(sports[0].getCity());
        sports[2].setCity(sports[0].getCity());

        //3 from same city
        sports[4].setCity(sports[3].getCity());
        sports[5].setCity(sports[3].getCity());

        //5 active, 5 not
        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);
        athletes[3].setIsActive(true);
        athletes[4].setIsActive(true);
        athletes[5].setIsActive(true);

        addAthletesAndSports();

        assertEquals("all cities have 0 participants => last lexicographically", sports[9].getCity(), Solution.getMostPopularCity());

        assertEquals("observer joins sport", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[6].getId()));
        assertEquals("observer joins sport", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[7].getId()));

        assertEquals("observers don't change popularity", sports[9].getCity(), Solution.getMostPopularCity());


        assertEquals("ath0 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("ath1 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[1].getId()));

        assertEquals("2 sports in same city with 1 active each, rest with 0", sports[0].getCity(), Solution.getMostPopularCity());

        assertEquals("ath2 joins sport3", ReturnValue.OK, Solution.athleteJoinSport(sports[3].getId(), athletes[2].getId()));
        assertEquals("ath3 joins sport3", ReturnValue.OK, Solution.athleteJoinSport(sports[3].getId(), athletes[3].getId()));

        assertEquals("2 sports in same city, 2 in another, rest with 0", sports[3].getCity(), Solution.getMostPopularCity());

        assertEquals("ath0 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[0].getId()));
        assertEquals("ath1 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[1].getId()));

        assertEquals("ath2 joins sport4", ReturnValue.OK, Solution.athleteJoinSport(sports[4].getId(), athletes[2].getId()));
        assertEquals("ath3 joins sport4", ReturnValue.OK, Solution.athleteJoinSport(sports[4].getId(), athletes[3].getId()));

        assertEquals("(2 sports with 2 athletes each, both in the same city) * 2", sports[3].getCity(), Solution.getMostPopularCity());

        //TODO test athletes leaving, athletes/sports getting deleted
    }

    @Test
    public void TestGetAthleteMedals()
    {
        ArrayList<Integer> emptyMedals = new ArrayList<Integer>(Arrays.asList(0, 0, 0));

        assertEquals("no athletes yet", emptyMedals, Solution.getAthleteMedals(999));
        assertEquals("negative id", emptyMedals, Solution.getAthleteMedals(-999));

        athletes[0].setIsActive(true);
        athletes[1].setIsActive(true);
        athletes[2].setIsActive(true);
        athletes[3].setIsActive(true);
        athletes[4].setIsActive(true);

        addAthletesAndSports();

        assertEquals("nonexistent athlete", emptyMedals, Solution.getAthleteMedals(999));
        assertEquals("athlete didn't join any sports", emptyMedals, Solution.getAthleteMedals(athletes[0].getId()));

        assertEquals("observer joins", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[5].getId()));
        assertEquals("observer tries to get medal", ReturnValue.NOT_EXISTS, Solution.confirmStanding(sports[0].getId(), athletes[5].getId(), 1));
        assertEquals("observer doesn't get a medal", emptyMedals, Solution.getAthleteMedals(athletes[5].getId()));

        assertEquals("ath0 joins sport0", ReturnValue.OK, Solution.athleteJoinSport(sports[0].getId(), athletes[0].getId()));
        assertEquals("participant starts with 0 medals", emptyMedals, Solution.getAthleteMedals(athletes[0].getId()));

        ArrayList<Integer> ath0Medals = new ArrayList<Integer>(Arrays.asList(1, 0, 0));

        assertEquals("ath0 1st place, sport0", ReturnValue.OK, Solution.confirmStanding(sports[0].getId(), athletes[0].getId(), 1));
        assertEquals("ath0 gets 1st place", ath0Medals, Solution.getAthleteMedals(athletes[0].getId()));

        ath0Medals.set(0, ath0Medals.get(0) + 1);
        assertEquals("ath0 joins sport1", ReturnValue.OK, Solution.athleteJoinSport(sports[1].getId(), athletes[0].getId()));
        assertEquals("ath0 1st place, sport1", ReturnValue.OK, Solution.confirmStanding(sports[1].getId(), athletes[0].getId(), 1));
        assertEquals("ath0 gets another 1st place", ath0Medals, Solution.getAthleteMedals(athletes[0].getId()));

        ath0Medals.set(1, 1);
        assertEquals("ath0 joins sport2", ReturnValue.OK, Solution.athleteJoinSport(sports[2].getId(), athletes[0].getId()));
        assertEquals("ath0 2nd place, sport2", ReturnValue.OK, Solution.confirmStanding(sports[2].getId(), athletes[0].getId(), 2));
        assertEquals("ath0 gets 2nd place", ath0Medals, Solution.getAthleteMedals(athletes[0].getId()));

        ath0Medals.set(2, 1);
        assertEquals("ath0 joins sport3", ReturnValue.OK, Solution.athleteJoinSport(sports[3].getId(), athletes[0].getId()));
        assertEquals("ath0 3rd place, sport3", ReturnValue.OK, Solution.confirmStanding(sports[3].getId(), athletes[0].getId(), 3));
        assertEquals("ath0 gets 3rd place", ath0Medals, Solution.getAthleteMedals(athletes[0].getId()));

        ArrayList<Integer> ath1Medals = new ArrayList<Integer>(Arrays.asList(0, 2, 3));

        assertEquals("ath1 joins sport2", ReturnValue.OK, Solution.athleteJoinSport(sports[2].getId(), athletes[1].getId()));
        assertEquals("ath1 joins sport3", ReturnValue.OK, Solution.athleteJoinSport(sports[3].getId(), athletes[1].getId()));
        assertEquals("ath1 joins sport4", ReturnValue.OK, Solution.athleteJoinSport(sports[4].getId(), athletes[1].getId()));
        assertEquals("ath1 joins sport5", ReturnValue.OK, Solution.athleteJoinSport(sports[5].getId(), athletes[1].getId()));
        assertEquals("ath1 joins sport6", ReturnValue.OK, Solution.athleteJoinSport(sports[6].getId(), athletes[1].getId()));
        assertEquals("ath1 3rd place, sport2", ReturnValue.OK, Solution.confirmStanding(sports[2].getId(), athletes[1].getId(), 3));
        assertEquals("ath1 3rd place, sport3", ReturnValue.OK, Solution.confirmStanding(sports[3].getId(), athletes[1].getId(), 3));
        assertEquals("ath1 3rd place, sport4", ReturnValue.OK, Solution.confirmStanding(sports[4].getId(), athletes[1].getId(), 3));
        assertEquals("ath1 2nd place, sport5", ReturnValue.OK, Solution.confirmStanding(sports[5].getId(), athletes[1].getId(), 2));
        assertEquals("ath1 2nd place, sport5", ReturnValue.OK, Solution.confirmStanding(sports[6].getId(), athletes[1].getId(), 2));

        assertEquals("ath1 gets a buncha medals", ath1Medals, Solution.getAthleteMedals(athletes[1].getId()));
        assertEquals("ath0's medals don't change", ath0Medals, Solution.getAthleteMedals(athletes[0].getId()));

        assertEquals("ath1's 2nd place get's disqualified", ReturnValue.OK, Solution.athleteDisqualified(sports[6].getId(), athletes[1].getId()));
        ath1Medals.set(1, 1);
        assertEquals("ath1 loses a medal", ath1Medals, Solution.getAthleteMedals(athletes[1].getId()));
    }
}
