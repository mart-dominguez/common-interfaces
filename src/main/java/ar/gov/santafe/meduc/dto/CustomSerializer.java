package ar.gov.santafe.meduc.dto;

import java.io.IOException;
import java.util.Set;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author enorrmann
 */
public class CustomSerializer extends JsonSerializer<SimpleDto> {

    @Override
    public void serialize(SimpleDto genericDto, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartObject();
        Set<String> keys = genericDto.getAtributos().keySet();
        for (String aKey : keys) {
            jg.writeObjectField(aKey, genericDto.get(aKey));
        }
        jg.writeEndObject();
    }

}
