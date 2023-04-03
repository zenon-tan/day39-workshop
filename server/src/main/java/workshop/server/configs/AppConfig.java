package workshop.server.configs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {

    public static final String DB_MARVEL = "marvel";

    @Value("${mongo.url}")
    private String mongoUrl;

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;
    @Value("${spring.redis.username}")
    private String redisUsername;
    @Value("${spring.redis.password}")
    private String redisPassword;

    // @Value("${do.storage.access.key}")
    // private String accessKey;

    // @Value("${do.storage.secret.key}")
    // private String secretKey;

    // @Value("${do.storage.endpoint}")
    // private String endpoint;

    // @Value("${do.storage.endpoint.region}")
    // private String endpointRegion;

    @Bean
    public MongoTemplate createMongoTemplate() {
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, DB_MARVEL);
    }

    // @Bean
    // @Qualifier("amazon")
    // public AmazonS3 createS3Client() {
    //     BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
    //     EndpointConfiguration ep = new EndpointConfiguration(endpoint, endpointRegion);

    //     return AmazonS3ClientBuilder
    //     .standard().withEndpointConfiguration(ep)
    //     .withCredentials(new AWSStaticCredentialsProvider(cred)).build();
        
    // }

    @Bean
    @Qualifier("redis")
    @Scope("singleton")
    public RedisTemplate<String, String> redisTemplate() {

        //3 items needed to set up the redis template(RedisStandaloneConfiguration, JedisConnectionFactory, redisTemplate)
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort.get());
        config.setUsername(redisUsername);
        config.setPassword(redisPassword);
        config.setDatabase(0);

        // Setting up the Jedis Connection Factory
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);

        // Save the properties
        jedisFac.afterPropertiesSet();

        // Create the redis template
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(jedisFac);

        // Keys (Type String) will be serialised
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());

        Jackson2JsonRedisSerializer jjsons = new Jackson2JsonRedisSerializer(Character.class);

        redisTemplate.setValueSerializer(new JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new JsonRedisSerializer());

        return redisTemplate;
        
    }

    static class JsonRedisSerializer implements RedisSerializer<Object> {

        private final ObjectMapper om;

        public JsonRedisSerializer() {
            this.om = new ObjectMapper().activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        }

        @Override
        public byte[] serialize(Object t) throws SerializationException {
            try {
                return om.writeValueAsBytes(t);
            } catch (JsonProcessingException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {

            if (bytes == null) {
                return null;
            }

            try {
                return om.readValue(bytes, Object.class);
            } catch (Exception e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }
    }


    
}
