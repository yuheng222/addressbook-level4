package seedu.address.model.person;

import java.util.function.Predicate;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code ReadOnlyPerson}'s {@code UniqueTagList} contains the specific tag.
 */
public class PersonHasTagPredicate implements Predicate<ReadOnlyPerson> {
    private final String tagKeyword;

    public PersonHasTagPredicate(String tagKeyword) {
        this.tagKeyword = tagKeyword.toLowerCase();
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        Set<Tag> tagSet = person.getTags();
        for (Tag t: tagSet) {
            if (t.tagName.toLowerCase().equals(tagKeyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonHasTagPredicate // instanceof handles nulls
                && this.tagKeyword.equals(((PersonHasTagPredicate) other).tagKeyword)); // state check
    }
}
