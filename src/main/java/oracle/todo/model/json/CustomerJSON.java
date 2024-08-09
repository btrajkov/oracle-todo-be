package oracle.todo.model.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJSON {
    private Long id;
    private String name;
    private String username;
    private String token;
    private String password;
    private String mail;
}
