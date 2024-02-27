package denvot.homework.bookService.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "authors", schema = "books_service")
public class Author {
  @Id
  @NotNull
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "first_name", length = Integer.MAX_VALUE)
  private String firstName;

  @Column(name = "last_name", length = Integer.MAX_VALUE)
  private String lastName;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private List<Book> books;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void assignNewBook(Book book) {
    books.add(book);
  }
}