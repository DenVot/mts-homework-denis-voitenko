package denvot.homework.bookService.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @ManyToMany(fetch = LAZY, cascade = PERSIST)
  @JoinTable(
      name = "books_tags",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  @NotNull
  @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
  private String title;

  protected Book() {}

  public Book(String title, Author author) {
    this.title = title;
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void assignTag(Tag tag) {
    tags.add(tag);
  }

  public void deassignTag(Tag tag) {
    tags.removeIf(pTag -> Objects.equals(pTag.getId(), tag.getId()));
  }
}