package io.github.eziomou.pm.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageResource<T> {

    @JsonProperty("data")
    private final List<T> data;

    @JsonProperty("_metadata")
    private final Metadata metadata;

    public PageResource(List<T> data, Metadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    public List<T> getData() {
        return data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public static class Metadata {

        public final long totalCount;

        public Metadata(long totalCount) {
            this.totalCount = totalCount;
        }

        public long getTotalCount() {
            return totalCount;
        }
    }
}
