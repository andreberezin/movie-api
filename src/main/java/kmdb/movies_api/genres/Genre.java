package kmdb.movies_api.genres;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class Genre {
   @Id
   @SequenceGenerator(
           name = "genre_sequence",
           sequenceName = "genre_sequence",
           allocationSize = 1
   )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genre_sequence"
    )

   private Long id;
   private String name;

   public Genre (String name) {
       this.name = name;
   }


/*    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }*/

}