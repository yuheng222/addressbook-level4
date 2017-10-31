# Ryan
###### \main\java\seedu\address\logic\commands\ExportCommand.java
``` java

package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Exports the current Address Book data into a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_EXPORT_SUCCESS = "Successfully exported data.";

    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "Export failed. Current Address Book is empty.";

    public static final String DATA_FILE_PATH = "AddressBookData.csv";

    private List<ReadOnlyPerson> currentData;

    @Override
    public CommandResult execute() throws CommandException {
        this.currentData = model.getAddressBook().getPersonList();

        if (currentData.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_ADDRESS_BOOK);
        }

        if (fileExists()) {
            deleteFile();
        }

        createFile();
        writeData();
        return new CommandResult(MESSAGE_EXPORT_SUCCESS);
    }

    /** Constructing the headers for CSV */
    private String constructHeaders() {
        ReadOnlyPerson samplePerson = currentData.get(0);
        List<String> propertyNames = samplePerson.getPropertyNamesAsList();

        final StringBuilder builder = new StringBuilder();

        for (String field : propertyNames) {
            builder.append(field);
            builder.append(",");
        }
        builder.append("Tags");
        builder.append("\n");

        return builder.toString();
    }

    /** Function to write data to the CSV */
    private void writeData() throws CommandException {
        final StringBuilder builder = new StringBuilder();

        File csvData = new File(DATA_FILE_PATH);
        String headers = constructHeaders();

        for (ReadOnlyPerson person : currentData) {
            String personData = generatePersonData(person);
            builder.append(personData);
        }

        String dataToWrite = builder.toString();

        try {
            PrintWriter pw = new PrintWriter(csvData);
            pw.write(headers);
            pw.write(dataToWrite);
            pw.close();
        } catch (IOException ioe) {
            throw new CommandException(ioe.getMessage());
        }

    }

    /**
     * Function to generate Person data as text.
     * Mostly hardcoded for now, until a better implementation can be found.
     */
    private String generatePersonData(ReadOnlyPerson person) {
        String name = person.getName().toString();
        String phone = person.getPhone().toString();
        String email = person.getEmail().toString();
        String address = person.getAddress().toString();
        address = address.replace(',', ';');
        String nokName = person.getNokName().toString();
        String nokPhone = person.getNokPhone().toString();
        String tags = parseTagsToString(person);

        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(",");
        builder.append(phone);
        builder.append(",");
        builder.append(email);
        builder.append(",");
        builder.append(address);
        builder.append(",");
        builder.append(nokName);
        builder.append(",");
        builder.append(nokPhone);
        builder.append(",");
        builder.append(tags);
        builder.append("\n");

        return builder.toString();
    }

    /** Function to convert tags into a suitable String format for CSV */
    private String parseTagsToString(ReadOnlyPerson person) {
        Set<Tag> tags = person.getTags();
        final StringBuilder builder = new StringBuilder();
        for (Tag tag : tags) {
            String convertedTag = tag.toString();
            builder.append(convertedTag);
        }

        return builder.toString();
    }

    /** Function to create the CSV */
    private void createFile() throws CommandException {
        try {
            File csvData = new File(DATA_FILE_PATH);
            csvData.createNewFile();
        } catch (IOException ioe) {
            throw new CommandException(ioe.getMessage());
        }
    }

    private boolean fileExists() {
        File csvData = new File(DATA_FILE_PATH);
        return csvData.exists();
    }

    private void deleteFile() {
        File csvData = new File(DATA_FILE_PATH);
        csvData.delete();
    }

}
```
###### \main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case ExportCommand.COMMAND_WORD:
            return new ExportCommand();
```
###### \test\java\seedu\address\logic\commands\ExportCommandTest.java
``` java

package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteByNameCommand}.
 */

public class ExportCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addressBookExport_success() {
        ExportCommand exportCommand = prepareCommand();
        assertCommandSuccess(exportCommand, model, ExportCommand.MESSAGE_EXPORT_SUCCESS, model);
    }

    @Test
    public void execute_emptyAddressBookExport_throwsCommandException() throws Exception {
        List<ReadOnlyPerson> persons = model.getAddressBook().getPersonList();
        while (!persons.isEmpty()) {
            ReadOnlyPerson personToRemove = persons.get(0);
            model.deletePerson(personToRemove);
        }
        ExportCommand exportCommand = prepareCommand();
        assertCommandFailure(exportCommand, model, ExportCommand.MESSAGE_EMPTY_ADDRESS_BOOK);
    }

    /**
     * Returns a {@code ExportCommand}.
     */
    private ExportCommand prepareCommand() {
        ExportCommand exportCommand = new ExportCommand();
        exportCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return exportCommand;
    }
}
```
###### \test\java\systemtests\ExportCommandSystemTest.java
``` java

package systemtests;

import static seedu.address.logic.commands.ExportCommand.MESSAGE_EMPTY_ADDRESS_BOOK;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_EXPORT_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.model.Model;

public class ExportCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void exportCommand() {
        /*----------------- Performing export operation while an unfiltered list is being shown --------------------*/
        assertCommandSuccess();
        Index firstIndex = INDEX_FIRST_PERSON;
        selectPerson(firstIndex);
        assertCommandSuccess();

        /*----------------- Performing export operation while a filtered list is being shown --------------------*/

        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess();

        /*----------------- Performing export operation with an empty Address Book --------------------*/
        executeCommand(ClearCommand.COMMAND_WORD);
        assertCommandFailure(ExportCommand.COMMAND_WORD, MESSAGE_EMPTY_ADDRESS_BOOK);
    }

    /**
     * Exports current Address Book Data by creating a {@code ExportCommand} and performs the same verification as
     * {@code assertCommandSuccess(String, Model, String)}.
     * @see ExportCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess() {
        Model expectedModel = getModel();
        String expectedResultMessage = MESSAGE_EXPORT_SUCCESS;

        assertCommandSuccess(
                ExportCommand.COMMAND_WORD, expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteByNameCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
