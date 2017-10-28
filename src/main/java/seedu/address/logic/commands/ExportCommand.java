package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Exports the current Address Book data into a CSV file.
 */
public class ExportCommand extends Command {

    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "Export failed. Current Address Book is empty.";

    public static final String DATA_FILE_PATH = "AddressBookData.csv";

    //public static final String MESSAGE_IO_EXCEPTION = "Read/Write Exception Occurred";

    private final List<ReadOnlyPerson> currentData;

    public ExportCommand() { this.currentData = model.getAddressBook().getPersonList(); }

    @Override
    public CommandResult execute() throws CommandException {

        if (currentData.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_ADDRESS_BOOK);
        }

        if (fileExists()) {
            deleteFile();
        }

        createFile();

        writeData();

        return null;
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

        int lastChar = builder.lastIndexOf(",");
        builder.setCharAt(lastChar, '\n');

        return builder.toString();
    }

    /** Function to write data to the CSV */
    private void writeData() throws CommandException {
        File csvData = new File(DATA_FILE_PATH);
        String headers = constructHeaders();
        try {
            PrintWriter pw = new PrintWriter(csvData);
            pw.write(headers);
        } catch (IOException ioe) {
            throw new CommandException(ioe.getMessage());
        }

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
