package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of all tags.
 */
public class TagPane extends UiPart<Region> {
    private static final String FXML = "TagPane.fxml";
    private static final Logger logger = LogsCenter.getLogger(TagPane.class);

    public final ObjectProperty<ObservableList<Tag>> tagList;

    @FXML
    private FlowPane totalTags;

    public TagPane(ObservableList<Tag> tagListCopy) {
        super(FXML);
        tagList = new SimpleObjectProperty<ObservableList<Tag>>(tagListCopy);
        initTags();
        bindListeners();
    }

    /**
     * Creates a tag label for every unique {@code Tag} and sets a color for each tag label.
     */
    private void initTags() {
        for (Tag tag: tagList.get()) { 
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + tag.tagColour + ";");
            totalTags.getChildren().add(tagLabel);
        }
    }

    /**
     * Binds the tags to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners() {
        tagList.addListener((v, oldValue, newValue) -> {
            totalTags.getChildren().clear();
            initTags();
        });
    }
}


