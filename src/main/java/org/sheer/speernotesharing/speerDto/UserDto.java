package org.sheer.speernotesharing.speerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sheer.speernotesharing.entity.Note;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String userPhone;
    private List<Note> notes = new ArrayList<>();
}
