# Study Quarkus
- Apontar para o daemon do minikube
```
eval $(minikube -p minikube docker-env)
```
- Build imagem docker:
```
mvn clean package -Dquarkus.container-image.build=true
```
- Deploy em um ambiente kubernetes:
```
mvn clean package -Dquarkus.kubernetes.deploy=true
```
