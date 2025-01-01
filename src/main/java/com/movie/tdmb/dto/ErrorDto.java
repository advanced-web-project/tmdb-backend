package com.movie.tdmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private Date timestamp;
    private int status;
    private String path;
    private List<String> errors = new ArrayList<>();

    /**
     * Adds an error message to the list of errors.
     *
     * @param message the error message to add
     */
    public void addError(String message) {
        errors.add(message);
    }
}
