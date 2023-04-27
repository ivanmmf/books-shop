package ru.maxima.booksshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UpdateUserBook {
    private String login;
    private int book_id;

    public String getLogin() {
        return login;
    }

    public int getBook_id() {
        return book_id;
    }
}
