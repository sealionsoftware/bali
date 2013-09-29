package example;

import bali.Boolean;

import static bali.IdentityBoolean.TRUE;

public class WhileObject {

	public void goDo() {

		Boolean flag = TRUE;

		while (flag == TRUE) {
			flag = flag.not();
		}

	}

}
