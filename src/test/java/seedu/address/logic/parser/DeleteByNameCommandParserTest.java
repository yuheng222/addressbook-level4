package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteByNameCommand;
import seedu.address.model.person.Name;

public class DeleteByNameCommandParserTest {

    private DeleteByNameCommandParser parser = new DeleteByNameCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteByNameCommand() throws IllegalValueException {
        Name name = new Name(VALID_NAME_BOB);
        DeleteByNameCommand expectedDeleteByNameCommand = new DeleteByNameCommand(name);
        assertParseSuccess(parser, VALID_NAME_BOB, expectedDeleteByNameCommand);
    }

}
