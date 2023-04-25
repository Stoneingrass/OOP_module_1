package com.example.module1.controllers;

import com.example.module1.domain.Genre;
import com.example.module1.domain.Record;
import com.example.module1.repos.GenreRepo;
import com.example.module1.repos.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/records")
public class RecordController {

    private final RecordRepo recordRepo;
    private final GenreRepo genreRepo;

    @Autowired
    public RecordController(RecordRepo recordRepo, GenreRepo genreRepo) {
        this.recordRepo = recordRepo;
        this.genreRepo = genreRepo;
    }

    @GetMapping("/")
    public String showRecord(@RequestParam(name = "genreId", required = false) Long genreId, Map<String, Object> model) {
        List<Record> records;
        List<Genre> genres = genreRepo.findAll();

        if (genreId == null) {
            records = recordRepo.findAll();
        } else {
            Optional<Genre> genre = genreRepo.findById(genreId);
            if (genre.isEmpty()) {
                throw new IllegalArgumentException("Invalid genre id: " + genreId);
            }
            records = genre.get().getRecords();
        }

        model.put("records", records);
        model.put("genres", genres);
        model.put("genre_id", genreId);

        return "records";
    }

    @PostMapping("/add")
    public String addRecord(@RequestParam String song,
                          @RequestParam String performer,
                          @RequestParam(required = false) Double cost,
                          @RequestParam Long genre_id) {

        if (song.trim().isEmpty() || performer.trim().isEmpty()) {
            return "redirect:/records/";
        }

        Genre genre = genreRepo.findById(genre_id).orElseThrow(() -> new IllegalArgumentException("Invalid genre id: " + genre_id));

        Record record = new Record(song.trim(), performer.trim(), cost);
        record.getGenres().add(genre);
        recordRepo.save(record);

        return "redirect:/records/";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecord(@PathVariable("id") Long id) {
        recordRepo.deleteById(id);

        return "redirect:/records/";
    }

    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable("id") Long id, Map<String, Object> model) {
        Record record = recordRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record id: " + id));

        List<Genre> genres = genreRepo.findAll();

        model.put("record", record);
        model.put("genres", genres);

        return "record_edit";
    }

    @PostMapping("/save")
    public String saveRecord(@RequestParam Long id,
                           @RequestParam String song,
                           @RequestParam String performer,
                           @RequestParam(required = false) Double cost,
                           @RequestParam Long genre_id) {
        Record record = recordRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid record id:" + id));

        Genre genre = genreRepo.findById(genre_id).orElseThrow(() -> new IllegalArgumentException("Invalid genre id:" + genre_id));

        record.setSong(song.trim());
        record.setPerformer(performer.trim());
        record.setCost(cost);
        record.setCategory(genre);

        recordRepo.save(record);

        return "redirect:/records/";
    }
}