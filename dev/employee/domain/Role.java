package employee.domain;

import java.util.List;

public class Role {
    private long id;
    private String name;
    private List<Permission> permissions; // maybe Set will be a better option..

    public Role(long id, String name, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
