package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonHasTagPredicate;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

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
                new FilterCommand(new PersonHasTagPredicate("friends"));
        assertParseSuccess(parser, "friends", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n friends  \t", expectedFilterCommand);
    }
}