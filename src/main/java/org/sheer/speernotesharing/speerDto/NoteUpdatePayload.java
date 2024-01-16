package org.sheer.speernotesharing.speerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteUpdatePayload {
    private String noteTitle;
    private String noteBody;
}
