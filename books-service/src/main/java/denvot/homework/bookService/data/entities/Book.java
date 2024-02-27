package denvot.homework.bookService.data.entities;

import jakarta.persistence.*;

import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "books", schema = "books_service")
public class Book {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "author_id")
  private Long authorId;

  @Column(name = "title", length = Integer.MAX_VALUE)
  private String title;

  @ManyToOne(fetch = LAZY)
  private Author author;

  @ManyToMany(fetch = LAZY, cascade = PERSIST)
  @JoinTable(
          name = "books_tags",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
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
}