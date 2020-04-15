package com.example.jpa.ManyToMany;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String name;
    private String address;
    private OffsetTime openingTime;
    private OffsetTime closingTime;

    @ManyToMany( cascade = CascadeType.ALL )
    @JoinTable(name = "stores_books", joinColumns = { @JoinColumn(name = "store_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
    private List<Book> books = new ArrayList<>();

    @Version
    private long version;

    protected Store() {}

    public Store(String name, String address, OffsetTime openingTime, OffsetTime closingTime) {
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", version=" + version +
                '}';
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        this.books.add( book );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OffsetTime getOpeningTime(ZoneOffset offset) {
        return openingTime.withOffsetSameInstant( offset );
    }

    public void setOpeningTime(OffsetTime openingTime) {
        this.openingTime = openingTime;
    }

    public OffsetTime getClosingTime(ZoneOffset offset) {
        return closingTime.withOffsetSameInstant( offset );
    }

    public void setClosingTime(OffsetTime closingTime) {
        this.closingTime = closingTime;
    }
}
