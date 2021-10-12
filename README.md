# Study Quarkus
- Apontar para o daemon do minikube
```
eval $(minikube -p minikube docker-env)
```
- Build imagem docker:
```
mvn clean package -Dquarkus.container-image.build=true
```
