package workshop.server.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {

    private String commentId;
    private int charId;
    private String comment;
    private Long timestamp;

    public Comment() {
        this.commentId = UUID.randomUUID().toString().substring(0, 8);
        this.timestamp = new Date().getTime();
    }

    public static List<Comment> toComments(List<Document> docs) {

        List<Comment> comments = new ArrayList<>();

        docs.stream().forEach(
            v -> {
                Comment comment = new Comment();
                comment.setCommentId(v.getString("commentId"));
                comment.setCharId(v.getInteger("charId"));
                comment.setComment(v.getString("comment"));
                comment.setTimestamp(v.getLong("timestamp"));

                comments.add(comment);
            }
        );

        return comments;
        
    }

    public static Document toDocument(Comment comment) {

        Comment cc = new Comment();
        cc.setCharId(comment.getCharId());
        cc.setComment(comment.getComment());

        Document doc = new Document();
        doc.append("commentId", cc.getCommentId())
        .append("charId", cc.getCharId())
        .append("comment", cc.getComment())
        .append("timestamp", cc.getTimestamp());

        return doc;


    }
    
}
