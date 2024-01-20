package com.example.network.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"likedByUsers", "comments", "ratings"})
@ToString(exclude = {"likedByUsers", "comments", "ratings"})
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES user ON DELETE CASCADE ON UPDATE CASCADE"))
    private User user;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> likedByUsers;


}
