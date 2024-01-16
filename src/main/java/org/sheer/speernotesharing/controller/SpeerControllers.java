package org.sheer.speernotesharing.controller;

import org.aspectj.weaver.ast.Not;
import org.sheer.speernotesharing.entity.Note;
import org.sheer.speernotesharing.entity.User;
import org.sheer.speernotesharing.exceptions.NoteNotFoundException;
import org.sheer.speernotesharing.exceptions.UserNotFoundException;
import org.sheer.speernotesharing.repository.UserRepository;
import org.sheer.speernotesharing.service.SpeerServices;
import org.sheer.speernotesharing.speerDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpeerControllers {
    @Autowired
    private SpeerServices speerServices;

    /*
    GET /api/notes: get a list of all notes for the authenticated user.
    GET /api/notes/:id: get a note by ID for the authenticated user.
    POST /api/notes: create a new note for the authenticated user.
    PUT /api/notes/:id: update an existing note by ID for the authenticated user.
    DELETE /api/notes/:id: delete a note by ID for the authenticated user.

    POST /api/notes/:id/share: share a note with another user for the authenticated user.
    GET /api/search?q=:query: search for notes based on keywords for the authenticated user.
     */

    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes(){
        List<Note> notes = speerServices.getAllNotes();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Note> getNoteByNoteId(@PathVariable Integer noteId) throws NoteNotFoundException {
        Note note = speerServices.getNotesById(noteId);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> addNote(@RequestBody NotesPayload notesPayload) throws UserNotFoundException {
        Note note = speerServices.addNotes(notesPayload);
        return  new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto){

        User newUser = speerServices.addUser(userDto);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> usersList = speerServices.getAllUsers();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Integer id, @RequestBody NoteUpdatePayload noteUpdatePayload) throws NoteNotFoundException {
        Note updatedNote = speerServices.updateNotes(id, noteUpdatePayload);

        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Integer id) throws NoteNotFoundException {
        String message = speerServices.deleteNote(id);
        return ResponseEntity.ok(message);
    }

    @PostMapping("notes/{id}/share")
    public ResponseEntity<List<ResponseDtoSharing>> shareNote(@PathVariable Integer id, @RequestBody SharingPayload payload) throws NoteNotFoundException {
        List<ResponseDtoSharing> shaedNotes = speerServices.shareNotes(id, payload);

        return ResponseEntity.ok(shaedNotes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteUpdatePayload>> getAllNotesByQuery(@RequestParam String query){
        List<NoteUpdatePayload> notes = speerServices.finaAllNotes(query);

        return ResponseEntity.ok(notes);
    }

}
