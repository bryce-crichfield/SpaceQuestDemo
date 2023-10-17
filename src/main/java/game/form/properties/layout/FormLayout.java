package game.form.properties.layout;

import game.Game;
import game.form.element.FormElement;
import game.form.properties.FormBounds;
import game.util.Util;

import java.util.List;

public interface FormLayout {
    void onLayout(FormElement parent, List<FormElement> children);
}
