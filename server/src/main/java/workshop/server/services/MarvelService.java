package workshop.server.services;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import workshop.server.models.Character;
import workshop.server.models.Comment;
import workshop.server.repositories.ChracterRepo;
import workshop.server.repositories.CommentRepo;


@Service
public class MarvelService {

    @Autowired
    CommentRepo cRepo;

    @Autowired
    ChracterRepo charRepo;

    private static final String MARVEL_API_GET_CHARACTERS = """
        https://gateway.marvel.com:443/v1/public/characters""";

    @Value("${marvel.public.key}")
    private String publicKey;

    @Value("${marvel.private.key}")
    private String privateKey;

    final Long ts = new Date().getTime();

    private String generateMD5String() {

        String toHash = ts + privateKey + publicKey;
        // System.out.println(toHash);
        String hash = DigestUtils.md5DigestAsHex(toHash.getBytes());

        return hash;

    }

    public String getCharacters(String search, int limit, int offset) {
        String url = UriComponentsBuilder.fromUriString(MARVEL_API_GET_CHARACTERS)
        .queryParam("nameStartsWith", search)
        .queryParam("ts", ts)
        .queryParam("apikey", publicKey)
        .queryParam("hash", generateMD5String())
        .queryParam("limit", limit)
        .queryParam("offset", offset)
        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
        .accept(MediaType.APPLICATION_JSON).build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);
            List<Character> characters = Character.toCharacters(resp.getBody());

            return Character.toJson(characters);

        } catch (Exception e) {

            e.printStackTrace();
            JsonObject status = Json.createObjectBuilder().add("Status", "not found").build();
            return status.toString();

        }

    }

    public String getCharacter(int charId) {

        Optional<String> result = charRepo.getCached(charId);
        if(result.isEmpty()) {

            String url = UriComponentsBuilder.fromUriString(MARVEL_API_GET_CHARACTERS + "/" + charId)
            .queryParam("ts", ts)
            .queryParam("apikey", publicKey)
            .queryParam("hash", generateMD5String())
            .toUriString();
    
            RequestEntity<Void> req = RequestEntity.get(url)
            .accept(MediaType.APPLICATION_JSON).build();
    
            RestTemplate template = new RestTemplate();
    
            ResponseEntity<String> resp = null;
    
            try {
                resp = template.exchange(req, String.class);
    
                Character character = Character.toCharacter(resp.getBody());
    
                List<Document> docs = cRepo.getComments(charId);
                List<Comment> comments = Comment.toComments(docs);
    
                character.setComments(comments);

                charRepo.cacheCharacter(character);
    
                return Character.toJson(character);
    
            } catch (Exception e) {
    
                e.printStackTrace();
                JsonObject status = Json.createObjectBuilder().add("Status", "not found").build();
                return status.toString();
    
            }

        } else {

            String json = result.get();
            Character character = Character.toCharacterRedis(json);

            List<Document> docs = cRepo.getComments(charId);
            List<Comment> comments = Comment.toComments(docs);
    
            character.setComments(comments);

            return Character.toJson(character);

        }

    }

    public void saveComment(Comment comment) {
        cRepo.addComment(comment);
    }


    
}
