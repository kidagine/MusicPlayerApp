/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

/**
 *
 * @author Acer
 */
public class User {
    
    private int id;
    private String email;
    private String password;
    
    /**
     * Constructs a new user.
     * 
     * @param id The id of user.
     * @param email The users e-mail address.
     * @param password The users password.
     */
    public User(int id, String email, String password)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    
    /**
     * Returns id of the user.
     * 
     * @return The users ID.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Returns users e-mail address.
     * 
     * @return The users e-mail.
     */
    public String getEmail()
    {
        return email;
    }
    
    /**
     * Returns users password.
     * @return The password of user
     */
    public String getPassword()
    {
        return password;
    }
    
}
