package denvot.homework.bookpurchaseservice.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Table(name = "outbox")
@Entity
public class OutboxRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "data")
  private String data;

  public OutboxRecord(String data) {
    this.data = data;
  }

  public OutboxRecord() {

  }

  public Long getId() {
    return id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
