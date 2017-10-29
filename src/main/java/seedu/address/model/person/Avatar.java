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
    public static final String DEFAULT_PATH = "images/default.png";
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
            File source = new File(DEFAULT_PATH);
            this.avatar = ImageIO.read(source);
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
