package cn.onlov.evaluate.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by yangqj on 2017/4/30.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Value("${spring.redis.host}")  
    private String host;  
    @Value("${spring.redis.port}")  
    private int port;  
    @Value("${spring.redis.password}")  
    private String passWord;  
    @Value("${spring.redis.pool.max-idle}")  
    private int maxIdl;  
    @Value("${spring.redis.pool.min-idle}")  
    private int minIdl;  
//    @Value("${spring.redis.database}")  
//    private int database;  
    
    @Bean  
    public RedisConnectionFactory jedisConnectionFactory(){  
        JedisPoolConfig poolConfig=new JedisPoolConfig();  
        poolConfig.setMaxIdle(maxIdl);  
        poolConfig.setMinIdle(minIdl);  
        poolConfig.setTestOnBorrow(true);  
        poolConfig.setTestOnReturn(true);  
        poolConfig.setTestWhileIdle(true);  
        poolConfig.setNumTestsPerEvictionRun(10);  
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);  
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);  
        jedisConnectionFactory.setHostName(host);   
        if(!passWord.isEmpty()){   
            jedisConnectionFactory.setPassword(passWord);   
        }   
        jedisConnectionFactory.setPort(port);   
//        jedisConnectionFactory.setDatabase(database);  
        return jedisConnectionFactory;  
    }  
    
	/**
     * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
     * @param
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
}
