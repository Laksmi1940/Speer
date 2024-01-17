package org.sheer.speernotesharing.controller;

import org.aspectj.weaver.ast.Not;
import org.sheer.speernotesharing.entity.Note;
import org.sheer.speernotesharing.entity.User;
import org.sheer.speernotesharing.exceptions.NoteNotFoundException;
import org.sheer.speernotesharing.exceptions.UserNotFoundException;
import org.sheer.speernotesharing.repository.UserRepository;
import org.sheer.speernotesharing.service.SpeerServices;
import org.sheer.speernotesharing.service.UserService;
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

    @Autowired
    private UserService userService;

    /*
    GET /api/notes: get a list of all notes for the authenticated user.
    GET /api/notes/:id: get a note by ID for the authenticated user.
    POST /api/notes: create a new note for the authenticated user.
    PUT /api/notes/:id: update an existing note by ID for the authenticated user.
    DELETE /api/notes/:id: delete a note by ID for the authenticated user.

    POST /api/notes/:id/share: share a note with another user for the authenticated user.
    GET /api/search?q=:query: search for notes based on keywords for the authenticated user.
     */

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserPayload payload){
        User user = userService.signup(payload);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody UserPayload userPayload){
        String token = userService.login(userPayload);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/notes")
    public ResponseEntity<List<UserDto>> getAllNotes(@RequestParam String token){
        List<UserDto> usersNotes = speerServices.getAllNotes(token);
        return new ResponseEntity<>(usersNotes, HttpStatus.OK);
    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Note> getNoteByNoteId(@RequestParam String token, @PathVariable Integer noteId) throws NoteNotFoundException {
        Note note = speerServices.getNotesById(token, noteId);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @PostMapping("/notes")
    public ResponseEntity<User> addNote(@RequestParam String token, @RequestBody NotesPayload notesPayload) throws UserNotFoundException {
        User user = speerServices.addNotes(token, notesPayload);
        return  new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /*
    This block is in the older first version, uset to post a user
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

     */

    @PutMapping("/notes/{id}")
    public ResponseEntity<User> updateNote(@RequestParam String token, @PathVariable Integer id, @RequestBody NoteUpdatePayload noteUpdatePayload) throws NoteNotFoundException {
        User updatedUser = speerServices.updateNotes(id, noteUpdatePayload, token);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<String> deleteNote(@RequestParam String token, @PathVariable Integer id) throws NoteNotFoundException {
        String message = speerServices.deleteNote(token, id);
        return ResponseEntity.ok(message);
    }

    @PostMapping("notes/{id}/share")
    public ResponseEntity<List<User>> shareNote(@RequestParam String token, @PathVariable Integer id, @RequestBody SharingPayload payload) throws NoteNotFoundException {
        List<User> users = speerServices.shareNotes(token, id, payload);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteUpdatePayload>> getAllNotesByQuery(@RequestParam String token,@RequestParam String query){
        List<NoteUpdatePayload> notes = speerServices.finaAllNotes(token, query);

        return ResponseEntity.ok(notes);
    }

}
