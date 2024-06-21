package com.example.charter.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name = "yachts")
@Table(name = "yachts")
public class Yacht {
    @Id
    @Column(name = "id")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Yacht's name must not be empty")
    private String name;
    @Column(name = "type")
    @NotBlank(message = "Yacht's type must not be empty")
    private String type;
    @Column(name = "length")

    @NotNull(message = "Yacht's length must not be empty")
    private double length;

    public Yacht(){

    }

    public int getId() {
        return id;
    }

    public void setId(int yachtId) {
        this.id = yachtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
