package dayp308.lebabel.bean.ao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {
    @NotNull
    private Integer categoryId;
    @NotBlank
    @Size(max = 192)
    private String title;
    @NotBlank
    private String content;
}
