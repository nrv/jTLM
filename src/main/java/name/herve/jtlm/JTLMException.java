package name.herve.jtlm;

public class JTLMException extends Exception {
	private static final long serialVersionUID = 8617680724685561829L;

	public JTLMException() {
		super();
	}

	public JTLMException(String message) {
		super(message);
	}

	public JTLMException(String message, Throwable cause) {
		super(message, cause);
	}

	public JTLMException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JTLMException(Throwable cause) {
		super(cause);
	}
}
