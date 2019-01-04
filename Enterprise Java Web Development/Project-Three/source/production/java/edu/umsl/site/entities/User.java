package edu.umsl.site.entities;

import edu.umsl.site.validation.NotBlank;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;

@Entity
@Table(name = "USERS")
@XmlRootElement
public class User {
    private long id;
    private String lastUpdated;

    @NotBlank(message = "{validate.user.username}")
    private String username;

    @NotBlank(message = "{validate.user.password}")
    private String password;

    @NotBlank(message = "{validate.user.firstname}")
    private String firstname;

    @NotBlank(message = "{validate.user.lastname}")
    private String lastname;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LASTUPDATED")
    public String getlastUpdated() {
        return lastUpdated;
    }

    public void setlastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Basic
    @Column(name = "USERNAME", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "FIRSTNAME", nullable = false)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "LASTNAME", nullable = false)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
