package com.example.module1.controllers;

import com.example.module1.domain.Genre;
import com.example.module1.repos.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreRepo genreRepo;

    @Autowired
    public GenreController(GenreRepo genreRepo){
        this.genreRepo = genreRepo;
    }

    @GetMapping("/")
    public String showGenre(Map<String, Object> model) {
        Iterable<Genre> genres = genreRepo.findAll();

        model.put("genres", genres);

        return "genres";
    }

    @PostMapping("/add")
    public String addGenre(@RequestParam String title){
        if(title.trim().isEmpty()){
            return "redirect:/genres/";
        }
        Genre genre = new Genre(title.trim());
        genreRepo.save(genre);

        return "redirect:/genres/";
    }

    @PostMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") Long id){
        genreRepo.deleteById(id);

        return "redirect:/genres/";
    }

    @GetMapping("/edit/{id}")
    public String editGenre(@PathVariable("id") Long id, Map<String, Object> model){
        Genre genre = genreRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Genre id: " + id));

        model.put("genre", genre);

        return "genres_edit";
    }

    @PostMapping("/save")
    public String saveGenre(@RequestParam Long id,
                            @RequestParam String title){
        Genre genre = genreRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid genre id: " + id));

        genre.setTitle(title.trim());

        genreRepo.save(genre);

        return "redirect:/genres/";
    }

}
