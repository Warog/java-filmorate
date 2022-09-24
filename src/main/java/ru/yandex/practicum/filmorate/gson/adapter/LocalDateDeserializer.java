package ru.yandex.practicum.filmorate.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json.getAsString().equals("null"))
            return null;
        else {
            return LocalDate.parse(json.getAsString(),
                    DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }
}