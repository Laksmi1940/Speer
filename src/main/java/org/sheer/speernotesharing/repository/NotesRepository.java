package org.sheer.speernotesharing.repository;

import org.sheer.speernotesharing.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Note, Integer> {
}
