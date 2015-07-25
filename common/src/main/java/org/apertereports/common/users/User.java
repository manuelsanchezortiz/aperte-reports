package org.apertereports.common.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class represents user
 *
 * @author Tomasz Serafin, BlueSoft sp. z o. o.
 */
public class User {

	private long userId;
    private final String login;
    private Set<UserRole> roles = new HashSet<UserRole>();
    private final boolean administrator;
    private final String email;

    public User(long userId, String login, Set<UserRole> roles, boolean administrator, String email) {
    	this.userId= userId;
        this.login = login;
        this.roles = roles;
        this.administrator = administrator;
        this.email = email;
    }
    
    /**
     * 
     * @return the userId
     */
    public long getUserId(){
    	return this.userId;
    }

    /**
     * Return login of the user
     *
     * @return Login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns collection of roles assigned to the user
     *
     * @return Collection of roles
     */
    public Collection<UserRole> getRoles() {
        return roles;
    }

    /**
     * Determines if user is administrator
     *
     * @return true if administrator, false otherwise
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Returns email address
     * @return Email address
     */
    public String getEmail() {
        return email;
    }
}