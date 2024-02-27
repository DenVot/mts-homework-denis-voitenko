package denvot.homework.bookService.data.entities;

import jakarta.persistence.*;

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

  @ManyToOne(fetch = FetchType.LAZY)
  private Author author;

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

}