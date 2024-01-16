package org.sheer.speernotesharing.speerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDtoSharing {
    private String userName;
    private String userPhone;
    private String noteTitle;
    private String noteBody;
}
