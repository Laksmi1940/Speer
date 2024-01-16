package org.sheer.speernotesharing.speerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharingPayload {
    private List<Integer> userId;
}
