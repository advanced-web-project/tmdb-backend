package com.movie.tdmb.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class MovieCredit {
    @Getter
    private List<MovieCast> cast;
    private List<MovieCrew> crew;

}
