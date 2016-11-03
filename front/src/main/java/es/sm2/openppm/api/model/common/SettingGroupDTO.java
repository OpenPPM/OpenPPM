package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * Created by Javier on 09/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingGroupDTO extends EntityDTO {

    private HashMap<String, String> settingList;

    public SettingGroupDTO() {
    }

    public SettingGroupDTO(String name) {
        super(name);
    }

    public HashMap<String, String> getSettingList() {
        return settingList;
    }

    public void setSettingList(HashMap<String, String> settingList) {
        this.settingList = settingList;
    }
}
