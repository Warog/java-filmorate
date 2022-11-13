package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Genre implements Comparable <Genre>{
    private Integer id;
    private String name;

    @Override
    public int compareTo(Genre o) {

        int result = this.id.compareTo(o.id);

        if (result == 0) {
            result = this.name.compareTo(o.name);
        }
        return result;
    }
}
