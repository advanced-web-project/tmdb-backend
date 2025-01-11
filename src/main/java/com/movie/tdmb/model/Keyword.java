package com.movie.tdmb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Keyword {
    @Field("id")
    private Integer id;
    private String name;
}
