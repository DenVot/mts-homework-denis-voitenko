package denvot.homework.bookService.data.entities;

import java.util.Set;

public class Book {
  private final long id;
  private String author;
  private String title;
  private Set<String> tags;

  public Book(long id, String author, String title, Set<String> tags) {
    this.id = id;
    this.author = author;
    this.title = title;
    this.tags = tags;
  }

  public long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}
