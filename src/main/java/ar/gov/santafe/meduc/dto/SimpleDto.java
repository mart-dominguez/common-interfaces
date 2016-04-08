package ar.gov.santafe.meduc.dto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

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

    public <T> T as(Class<T> aClass) {
        T newInstance = null;
        try {
            newInstance = aClass.newInstance();
            Field[] fields = aClass.getDeclaredFields();
            for (Field aField : fields) {
                aField.setAccessible(true);
                aField.set(newInstance, get(aField.getName(), aField.getGenericType()));
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return newInstance;
    }

    public SimpleDto(Object any) {
        Class aClass = any.getClass();

        Field[] fields = aClass.getDeclaredFields();
        for (Field aField : fields) {
            try {
                aField.setAccessible(true);
                atributos.put(aField.getName(), aField.get(any));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(SimpleDto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private <T> T get(String key, Type type) {
        Object value = atributos.get(key);
        if (type.equals(String.class)) {
            return (T) value.toString();
        } else if (type.equals(Long.class)) {
            return (T) Long.valueOf(value.toString());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType actualType = (ParameterizedType) type;
            Type actualClass = actualType.getActualTypeArguments()[0];
            boolean isList = actualType.getRawType().equals(List.class);
            List oList = (List) value;

            if (oList == null || oList.isEmpty()) {
                return null;
            }
            List newList = new ArrayList();

            for (Object unObject : oList) {
                if (unObject instanceof SimpleDto) {
                    SimpleDto unSimpleDto = (SimpleDto) unObject;
                    newList.add(unSimpleDto.as((Class<T>) actualClass));
                } else {
                    newList.add(unObject);
                }
            }

            return (T) newList;
        } else {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            return null;
        }
    }

}
