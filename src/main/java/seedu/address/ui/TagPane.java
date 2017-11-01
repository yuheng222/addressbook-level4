package seedu.address.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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

    public final ObservableList<Tag> tagList;

    @FXML
    private FlowPane totalTags;

    public TagPane(ObservableList<Tag> tagListCopy) {
        super(FXML);
        tagList = tagListCopy;
        initTags();
        bindListeners();
    }

    /**
     * Creates a tag label for every unique {@code Tag} and sets a color for each tag label.
     */
    private void initTags() {
        for (Tag tag: tagList) { 
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
        /*ObjectProperty<ObservableList<Tag>> tagListProperty = new SimpleObjectProperty<ObservableList<Tag>>(tagList);
        tagListProperty.addListener((observable, oldValue, newValue) -> {
            totalTags.getChildren().clear();
            initTags(tagList);
        });*/
        tagList.addListener(new ListChangeListener<Tag>() {
            @Override
            public void onChanged(Change<? extends Tag> c) {
                totalTags.getChildren().clear();
                initTags();
            }
        });
    }
}


