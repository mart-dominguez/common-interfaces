# common-interfaces
aca van las interfaces compartidas por varios proyectos etc

Como mantener sincronizados los fork

agregar el repositorio original a la lista de remotos
- git remote add base-upstream https://github.com/enorrmann/common-interfaces.git
Luego verque se agrego
- git remote
Luego hacer un fetch desde ese repo
- git fetch base-upstream
Luego hacer un rebase (no se por que)
- git rebase base-upstream/master

