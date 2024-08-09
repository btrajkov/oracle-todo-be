package oracle.todo.model.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemJSON {
    private Long id;
    private String name;
    private String date;
    private Long customerId;
    private Long categoryId;
    private String status;
}
