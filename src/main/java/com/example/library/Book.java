package com.example.library;

import jakarta.persistence.*;
// DB for Book
@Entity
@Table(name = "books")
public class Book{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "available")
    private Boolean available;
    @Column(name = "price")
    private double price;
    @Column(name = "content")
    private String content;
    @Column(name = "bookURL")
    private String fullBookUrl;


    //Constructor
    public Book(){

    }
    public Book(String title,String author,Boolean available,double price,String content,String fullBookUrl){

        this.title = title;
        this.author = author;
        this.available = available;
        this.price = price;
        this.content = content;
        this.fullBookUrl = fullBookUrl;
    }
    public Long getId() {
        return id;
    }
    //GETTERS
    public String getTitle() {
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public Boolean getAvailable() {
        return available;
    }
    public double getPrice(){ return price;}
    public String getContent(){ return content;}

    //Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author){ this.author = author; }
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    public void setPrice(double price ){this.price = price;}

    public void setFullBookUrl(String fullBookUrl) {
        this.fullBookUrl = fullBookUrl;
    }

    public String getFullBookUrl() {
        return fullBookUrl;
    }
}