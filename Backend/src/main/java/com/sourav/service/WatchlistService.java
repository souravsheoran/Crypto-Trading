package com.sourav.service;

import com.sourav.modal.Coin;
import com.sourav.modal.User;
import com.sourav.modal.Watchlist;

public interface WatchlistService {
    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(User user);
    Watchlist findByid(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
