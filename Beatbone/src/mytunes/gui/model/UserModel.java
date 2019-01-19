package mytunes.gui.model;

import mytunes.be.User;
import mytunes.bll.BllManager;
import mytunes.bll.IBllFacade;

/**
 * The {@code UserModel} class is responsible for 
 * getting and passing users informations to 
 * Business Logic Layer. It is using singleton 
 * design pattern.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class UserModel {
    
    private static UserModel instance;
    private IBllFacade bllManager;
    
    /**
     * Creates connection with BLL.
     */
    private UserModel()
    {
        bllManager = new BllManager();
    }
    
    /**
     * Returns single instance of UserModel class.
     * 
     * @return The instance of UserModel class.
     */
    public static UserModel createInstance()
    {
        if(instance == null)
        {
            instance = new UserModel();
        }
        return instance;
    }
    
    /**
     * Creates the user with given e-mail address and password.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return Created user.
     */
    public User createUser(String email, String password)
    {
        return bllManager.createUser(email, password);
    }
    
    /**
     * Returns the user with given e-mail address and password.
     * If there is no user with given e-mail and password returns null.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return The user with given e-mail and password.
     */
    public User getUser(String email, String password)
    {
        return bllManager.getUser(email, password);
    }
}
