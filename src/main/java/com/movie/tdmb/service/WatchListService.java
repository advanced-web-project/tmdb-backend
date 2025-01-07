package com.movie.tdmb.service;

import com.movie.tdmb.dto.RequestWatchListDTO;
import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.mapper.WatchListMapper;
import com.movie.tdmb.model.WatchList;
import com.movie.tdmb.repository.UserRepository;
import com.movie.tdmb.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchListService {
    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseWatchListDTO addWatchlist(RequestWatchListDTO request, String userId) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found"));
        WatchList watchlist = new WatchList();
        watchlist.setMovieId(request.getMovieId());
        watchlist.setUserId(userId);
        watchlist.setAddedAt(new Date());
        WatchList savedWatchlist = watchListRepository.save(watchlist);
        return WatchListMapper.INSTANCE.watchListToResponseWatchListDTO(savedWatchlist);
    }

    public List<ResponseWatchListDTO> getWatchlistByUser(String userId) {
        List<WatchList> watchlists = watchListRepository.findByUserId(userId);
        return watchlists.stream()
                .map(WatchListMapper.INSTANCE::watchListToResponseWatchListDTO)
                .collect(Collectors.toList());
    }

    public void removeWatchlist(String id) {
        watchListRepository.deleteById(id);
    }
}
