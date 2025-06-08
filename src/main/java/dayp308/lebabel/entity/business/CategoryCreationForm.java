package dayp308.lebabel.entity.business;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreationForm {
    @NotBlank
    private String name;
    private String avatar;
    @NotBlank
    private String info;
}
