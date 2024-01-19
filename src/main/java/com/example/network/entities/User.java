package com.example.network.entities;

import com.example.network.entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "likedPosts")
@ToString(exclude = "likedPosts")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull(message = "Name must be included.")
    @Size(min=2,max = 30, message= "Name must be beetwen {min} and {max} characters long.")
    private String name;

    @Column(nullable=false, unique = true)
    @NotNull(message="username must be provided")
    @Size(min=5,max=20, message= "username must be beetwen {min} and {max} characters long.")
    private String username;

    @Column(nullable=false,name="password")
    @NotNull(message="Password must be provided")
    private String password;

    @Column(nullable=false,name = "email", unique = true)
    private String email;

    @Column(nullable=false,name = "role")
    private Role role;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_liked_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> likedPosts;

}
