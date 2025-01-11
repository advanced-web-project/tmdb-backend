package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPageResponseExpand {
    private int page;
    @JsonProperty("total_results")
    private long totalResults;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("per_page")
    private int perPage;
    private Object data;
}
