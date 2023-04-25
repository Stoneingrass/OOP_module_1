package com.example.module1.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="song")
    private String song;
    @Column(name="performer")
    private String performer;
    @Column(name = "cost")
    private double cost;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "records_genres",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    public List<Genre> getGenres() {
        return genres;
    }

    public void setCategory(Genre genre) {
        this.genres.clear();
        this.genres.add(genre);
    }

    public Record(){}

    public Record(String song, String performer, double cost){
        this.song = song;
        this.performer = performer;
        this.cost = cost;
    }

    public void setSong(String title) {
        this.song = title;
    }

    public void setPerformer(String author) {
        this.performer = author;
    }

    public void setCost(double price) {
        this.cost = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
