/**
 * 
 */
package es.sm2.openppm.api.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.enums.StatusNotificationEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @author Dani
 *
 */
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO extends EntityDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8413760970072660946L;

	private String key;

	private StatusNotificationEnum status;
	
	private String distributionList;
	
	private List<NotificationProfileDTO> profiles;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
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
	 * @return the distributionList
	 */
	public String getDistributionList() {
		return distributionList;
	}

	/**
	 * @param distributionList the distributionList to set
	 */
	public void setDistributionList(String distributionList) {
		this.distributionList = distributionList;
	}

	/**
	 * @return the profiles
	 */
	public List<NotificationProfileDTO> getProfiles() {
		return profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<NotificationProfileDTO> profiles) {
		this.profiles = profiles;
	}
}
