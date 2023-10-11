package game.form.properties;

import game.form.element.FormElement;

import java.util.List;

public interface FormLayout {
    void execute(FormElement parent, List<FormElement> children, FormBounds margin);
}
