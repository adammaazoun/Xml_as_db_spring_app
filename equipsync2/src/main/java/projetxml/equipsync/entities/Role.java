package projetxml.equipsync.entities;

public class Role {
    private int roleId;
    private int userId;
    private int[] permissionIds;

    public int[] getPermissionIds() {
        return permissionIds;
    }

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setPermissionIds(int[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
