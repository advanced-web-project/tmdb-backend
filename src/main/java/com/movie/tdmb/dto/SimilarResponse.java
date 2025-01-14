package com.movie.tdmb.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimilarResponse {
    private int status;
    private Data data;
    @lombok.Data
    public class Data {
        private List<String> result;
    }
}
