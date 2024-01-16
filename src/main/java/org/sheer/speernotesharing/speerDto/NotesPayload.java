package org.sheer.speernotesharing.speerDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sheer.speernotesharing.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotesPayload {
    private String noteTitle;
    private String noteBody;
    private Integer userId;
}
