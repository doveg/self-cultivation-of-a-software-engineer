@Data
@NoArgsConstructor
public class Menu implements Serializable {

    private String id;
    private String name;
    private String pid;
    private List<Menu> menuChildren;

}
