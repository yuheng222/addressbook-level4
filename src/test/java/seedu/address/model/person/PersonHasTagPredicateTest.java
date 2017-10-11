package seedu.address.model.person;

import org.junit.Test;
import seedu.address.testutil.PersonBuilder;

import static org.junit.Assert.*;

public class PersonHasTagPredicateTest {

    @Test
    public void test_personHasTag_returns_true(){
        //person has only one tag
        PersonHasTagPredicate predicate = new PersonHasTagPredicate("friends");
        assertTrue(predicate.test(new PersonBuilder().build()));

        //person has multiple tags
        predicate = new PersonHasTagPredicate("friends");
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "colleagues").build()));

        //different case
        predicate = new PersonHasTagPredicate("FRIENDS");
        assertTrue(predicate.test(new PersonBuilder().withTags("fRiEnds").build()));
    }

    @Test
    public void test_personHasTag_returns_false(){
        //no keyword
        PersonHasTagPredicate predicate = new PersonHasTagPredicate("");
        assertFalse(predicate.test(new PersonBuilder().build()));

        //keyword does not match
        predicate = new PersonHasTagPredicate("families");
        assertFalse(predicate.test(new PersonBuilder().withTags("friends", "colleagues").build()));

        //keyword matches name, but not tag name
        predicate = new PersonHasTagPredicate("Alice");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void equals() throws Exception {
        String firstPredicateKeyword = new String("first");
        String secondPredicateKeyword = new String("second");

        PersonHasTagPredicate firstPredicate = new PersonHasTagPredicate(firstPredicateKeyword);
        PersonHasTagPredicate secondPredicate = new PersonHasTagPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonHasTagPredicate firstPredicateCopy = new PersonHasTagPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }
}
