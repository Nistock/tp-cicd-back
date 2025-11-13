package fr.iut.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String title;
    
    @Column(name = "publish_year")
    private Integer publishYear;
    
    private String isbn;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}