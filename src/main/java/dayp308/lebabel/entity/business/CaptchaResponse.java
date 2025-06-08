package dayp308.lebabel.entity.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResponse {
    String img;
    String uuid;
}
