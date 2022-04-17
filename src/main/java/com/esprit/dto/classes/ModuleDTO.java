package com.esprit.dto.classes;

public class ModuleDTO {
    private String moduleId;
    private String designation;
    private String session;

    public String getModuleId() {
        return moduleId;
    }

    public ModuleDTO moduleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }

    public String getDesignation() {
        return designation;
    }

    public ModuleDTO designation(String designation) {
        this.designation = designation;
        return this;
    }

    public String getSession() {
        return session;
    }

    public ModuleDTO session(String session) {
        this.session = session;
        return this;
    }
}