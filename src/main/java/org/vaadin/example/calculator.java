import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Route("/calc")

public class calculator extends VerticalLayout {

    private TextField display;
    private StringBuilder currentInput;

    public calculator() {
        display = new TextField();
        display.setReadOnly(true);
        display.setWidth("200px");

        currentInput = new StringBuilder();

        VerticalLayout buttonLayout = new VerticalLayout();

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", "C", "=", "+"}
        };

        for (String[] row : buttons) {
            VerticalLayout rowLayout = new VerticalLayout();
            for (String text : row) {
                Button button = new Button(text, event -> onButtonClick(text));
                button.setWidth("50px");
                rowLayout.add(button);
            }
            buttonLayout.add(rowLayout);
        }

        add(display, buttonLayout);
    }

    private void onButtonClick(String value) {
        if (value.equals("C")) {
            currentInput.setLength(0);
            display.setValue("");
        } else if (value.equals("=")) {
            calculateResult();
        } else {
            currentInput.append(value);
            display.setValue(currentInput.toString());
        }
    }

    private void calculateResult() {
        try {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String result = engine.eval(currentInput.toString()).toString();
            display.setValue(result);
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (ScriptException e) {
            display.setValue("Error");
        }
    }
}

