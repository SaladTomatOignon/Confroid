package fr.uge.confroid.web;

class UserInfos {
    private final String username;
    private final String password;

    public UserInfos(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
