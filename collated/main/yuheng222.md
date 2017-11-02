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
###### \resources\view\CoffeeTheme.css
``` css

.background {
    -fx-background-color: derive(#f5f5f5, 20%);
    background-color: #fafafa; /* Used in the default.html file (google window) */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #1d1d1d;
    -fx-control-inner-background: #1d1d1d;
    -fx-background-color: #1d1d1d;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#d3d3d3, 20%);
    -fx-border-color: transparent transparent transparent transparent;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#fafafa, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-cell {
    -fx-background-color: #724706 ;
    -fx-label-padding: 0 0 5 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 5 0;
}

.list-cell:filled:even {
    -fx-background-color: #724706;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:odd {
    -fx-background-color: #c17b13;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected {
    -fx-background-color: #442901;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #4f2f03;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: white;
}

.cell_big_label {
    -fx-font-family: "Avenir Next";
    -fx-font-size: 25px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Futura";
    -fx-font-size: 20px;
    -fx-text-fill: #010504;
}

.anchor-pane {
    -fx-background-color: derive(#f2f1f1, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#8c6125, 20%);
     -fx-border-color: derive(#8c6125, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#8c6125, 30%);
}

.context-menu {
    -fx-background-color: derive(#1d1d1d, 50%);
}

.context-menu .label {
    -fx-text-fill: white;
}

.menu-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #20b2aa;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: #fafafa;
}

.scroll-bar .thumb {
    -fx-background-color: derive(#d3d3d3, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#browserPlaceholder {
    -fx-background-color: #ffffff;
}


#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #383838 transparent #383838;
    -fx-background-insets: 0;
    -fx-border-color: transparent transparent transparent transparent;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, transparent, #f2f1f1, transparent;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
###### \resources\view\MidnightTheme.css
``` css

.background {
    -fx-background-color: derive(#1d1d1d, 20%);
    background-color: #383838; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #1d1d1d;
    -fx-control-inner-background: #1d1d1d;
    -fx-background-color: #1d1d1d;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-border-color: transparent transparent transparent #4d4d4d;
}

.split-pane {
    -fx-border-radius: 7;
    -fx-border-width: 1;
    -fx-background-color: derive(#1d1d1d, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-cell {
    -fx-background-color: #474747 ;
    -fx-label-padding: 0 0 5 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 5 0;
    -fx-border-width: 3px;
}

.list-cell:filled:even {
    -fx-background-color: #474747;
    -fx-background-radius: 1;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:odd {
    -fx-background-color: #515658;
    -fx-background-radius: 1;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected {
    -fx-background-color: #424d5f;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #3e7b91;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: white;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 25px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 20px;
    -fx-text-fill: #010504;
}

.anchor-pane {
     -fx-background-color: derive(#1d1d1d, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#1d1d1d, 20%);
     -fx-border-color: derive(#1d1d1d, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-background-radius: 8;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: white;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
}

.status-bar-with-border {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: white;
}

.grid-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
}

.context-menu {
    -fx-background-color: derive(#1d1d1d, 50%);
}

.context-menu .label {
    -fx-text-fill: white;
}

.menu-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #3a3a3a;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#1d1d1d, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #383838 transparent #383838;
    -fx-background-insets: 0;
    -fx-border-color: #383838 #383838 #ffffff #383838;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 14pt;
    -fx-text-fill: white;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #383838, transparent, #383838;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
###### \resources\view\SummerTheme.css
``` css

.background {
    -fx-background-color: derive(#f5f5f5, 20%);
    background-color: #fafafa; /* Used in the default.html file (google window) */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #1d1d1d;
    -fx-control-inner-background: #1d1d1d;
    -fx-background-color: #1d1d1d;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#d3d3d3, 20%);
    -fx-border-color: transparent transparent transparent transparent;
}

.split-pane {
    -fx-border-radius: 7;
    -fx-border-width: 1;
    -fx-background-color: derive(#fafafa, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-cell {
    -fx-background-color: #85fcf0;
    -fx-label-padding: 0 0 5 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 5 0;
}

.list-cell:filled:even {
    -fx-background-color: #85fcf0;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:odd {
    -fx-background-color: #5ef4f9;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected {
    -fx-background-color: #eaea5b;
    -fx-border-color: transparent transparent #dcdcdc transparent;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #eaea5b;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: black;
}

.cell_big_label {
    -fx-font-family: "Georgia";
    -fx-font-size: 25px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "PT Sans Caption";
    -fx-font-size: 20px;
    -fx-text-fill: #010504;
}

.anchor-pane {
    -fx-background-color: derive(#f2f1f1, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#fffdbc, 20%);
     -fx-border-color: derive(#fffdbc, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#fffdbc, 30%);
}

.context-menu {
    -fx-background-color: derive(#1d1d1d, 50%);
}

.context-menu .label {
    -fx-text-fill: white;
}

.menu-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #20b2aa;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: #fafafa;
}

.scroll-bar .thumb {
    -fx-background-color: derive(#d3d3d3, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #383838 transparent #383838;
    -fx-background-insets: 0;
    -fx-border-color: transparent transparent transparent transparent;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, transparent, #f2f1f1, transparent;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
