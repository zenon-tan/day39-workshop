package workshop.server.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    private int id;
    private String name;
    private String imageUrl;
    private List<Comment> comments = new ArrayList<>();

    public static Character toCharacter(String json) {

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();
        JsonObject jj = j.getJsonObject("data").getJsonArray("results").get(0).asJsonObject();

        Character character = new Character();
        character.setId(jj.getInt("id"));
        character.setName(jj.getString("name"));

        JsonObject thumbnail = jj.getJsonObject("thumbnail");
        String path = thumbnail.getString("path");
        String ext = thumbnail.getString("extension");

        character.setImageUrl(path + "." + ext);

        return character;

    }

    public static Character toCharacterRedis(String json) {

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        Character character = new Character();
        character.setId(j.getInt("charId"));
        character.setName(j.getString("name"));
        character.setImageUrl(j.getString("imageUrl"));

        return character;

    }

    public static List<Character> toCharacters(String json) {

        List<Character> chars = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();
        JsonArray arr = j.getJsonObject("data").getJsonArray("results");

        arr.stream().forEach(
            v -> {
                JsonObject vv = v.asJsonObject();
                Character character = new Character();
                character.setId(vv.getInt("id"));
                character.setName(vv.getString("name"));
        
                JsonObject thumbnail = vv.getJsonObject("thumbnail");
                String path = thumbnail.getString("path");
                String ext = thumbnail.getString("extension");
        
                character.setImageUrl(path + "." + ext);
                System.out.println(character.toString());

                chars.add(character);

            }
            
        );

        return chars;

    }

    public static String toJson(Character character) {

        JsonObjectBuilder json = Json.createObjectBuilder()
        .add("charId", character.getId())
        .add("name", character.getName())
        .add("imageUrl", character.getImageUrl());

        JsonArrayBuilder arr = Json.createArrayBuilder();
        character.getComments().stream().forEach(
            v -> {
                JsonObjectBuilder j = Json.createObjectBuilder();
                j.add("commentId", v.getCommentId())
                .add("charId", v.getCharId())
                .add("comment", v.getComment())
                .add("timestamp", v.getTimestamp());

                arr.add(j);
            }
        );

        json.add("comments", arr);
        return json.build().toString();
    }

    public static String toJsonRedis(Character character) {

        JsonObjectBuilder json = Json.createObjectBuilder()
        .add("charId", character.getId())
        .add("name", character.getName())
        .add("imageUrl", character.getImageUrl());

        return json.build().toString();
    }

    public static String toJson(List<Character> chars) {

        JsonObjectBuilder json = Json.createObjectBuilder();
        JsonArrayBuilder arr = Json.createArrayBuilder();

        chars.stream().forEach(
            v -> {
                JsonObjectBuilder j = Json.createObjectBuilder()
                .add("charId", v.getId())
                .add("name", v.getName())
                .add("imageUrl", v.getImageUrl());

                arr.add(j);
            }
        );

        return json.add("Characters", arr).build().toString();
    }
    
}
