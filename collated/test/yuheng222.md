# yuheng222
###### \java\seedu\address\logic\commands\SelectThemeCommand.java
``` java

package seedu.address.logic.commands;

import java.util.ArrayList;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.SelectThemeRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Selects a theme based on the index provided by the user, which can be referred from the themes list.
 */
public class SelectThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "st";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Switches the current theme to the theme identified by the index number in the themes list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SWITCH_THEME_SUCCESS = "Switched Theme: %1$s";

    private final Index targetIndex;

    public SelectThemeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() throws CommandException {

        ArrayList<String> themesList = model.getThemesList();

        if (targetIndex.getZeroBased() >= themesList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_THEME_DISPLAYED_INDEX);
        }

        String themeToChange = themesList.get(targetIndex.getZeroBased());

        EventsCenter.getInstance().post(new SelectThemeRequestEvent(themeToChange));

        return new CommandResult(String.format(MESSAGE_SWITCH_THEME_SUCCESS, themeToChange));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectThemeCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectThemeCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\SortCommand.java
``` java

package seedu.address.logic.commands;

/**
 * Sorts all persons in the address book lexicographically.
 */

public class SortCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons in the Address Book alphabetically by their name.";

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    @Override
    public CommandResult executeUndoableCommand() {
        model.sort();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\parser\ArgumentMultimap.java
``` java
    /**
     * Returns the last value of {@code prefix}.
     */
    public String getAvatarValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        if (values.isEmpty()) {
            return "";
        } else {
            return values.get((values.size() - 1));
        }
    }
```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
    /**
     * Parses {@code String avatar} into a {@code String<Avatar>} if {@code avatar} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code String<Avatar>} containing zero tags.
     */
    private Optional<Avatar> parseAvatarForEdit(String avatar) throws IllegalValueException, IOException {
        assert avatar != null;

        if (avatar.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseAvatar(avatar));
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> avatar} into an {@code Optional<Avatar>} if {@code avatar} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Avatar parseAvatar(String avatar) throws IllegalValueException, IOException {
        requireNonNull(avatar);
        return new Avatar(avatar);
    }
```
###### \java\seedu\address\logic\parser\SelectThemeCommandParser.java
``` java

package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SelectThemeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SwitchThemeCommand object
 */
public class SelectThemeCommandParser implements Parser<SelectThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SwitchThemeCommand
     * and returns an SwitchThemeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectThemeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectThemeCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectThemeCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Initialises the themes in this {@code AddressBook}.
     */

    private void initialiseThemes() {
        themes.add("MidnightTheme.css");
        themes.add("SummerTheme.css");
        themes.add("CoffeeTheme.css");
        themes.add("CrayonTheme.css");
    }

    public ArrayList<String> getThemesList() {
        return themes;
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /** Sorts the persons in this {@code AddressBook} lexicographically */

    public void sort() {
        persons.sort();
    }
```
###### \java\seedu\address\model\Model.java
``` java
    /** Sorts the persons in the AddressBook lexicographically */
    void sort();

    /** Returns the themes list */
    ArrayList<String> getThemesList();
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void sort() {
        addressBook.sort();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public ArrayList<String> getThemesList() {
        return this.addressBook.getThemesList();
    }
```
###### \java\seedu\address\model\person\Avatar.java
``` java

package seedu.address.model.person;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's avatar in the address book.
 */

public class Avatar {
    public static final String MESSAGE_AVATAR_CONSTRAINTS =
            "The file path must be valid and the avatar should be of correct image file format";
    public static final String DEFAULT_PATH = "default.png";
    private static final String AVATAR_VALIDATION_REGEX =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    public final String value;
    private BufferedImage avatar;

    /**
     * Sets avatar based on the given filepath.
     *
     * @throws IllegalValueException if the image filepath is invalid.
     */
    public Avatar(String path) throws IllegalValueException, IOException {
        if (path == null || (path.length()) == 0) {
            this.avatar = ImageIO.read(getClass().getClassLoader().getResource(DEFAULT_PATH));
            this.value = DEFAULT_PATH;
        } else if (!isValidAvatar(path)) {
            throw new IllegalValueException(MESSAGE_AVATAR_CONSTRAINTS);
        } else {
            try {
                File source = new File(path);
                this.avatar = ImageIO.read(source);
                this.value = path;
            } catch (IOException e) {
                throw new IllegalValueException(MESSAGE_AVATAR_CONSTRAINTS);
            }
        }
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    /**
     * Returns if a given string is a valid image file.
     */
    public static boolean isValidAvatar(String test) {
        return test.matches(AVATAR_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Avatar // instanceof handles nulls
                && this.value.equals(((Avatar) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setAvatar(Avatar avatar) {
        this.avatar.set(requireNonNull(avatar));
    }

    @Override
    public ObjectProperty<Avatar> avatarProperty() {
        return avatar;
    }

    @Override
    public Avatar getAvatar() {
        return avatar.get();
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Selects the theme given by user input.
     */
    public void handleSelectTheme(String theme) {
        if (getRoot().getStylesheets().size() > 1) {
            getRoot().getStylesheets().remove(CURRENT_THEME_INDEX);
        }
        getRoot().getStylesheets().add("/view/" + theme);
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    @Subscribe
    private void handleSelectThemeEvent(SelectThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleSelectTheme(event.theme);
    }
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     *  Sets the chosen Avatar for the specified person.
     */
    private void setAvatar(ReadOnlyPerson person) {

        BufferedImage avatar = person.getAvatar().getAvatar();
        Image image = SwingFXUtils.toFXImage(avatar, null);
        avatarDisplay.setImage(image);
    }
```
