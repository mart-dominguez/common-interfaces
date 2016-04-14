package ar.gov.santafe.meduc.dto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName("")
@JsonSerialize(using = CustomSerializer.class)
@JsonDeserialize(using = CustomDeserializer.class)
public class SimpleDto {

    private Map<String, Object> atributos = new HashMap<String, Object>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");

    public SimpleDto() {

    }

    public SimpleDto(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            this.atributos.put("arg" + i, args[i]);
        }
    }

    public SimpleDto(Map<String, Object> args) {
        if (args.get("atributos") != null) {
            atributos = (Map) args.get("atributos");
        } else {
            atributos = args;
        }
    }

    public SimpleDto add(String name, Object atributo) {
        if (atributo instanceof Date) {
            add(name, (Date) atributo);
        } else {
            this.atributos.put(name, atributo);
        }
        return this;
    }

    public Object get(String atributo) {
        return this.atributos.get(atributo);
    }

    public String getString(String atributo) {
        Object attr = this.atributos.get(atributo);
        return attr == null ? null : attr.toString();
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
                add(aField.getName(), aField.get(any));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(SimpleDto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private <T> T get(String key, Type type) {
        Object value = atributos.get(key);
        if (value == null) {
            return null;
        }
        if (type.equals(String.class)) {
            return (T) value.toString();
        } else if (type.equals(Long.class)) {
            return (T) Long.valueOf(value.toString());
        } else if (type.equals(Date.class)) {
            return (T) getDate(key);
        } else if (type.equals(SimpleDto.class)) {
            return ((SimpleDto) value).as((Class<T>) type);
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
        } else if (value instanceof SimpleDto) {
            return ((SimpleDto) value).as((Class<T>) type);
        } else {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            return null;
        }
    }

    public boolean isEmpty() {
        return atributos == null || atributos.isEmpty();
    }

    public Date getDate(String key) {
        Object val = atributos.get(key);
        if (val == null) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(val.toString());
        } catch (ParseException ex) {
            Logger.getLogger(SimpleDto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public SimpleDto add(String key, Date date) {
        this.atributos.put(key, sdf.format(date));
        return this;
    }

    @JsonIgnore
    private String getEqualsAttribute() {
        String equalsAttribute = getString("equalsAttribute");
        if (equalsAttribute == null) {
            equalsAttribute = "id";
        }
        return getString(equalsAttribute);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.getEqualsAttribute());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleDto other = (SimpleDto) obj;
        if (!Objects.equals(this.getEqualsAttribute(), other.getEqualsAttribute())) {
            return false;
        }
        return true;
    }

}
