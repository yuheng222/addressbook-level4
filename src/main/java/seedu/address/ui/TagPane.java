package seedu.address.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Panel containing the list of all tags.
 */
public class TagPane extends UiPart<Region> {
    private static final String FXML = "TagPane.fxml";
    private static final Logger logger = LogsCenter.getLogger(TagPane.class);

    private static String[] colors = {"CornflowerBlue", "Tomato", "DarkSlateGray", "Crimson", "DarkBlue", "DarkGreen",
            "FireBrick", "OrangeRed", "Orchid", "blue", "Gold", "red", "MediumSeaGreen",
            "PaleVioletRed", "Peru", "RebeccaPurple", "RoyalBlue", "SeaGreen", "Coral"};
    private static HashMap<String, String> tagColors = new HashMap<String, String>();
    private static int colourIndex = 0;

    public final ObservableList<ReadOnlyPerson> personList;

    @FXML
    private FlowPane totalTags;

    public TagPane(ObservableList<ReadOnlyPerson> personList) {
        super(FXML);
        this.personList = personList;
        initTags(personList);
        bindListeners(personList);
    }

    /**
     * Assign a color to a tag if it does not have an existing color.
     * @return the color assigned to that tag
     */
    private static String getColorForTag(String tagValue) {
        if (!tagColors.containsKey(tagValue)) {
            tagColors.put(tagValue, colors[colourIndex]);
            updateColourIndex();
        }

        return tagColors.get(tagValue);
    }

    /**
     * update the index of colour
     */
    private static void updateColourIndex() {
        if (colourIndex == colors.length - 1) {
            colourIndex = 0;
        } else {
            colourIndex++;
        }
    }

    /**
     * Creates a tag label for every unique {@code Tag} and sets a color for each tag label.
     */
    private void initTags(ObservableList<ReadOnlyPerson> personList) {
        Set<Tag> tagList = new HashSet<>();
        for (ReadOnlyPerson person: personList) {
            person.getTags().forEach(tag -> {
                if (!tagList.contains(tag)) {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName) + ";");
                    totalTags.getChildren().add(tagLabel);
                    tagList.add(tag);
                }
            });
        }
    }

    /**
     * Binds the tags to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ObservableList<ReadOnlyPerson> personList) {
        for (ReadOnlyPerson person: personList) {
            person.tagProperty().addListener(new ChangeListener<UniqueTagList>() {
                @Override
                public void changed(ObservableValue<? extends UniqueTagList> observable, UniqueTagList oldValue, UniqueTagList newValue) {
                    totalTags.getChildren().clear();
                    initTags(personList);
                }
            });
        }
    }
}


