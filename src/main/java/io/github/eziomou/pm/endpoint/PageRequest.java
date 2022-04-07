package io.github.eziomou.pm.endpoint;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PageRequest {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(value = 0, message = "Page must be greater or equal 0")
    private int page;

    @QueryParam("size")
    @DefaultValue("30")
    @Min(value = 10, message = "Size must be greater or equal 10")
    @Max(value = 100, message = "Size must be lower or equal 100")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
