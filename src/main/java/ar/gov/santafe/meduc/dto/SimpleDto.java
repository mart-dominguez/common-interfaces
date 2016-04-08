package ar.gov.santafe.meduc.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName("")
@JsonSerialize(using = CustomSerializer.class)
@JsonDeserialize(using = CustomDeserializer.class)
public class SimpleDto {

    private Map<String, Object> atributos = new HashMap<String, Object>();

    public SimpleDto() {

    }

    public SimpleDto(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            this.atributos.put("arg" + i, args[i]);
        }
    }

    public SimpleDto(String[] names, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (names == null || names.length < i + 1) {
                this.atributos.put(i + "", args[i]);
            } else {
                this.atributos.put(names[i], args[i]);
            }
        }
    }

    public SimpleDto(Map<String, Object> args) {
        if (args.get("atributos") != null) {
            atributos = (Map) args.get("atributos");
        } else {
            atributos = args;
        }
    }

    public static List<SimpleDto> asGenericDtoList(String[] names, List<Object[]> objectArrayList) {
        List<SimpleDto> genericDtoList = new ArrayList<SimpleDto>();
        for (Object[] unObjectArray : objectArrayList) {
            genericDtoList.add(new SimpleDto(names, unObjectArray));
        }
        return genericDtoList;

    }

    public void add(String name, Object atributo) {
        this.atributos.put(name, atributo);
    }

    public Object get(String atributo) {
        return this.atributos.get(atributo);
    }

    public String getString(String atributo) {
        return this.atributos.get(atributo).toString();
    }

    public Map<String, Object> getAtributos() {
        return atributos;
    }

    public void setAtributos(HashMap<String, Object> atributos) {
        this.atributos = atributos;
    }

    public List<SimpleDto> getList(String key) {
        List list = (List) atributos.get(key);
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return list;
        }
        if (list.get(0) instanceof Map) {
            return getGenericDtoFromMapList(list);
        } else if (list.get(0) instanceof SimpleDto) {
            return list;
        } else {
            return null;
        }

    }

    private List<SimpleDto> getGenericDtoFromMapList(List<Map> list) {
        List<SimpleDto> genericDtoList = new ArrayList<SimpleDto>();
        for (Map aMap : list) {
            genericDtoList.add(new SimpleDto(aMap));
        }
        return genericDtoList;
    }

}
