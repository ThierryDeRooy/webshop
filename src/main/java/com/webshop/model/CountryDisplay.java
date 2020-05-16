package com.webshop.model;

import com.webshop.constants.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.TreeMap;

public class CountryDisplay {

    @NotNull(message = "fill in country code")
    @Size(min=1,max=5,message="Invalid length for Country code")
    private String code;
    private Map<String, String> names = new TreeMap<>();
    private Map<String, Long> ids = new TreeMap<>();

    public CountryDisplay() {
        for (String lang : Constants.LANGUAGES) {
            names.put(lang, "");
            getIds().put(lang, null);
        }
    }

    public CountryDisplay(String code, String lang, String name, Long id) {
        setCode(code);
        names.put(lang, name);
        ids.put(lang, id);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public Map<String, Long> getIds() {
        return ids;
    }

    public void setIds(Map<String, Long> ids) {
        this.ids = ids;
    }
}
