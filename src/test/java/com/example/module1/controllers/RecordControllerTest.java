package com.example.module1.controllers;

import com.example.module1.domain.*;
import com.example.module1.domain.Record;
import com.example.module1.repos.*;
import com.example.module1.controllers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.ModelMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecordControllerTest {

    @Mock
    private RecordRepo recordRepo;
    @Mock
    private GenreRepo genreRepo;
    @InjectMocks
    private RecordController recordController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenreIdIsNull() {
        List<Record> records = List.of(new Record("Song", "Performer", 11111.0));
        List<Genre> genres = Collections.singletonList(new Genre("Genre"));
        when(recordRepo.findAll()).thenReturn(records);
        when(genreRepo.findAll()).thenReturn(genres);
        ModelMap model = new ModelMap();
        String result = recordController.showRecord(null, model);
        assertEquals("records", result);
        assertEquals(records, model.get("records"));
        assertEquals(genres, model.get("genres"));
        assertNull(model.get("genre_id"));
        verify(recordRepo).findAll();
        verify(genreRepo).findAll();
    }

    @Test
    public void testGenreDoesNotExist() {
        when(genreRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> recordController.showRecord(5L, new ModelMap()));
        verify(genreRepo).findById(5L);
    }

    @Test
    public void testAddRecord() {
        when(genreRepo.findById(5L)).thenReturn(Optional.of(new Genre("Genre")));
        Record record = new Record("Song", "Performer", 11111.0);
        record.setId(5L);
        when(recordRepo.save(any(Record.class))).thenReturn(record);
        String result = recordController.addRecord("Title", "Author", 11111.0, 5L);
        assertEquals("redirect:/records/", result);
        verify(genreRepo).findById(5L);
        verify(recordRepo).save(any(Record.class));
    }

    @Test
    public void testAddRecordInvalidInput() {
        String emptySong = "";
        String emptyPerformer = "";
        Double price = 11111.0;
        Long genreId = 5L;
        assertEquals("redirect:/records/", recordController.addRecord(emptySong, emptyPerformer, price, genreId));
        String song = "Record Title";
        String performer = "Record Author";
        final Long finalGenreId = 666L;
        assertThrows(IllegalArgumentException.class, () -> recordController.addRecord(song, performer, price, finalGenreId));
    }

    @Test
    public void testDeleteRecord(){
        Record record = new Record();
        record.setId(10L);
        when(recordRepo.findById(10L)).thenReturn(Optional.of(record));
        recordController.deleteRecord(10L);
        verify(recordRepo, times(1)).deleteById(10L);
    }

    @Test
    public void testDeleteRecordInvalidInput() {
        Long invalidId = 66666L;
        doThrow(EmptyResultDataAccessException.class).when(recordRepo).deleteById(invalidId);
        assertThrows(EmptyResultDataAccessException.class, () -> recordController.deleteRecord(invalidId));
        verify(recordRepo, times(1)).deleteById(invalidId);
    }
}