package pl.speednet.drools.dto;

/**
 * This is a sample class to launch a rule.
 */
public class Messages {

	public static final Integer UNDEFINED = -1;
	public static final Integer HELLO = 0;
	public static final Integer GOODBYE = 1;

	private String message;

	private Integer status;
	
	public Messages() {
		this.message = "";
		this.status = UNDEFINED;
	}
	
	// getter and setters
	
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
