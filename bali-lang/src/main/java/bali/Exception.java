package bali;

/**
 * User: Richard
 * Date: 07/07/13
 */
public class Exception extends RuntimeException {

	private String problem;

	public Exception(String problem) {
		this.problem = problem;
	}

	public String getProblem() {
		return problem;
	}
}
