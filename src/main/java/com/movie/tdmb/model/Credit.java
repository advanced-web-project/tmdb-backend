package com.movie.tdmb.model;

import lombok.Data;

import java.util.List;

@Data
public class Credits {
    private long id;
    private List<Cast> cast;
    private List<Crew> crew;
    private String _id;
}
