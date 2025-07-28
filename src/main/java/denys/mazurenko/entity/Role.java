package denys.mazurenko.entity;

public class Role {
    private Long id;
    private String name;

    public enum Roles {
        PUBLISHER,
        ADMIN
    }
}