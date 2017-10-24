package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class CaseInsensitiveExactNamePredicateTest {

    @Test
    public void equals() throws IllegalValueException {
        Name firstName = new Name("John Doe");
        Name secondName = new Name("Jane Dane");

        CaseInsensitiveExactNamePredicate firstPredicate = new CaseInsensitiveExactNamePredicate(firstName);
        CaseInsensitiveExactNamePredicate secondPredicate = new CaseInsensitiveExactNamePredicate(secondName);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        CaseInsensitiveExactNamePredicate firstPredicateCopy = new CaseInsensitiveExactNamePredicate(firstName);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different name -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }
}
