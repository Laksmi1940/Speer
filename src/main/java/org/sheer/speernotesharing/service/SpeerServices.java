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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpeerServices {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllNotes(String token) {

        String userName = token.substring(20);

        List<User> userList = userRepository.findAll();

        User user = new User();

        return userList.stream().filter(i -> i.getUserName().equals(userName))
                .map(j -> new UserDto(j.getUserName(), j.getUserPhone(), j.getNotes()))
                .collect(Collectors.toList());
    }

    public Note getNotesById(String token, Integer noteId) throws NoteNotFoundException {

        String userName = token.substring(20);

        List<User> userList = userRepository.findAll();

        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                List<Note> allNotes = notesRepository.findAll();
                for (Note n : allNotes) {
                    if (n.getNoteId() == noteId) {
                        return n;
                    }
                }
            }
        }

        throw new NoteNotFoundException("Invlaid note id");
    }

    public User addNotes(String token, NotesPayload notesPayload) throws UserNotFoundException {

        String userName = token.substring(20);
        List<User> userList = userRepository.findAll();


        for (User u : userList) {
            if (u.getUserName().equals(userName)) {

                Note note = new Note();
                note.setNoteBody(notesPayload.getNoteBody());
                note.setNoteTitle(notesPayload.getNoteTitle());
                note.setUsers(List.of(u));

                if (u.getNotes() == null) u.setNotes(List.of(note));
                else u.getNotes().add(note);

                userRepository.save(u);
                //notesRepository.save(note);

                return u;
            }
        }
        throw new UserNotFoundException("User is not available..Check Token");
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

    public User updateNotes(Integer id, NoteUpdatePayload noteUpdatePayload, String token) throws NoteNotFoundException {

        String userName = token.substring(20);
        List<User> userList = userRepository.findAll();


        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                List<Note> notes = u.getNotes();
                for (Note note : notes) {
                    if (note.getNoteId() == id) {
                        note.setNoteBody(noteUpdatePayload.getNoteBody());
                        note.setNoteTitle(noteUpdatePayload.getNoteTitle());

                        return userRepository.save(u);
                    }
                }
            }
        }

        throw new NoteNotFoundException("Invalid note id. Check check payload");
    }

    public String deleteNote(String token, Integer id) throws NoteNotFoundException {
        String userName = token.substring(20);
        List<User> userList = userRepository.findAll();

        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                List<Note> notes = u.getNotes();
                for (Note note : notes) {
                    if (note.getNoteId().equals(id)) {
                        notes.remove(note);
                        u.setNotes(notes);
                        //notesRepository.deleteById(id);
                        note.getUsers().remove(u);

                        userRepository.save(u);

                        notesRepository.save(note);
                        return "Note is successfully deleted... ";
                    }
                }
            }
        }

        throw new NoteNotFoundException("Invalid note id. Check check payload");
    }

    public List<User> shareNotes(String token, Integer id, SharingPayload payload) throws NoteNotFoundException {

        List<User> updatedUsers = new ArrayList<>();

        String userName = token.substring(20);
        List<User> userList = userRepository.findAll();

        Note note = notesRepository.findById(id).get();

        List<Integer> usersIds = payload.getUserId();

        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                for(Integer userId : usersIds){
                    User userTobeUpdated = userRepository.findById(userId).get();
                    userTobeUpdated.getNotes().add(note);
                    userRepository.save(u);

                    note.getUsers().add(userTobeUpdated);
                    notesRepository.save(note);
                    updatedUsers.add(userTobeUpdated);
                }

//                List<Note> notes = u.getNotes();
//                notes.add(note);
//
//                updatedUsers = updateUserNotes(usersIds, id);
            }
        }

        return updatedUsers;

    }

    private List<User> updateUserNotes(List<Integer> usersIds, Integer id) {
        List<User> updatedUsers = new ArrayList<>();
        Note note = notesRepository.findById(id).get();
        List<User> userList = userRepository.findAll();
        for (Integer i : usersIds) {
            for (User u : userList) {
                if (i == u.getUserId()) {
                    List<Note> notes = u.getNotes();
                    notes.add(note);
                    u.setNotes(notes);
                    userRepository.save(u);
                    updatedUsers.add(u);
                }
            }
        }
        return updatedUsers;
    }

    public List<NoteUpdatePayload> finaAllNotes(String token, String query) {
        List<User> updatedUsers = new ArrayList<>();

        String userName = token.substring(20);
        List<User> userList = userRepository.findAll();

        List<Note> allNotes = notesRepository.findAll();
        List<NoteUpdatePayload> filteredNotes = new ArrayList<>();
        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                filteredNotes = allNotes.stream()
                        .filter(i -> i.getNoteBody().contains(query))
                        .map(j -> new NoteUpdatePayload(j.getNoteTitle(), j.getNoteBody()))
                        .collect(Collectors.toList());
            }
        }
        return filteredNotes;
    }
}