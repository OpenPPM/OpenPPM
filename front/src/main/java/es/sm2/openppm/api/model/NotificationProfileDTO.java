package es.sm2.openppm.api.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.enums.ModeNotificationEnum;
import es.sm2.openppm.api.model.enums.StatusNotificationEnum;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationProfileDTO extends EntityDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1669759063720566272L;
	
	private StatusNotificationEnum status;
	private Boolean readOnly;
	private ModeNotificationEnum mode;
	private EntityDTO profile;

	/**
	 * @return the status
	 */
	public StatusNotificationEnum getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusNotificationEnum status) {
		this.status = status;
	}

	/**
	 * @return the readOnly
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the mode
	 */
	public ModeNotificationEnum getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(ModeNotificationEnum mode) {
		this.mode = mode;
	}

	/**
	 * @return the profile
	 */
	public EntityDTO getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(EntityDTO profile) {
		this.profile = profile;
	}
}
