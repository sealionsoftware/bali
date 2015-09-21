package com.sealionsoftware.bali.repl;

import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.StandardInterpreter;
import com.sealionsoftware.bali.compiler.antlr.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.asm.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.Map;


public class ExecuteController {

    @FXML private TextArea input;
    @FXML private Label output;

    private Interpreter interpreter = new StandardInterpreter(
            new ANTLRParseEngine(),
            new MultithreadedAssemblyEngine(),
            new ASMBytecodeEngine(),
            new ReflectiveExecutor()
    );

    @FXML protected void execute(ActionEvent event) {
        Map<String, Object> values = interpreter.run(input.getText());
        output.setText(values.toString());
    }

}