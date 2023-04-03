package workshop.server.repositories;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import workshop.server.models.Character;

@Repository
public class ChracterRepo {

    @Autowired
    @Qualifier("redis")
    RedisTemplate<String, String> redisTemplate;

    public void cacheCharacter(Character character) {
        String json = Character.toJsonRedis(character);
        redisTemplate.opsForValue().set(Integer.toString(character.getId()), json);
        redisTemplate.expire(Integer.toString(character.getId()), 3600, TimeUnit.SECONDS);
    }
    
    public Optional<String> getCached(int charId) {
        String result = redisTemplate.opsForValue().get(Integer.toString(charId));

        if(result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }
}


