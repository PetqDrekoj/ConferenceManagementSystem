package ro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ro.domain.Conference;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class ConferenceDto implements Serializable {
    private String title;
    private Conference conferenceName;
}
