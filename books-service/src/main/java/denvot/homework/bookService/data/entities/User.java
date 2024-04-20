package denvot.homework.bookService.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("nextval('users_id_seq'")
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @Column(name = "username", nullable = false, length = Integer.MAX_VALUE)
  private String username;

  @NotNull
  @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
  private String password;

  @NotNull
  @Column(name = "roles", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private Set<Role> roles;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
