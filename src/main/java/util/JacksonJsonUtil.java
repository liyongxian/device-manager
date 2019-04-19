package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @Author: renpeng
 * @email: renp90@qq.com
 * @CreateTime: 2017/12/21 17:44
 * @Description:
 */
public class JacksonJsonUtil {
    static private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <T> String toJson(T object) {

        try {
            /*if (object instanceof List) {
                String json = "[";
                for (Object item : (List<?>) object) {
                    json += mapper.writeValueAsString(item) + ",";
                }
                if (json.endsWith(",")) json = json.substring(0, json.length() - 1);
                json += "]";
                return json;
            }*/
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getObjectFromJson(String json, Class<T> clazz) throws  IOException
    {

        return mapper.readValue(json, clazz);
    }
}
