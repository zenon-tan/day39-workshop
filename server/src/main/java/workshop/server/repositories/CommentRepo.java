package workshop.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import workshop.server.models.Comment;

@Repository
public class CommentRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    private static final String COLLECTIONS_COMMENTS = "comments";

    public List<Document> getComments(int charId) {

        Query query = Query.query(Criteria.where("charId").is(charId)).with(Sort.by(Direction.DESC, "timestamp")).limit(10);
        List<Document> docs = mongoTemplate.find(query, Document.class, COLLECTIONS_COMMENTS);

        return docs;
        
    }

    public void addComment(Comment comment) {

        Document doc = Comment.toDocument(comment);

        mongoTemplate.insert(doc, COLLECTIONS_COMMENTS);

    }
    
}
