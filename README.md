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

Como mantener sincronizados los fork

Agregar el repositorio original a la lista de remotos
- git remote add base-upstream https://github.com/enorrmann/common-interfaces.git

Luego verque se agrego
- git remote

Luego hacer un fetch desde ese repo
- git fetch base-upstream

Luego hacer un rebase (no se por que)
- git rebase base-upstream/master


