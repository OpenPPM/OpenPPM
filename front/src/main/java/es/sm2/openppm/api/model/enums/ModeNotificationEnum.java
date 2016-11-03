package es.sm2.openppm.api.model.enums;

public enum ModeNotificationEnum {

	APPLICATION("APPLICATION"),
    EMAIL("EMAIL"),
    BOTH("BOTH");
	
	private String mode;
	
	private ModeNotificationEnum(String mode) {
		this.mode = mode;
	}
	
	public String getMode() {
		return this.mode;
	}
}
