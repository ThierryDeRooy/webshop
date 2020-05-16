package com.webshop.model;

import com.webshop.constants.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.TreeMap;

@Setter
@Getter
public class CategoryDisplay {

    @NotNull(message = "fill in Category code")
    @Size(min=1,max=6,message="Invalid length for Category code")
    private String code;
    private String upperCatCode;
    private Map<String, String> names = new TreeMap<>();
    private Map<String, Long> ids = new TreeMap<>();

    public CategoryDisplay() {
        for (String lang : Constants.LANGUAGES) {
            names.put(lang, "");
            getIds().put(lang, null);
        }
    }

    public CategoryDisplay(String code, String upperCatCode, String lang, String name, Long id) {
        setCode(code);
        setUpperCatCode(upperCatCode);
        names.put(lang, name);
        ids.put(lang, id);
    }


}
