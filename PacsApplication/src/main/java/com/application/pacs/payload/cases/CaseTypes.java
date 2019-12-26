package com.application.pacs.payload.cases;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.application.pacs.model.CaseType;

import java.util.List;

public class CaseTypes {
    

    

    private List<CaseType> casetype;

    

    

    public List<CaseType> getCaseTypes() {
        return casetype;
    }

    public void setCaseType(List<CaseType> casetype) {
        this.casetype = casetype;
    }

   
}
