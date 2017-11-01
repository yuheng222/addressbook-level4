# WangJieee
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_addtag() throws Exception {
        List<String> tags = Arrays.asList("friends", "colleagues");
        Set<Tag> tagsToAdd = ParserUtil.parseTags(tags);
        AddTagCommand command = (AddTagCommand) parser.parseCommand(AddTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + tags.stream().collect(Collectors.joining(" ")));
        assertEquals(new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd), command);
    }
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_filter() throws Exception {
        List<String> tags = Arrays.asList("friends", "families", "colleagues");
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " " + tags.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterCommand(new PersonHasTagPredicate(tags)), command);
    }
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_removetag() throws Exception {
        List<String> tags = Arrays.asList("friends", "colleagues");
        Set<Tag> tagsToRemove = ParserUtil.parseTags(tags);
        RemoveTagCommand command = (RemoveTagCommand) parser.parseCommand(RemoveTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + tags.stream().collect(Collectors.joining(" ")));
        assertEquals(new RemoveTagCommand(INDEX_FIRST_PERSON, tagsToRemove), command);
    }
```
###### /java/seedu/address/logic/parser/AddTagCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

public class AddTagCommandParserTest {
    public static final String VALID_TAG_1 = "friends";
    public static final String VALID_TAG_2 = "classmates";
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);

    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TAG_1, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_TAG_1, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + VALID_TAG_2, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC + VALID_TAG_1, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag
    }

    @Test
    public void parse_validValue_success() throws Exception {
        Set<Tag> tags = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Index targetIndex = INDEX_FIRST_PERSON;
        AddTagCommand expectedCommand = new AddTagCommand(targetIndex, tags);
        assertParseSuccess(parser, "1 " + VALID_TAG_1 + " " + VALID_TAG_2, expectedCommand);
    }
}
```
###### /java/seedu/address/logic/parser/FilterCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonHasTagPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand =
                new FilterCommand(new PersonHasTagPredicate(Arrays.asList("friends", "colleagues")));
        assertParseSuccess(parser, "friends colleagues", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n friends \n \t colleagues  \t", expectedFilterCommand);
    }
}
```
###### /java/seedu/address/logic/parser/RemoveTagCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveTagCommand;
import seedu.address.model.tag.Tag;

public class RemoveTagCommandParserTest {
    public static final String VALID_TAG_1 = "friends";
    public static final String VALID_TAG_2 = "classmates";
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE);

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TAG_1, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_TAG_1, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + VALID_TAG_2, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC + VALID_TAG_1, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag
    }

    @Test
    public void parse_validValue_success() throws Exception {
        Set<Tag> tags = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Index targetIndex = INDEX_FIRST_PERSON;
        RemoveTagCommand expectedCommand = new RemoveTagCommand(targetIndex, tags);
        assertParseSuccess(parser, "1 " + VALID_TAG_1 + " " + VALID_TAG_2, expectedCommand);
    }
}
```
