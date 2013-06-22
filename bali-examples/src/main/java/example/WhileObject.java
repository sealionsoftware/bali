package example;

import bali.Boolean;

import static bali.Boolean.TRUE;

public class WhileObject {

	public void goDo() {

		Boolean flag = TRUE;

		while (flag == TRUE) {
			flag = flag.not();
		}

	}

}
