# common-interfaces
aca van las interfaces compartidas por varios proyectos etc
```java
/* ejemplo */
        String fakeDbUrl = "http://localhost:3000/";
        String realDbUrl = "http://localhost:8080/cfx-rest-services/api/";

        PersonaService fakeDbPersonaService = getService(PersonaService.class, fakeDbUrl);
        PersonaService realDbPersonaService = getService(PersonaService.class, realDbUrl);

        SimpleDto personasDto = realDbPersonaService.getPersona("pedro");
        SimpleDto nuevaPersona = fakeDbPersonaService.createPersona(personasDto);

        return nuevaPersona;
```
