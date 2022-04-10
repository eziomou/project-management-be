package io.github.eziomou.pm.endpoint;

import javax.validation.constraints.NotBlank;

public class CreateProjectRequest {

    @NotBlank(message = "Name may not be blank")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
