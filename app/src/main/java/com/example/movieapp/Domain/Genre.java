
package com.example.movieapp.Domain;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Genre {
private List<GenreItem> Genres;

    public List<GenreItem> getGenres() {
        return Genres;
    }

    public void setGenres(List<GenreItem> genres) {
        Genres = genres;
    }
}
