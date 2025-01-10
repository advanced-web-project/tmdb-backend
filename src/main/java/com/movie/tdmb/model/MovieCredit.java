package com.movie.tdmb.model;

import lombok.Data;

import java.util.List;

@Data
public class MovieCredit {
    private List<MovieCast> cast;
    private List<MovieCrew> crew;
}
