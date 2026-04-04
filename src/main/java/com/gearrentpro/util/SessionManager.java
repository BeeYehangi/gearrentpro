package com.gearrentpro.util;

import com.gearrentpro.entity.SystemUser;

public class SessionManager {

    private static SessionManager instance;
    private SystemUser loggedInUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInUser(SystemUser user) {
        this.loggedInUser = user;
    }

    public SystemUser getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        this.loggedInUser = null;
    }

    public boolean isAdmin() {
        return loggedInUser != null && 
               loggedInUser.getRole().equals("ADMIN");
    }

    public boolean isBranchManager() {
        return loggedInUser != null && 
               loggedInUser.getRole().equals("BRANCH_MANAGER");
    }

    public boolean isStaff() {
        return loggedInUser != null && 
               loggedInUser.getRole().equals("STAFF");
    }
}