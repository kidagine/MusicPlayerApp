/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import mytunes.bll.util.TimeConverter;

/**
 * The {@code Song} class is representing 
 * playlist in our application. It consist of basic
 * informations about song.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class Song {
    
    private int id;
    private String title;
    private String artist;
    private String genre;
    private String path;
    private int time;
    
    /**
     * Constructs a new song.
     * 
     * @param id The ID of song.
     * @param title The title of song.
     * @param artist The artist of song.
     * @param genre The genre of song.
     * @param path The path of song.
     * @param time The time of song.
     */
    public Song(int id, String title, String artist, String genre, String path, int time) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.path = path;
        this.time = time;
    }
    
    /**
     * Returns ID of the song.
     * 
     * @return The id of song.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Returns title of the song.
     * 
     * @return The title of song.
     */
    public String getTitle() 
    {
        return title;
    }
    
    /**
     * Returns artist of the song.
     * 
     * @return The artist of song.
     */
    public String getArtist() 
    {
        return artist;
    }
    
    /**
     * Returns genre of the song.
     * 
     * @return The genre of song.
     */
    public String getGenre() 
    {
        return genre;
    }
    
    /**
     * Returns time of the song.
     * 
     * @return The time of song.
     */
    public int getTime() 
    {
        return time;
    }
    
    /**
     * Sets title of the song.
     * 
     * @param title The title to set.
     */
    public void setTitle(String title) 
    {
        this.title = title;
    }
    
    /**
     * Sets artist of the song.
     * 
     * @param artist The artist to set.
     */
    public void setArtist(String artist) 
    {
        this.artist = artist;
    }
    
    /**
     * Sets genre of the song.
     * 
     * @param genre The genre to set.
     */
    public void setGenre(String genre) 
    {
        this.genre = genre;
    }
    
    /**
     * Returns path of the song.
     * 
     * @return The path of song.
     */
    public String getPath() 
    {
        return path;
    }
    
    /**
     * Sets path of the song.
     * 
     * @param path The path to set.
     */
    public void setPath(String path) 
    {
        this.path = path;
    }
    
    /**
     * Returns in format specified by {@code TimeConverter} class.
     * 
     * @return The formatted time.
     */
    public String getTimeInString()
    {
        return TimeConverter.convertToString(time);
    }
    
    @Override
    public String toString()
    {
        return title;
    }
    
}
