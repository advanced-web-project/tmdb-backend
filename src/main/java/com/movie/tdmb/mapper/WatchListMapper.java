package com.movie.tdmb.mapper;

import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.model.WatchList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WatchListMapper {
    WatchListMapper INSTANCE = Mappers.getMapper(WatchListMapper.class);
    ResponseWatchListDTO watchListToResponseWatchListDTO(WatchList watchList);
}
