/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modal;

import java.io.Serializable;

/**
 *
 * @author lannguyen
 */
public class Song implements Serializable {
    
    private String id;
    private String name;
    private String nameFile;

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Song(String _id, String _name, String _nameFile) {
        this.id = _id;
        this.name = _name;
        this.nameFile = _nameFile;
    }
    
    public boolean equalsSong(String idSong) {
        return this.id.equals(idSong);
    }
}
