package kmdb.movies_api.genres;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

   @NotEmpty(message = "Name cannot be empty")
   private String name;

   public Genre (String name) {
       this.name = name;
   }
}