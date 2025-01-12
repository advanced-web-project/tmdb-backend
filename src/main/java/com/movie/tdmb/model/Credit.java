package com.movie.tdmb.model;

import lombok.Data;

import java.util.List;

@Data
public class Credit {
    private List<Cast> cast;
    private List<Crew> crew;
}
