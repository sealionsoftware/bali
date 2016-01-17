package com.sealionsoftware.bali.repl;

import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.StandardInterpreter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Map;


public class ExecuteController {

    @FXML private TextArea input;
    @FXML private Label output;

    private Interpreter interpreter = new StandardInterpreter();

    @FXML protected void execute() {

        try {
            Map<String, Object> values = interpreter.run(input.getText());

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : values.entrySet()){
                sb.append(entry.getKey()).append(": ").append(entry.getValue().toString());
            }

            output.setText(sb.toString());
        } catch (Exception e){
            output.setText(e.getMessage());
        }

    }

}