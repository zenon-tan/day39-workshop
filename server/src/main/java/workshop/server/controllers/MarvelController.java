package workshop.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import workshop.server.models.Comment;
import workshop.server.services.MarvelService;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class MarvelController {

    @Autowired
    MarvelService mSrc;

    @GetMapping(path = "/characters")
    public ResponseEntity<String> getCharacters(@RequestParam String search, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {

        String result = mSrc.getCharacters(search, limit, offset);

        return ResponseEntity.ok(result);

    }

    @GetMapping(path = "/character/{charId}")
    public ResponseEntity<String> getCharacterById(@PathVariable int charId) {
        String result = mSrc.getCharacter(charId);

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/comment/{charId}")
    public ResponseEntity<String> commentById(@PathVariable int charId, @RequestBody Comment comment) {

        mSrc.saveComment(comment);
        JsonObject response = Json.createObjectBuilder().add("status", "ok").build();
        return ResponseEntity.ok(response.toString());

    }
    
}
