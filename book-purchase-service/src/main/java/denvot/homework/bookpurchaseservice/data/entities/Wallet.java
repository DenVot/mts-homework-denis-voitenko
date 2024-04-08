package denvot.homework.bookpurchaseservice.data.entities;

import jakarta.persistence.*;

@Table(name = "wallet")
@Entity
public class Wallet {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "balance")
  private Integer balance;

  public Wallet(Integer balance) {
    this.balance = balance;
  }

  public Wallet() {

  }

  public Integer getId() {
    return id;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }
}
