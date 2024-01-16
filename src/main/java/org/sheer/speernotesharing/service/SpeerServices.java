package org.sheer.speernotesharing.service;

import org.sheer.speernotesharing.entity.Note;
import org.sheer.speernotesharing.entity.User;
import org.sheer.speernotesharing.exceptions.NoteNotFoundException;
import org.sheer.speernotesharing.exceptions.UserNotFoundException;
import org.sheer.speernotesharing.repository.NotesRepository;
import org.sheer.speernotesharing.repository.UserRepository;
import org.sheer.speernotesharing.speerDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpeerServices {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Note> getAllNotes() {
        return notesRepository.findAll();
    }

    public Note getNotesById(Integer noteId) throws NoteNotFoundException {

        return notesRepository.findById(noteId).orElseThrow(() -> new NoteNotFoundException("Note is not found with the given Id"));
    }

    public Note addNotes(NotesPayload notesPayload) throws UserNotFoundException {

        Integer userId = notesPayload.getUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User is not found"));

        Note note = new Note();
        note.setNoteBody(notesPayload.getNoteBody());
        note.setNoteTitle(notesPayload.getNoteTitle());
        note.setUsers(List.of(user));

        Note newNote = notesRepository.save(note);
//
//        List<Note> userNotes = user.getNotes();
//        userNotes.add(newNote);
//
//
//        user.setNotes(userNotes);
//        userRepository.save(user);

        return newNote;

    }

    public User addUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setUserPhone(userDto.getUserPhone());
        user.setNotes(userDto.getNotes());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Note updateNotes(Integer id, NoteUpdatePayload noteUpdatePayload) throws NoteNotFoundException {
        Note note = notesRepository.findById(id).orElseThrow(()->new NoteNotFoundException("Invalid note id"));

        note.setNoteTitle(noteUpdatePayload.getNoteTitle());
        note.setNoteBody(noteUpdatePayload.getNoteBody());

        return notesRepository.save(note);
    }

    public String deleteNote(Integer id) throws NoteNotFoundException {
        Note note = notesRepository.findById(id).orElseThrow(()->new NoteNotFoundException("Invalid note id"));

        notesRepository.deleteById(id);

        return "Note  " + note.getNoteTitle() + " is successfully deleted... ";
    }

    public List<ResponseDtoSharing> shareNotes(Integer id, SharingPayload payload) throws NoteNotFoundException {

        List<ResponseDtoSharing> resposeList = new ArrayList<>();

        Note note = notesRepository.findById(id).orElseThrow(()-> new NoteNotFoundException("Invalid note id"));

        List<Integer> userList = payload.getUserId();

        for(Integer i : userList) {

            User user = userRepository.findById(i).get();
            List<Note> notes = user.getNotes();
            notes.add(note);
            //userRepository.save(user);

            List<User> users = note.getUsers();
            users.add(user);
            note.setUsers(users);

            notesRepository.save(note);

            ResponseDtoSharing sharing = new ResponseDtoSharing();
            sharing.setNoteBody(note.getNoteBody());
            sharing.setNoteTitle(note.getNoteTitle());
            sharing.setUserName(user.getUserName());
            sharing.setUserPhone(user.getUserPhone());

            resposeList.add(sharing);
        }
        return resposeList;

    }

    public List<NoteUpdatePayload> finaAllNotes(String query) {
        List<Note> allNotes = notesRepository.findAll();
        List<NoteUpdatePayload> filteredNotes = allNotes.stream()
                .filter(i -> i.getNoteTitle().startsWith(query))
                .map(j -> new NoteUpdatePayload(j.getNoteTitle(), j.getNoteBody()))
                .collect(Collectors.toList());

        return filteredNotes;

    }
}
