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
- Listando os serviços expostos no minikube
```
 minikube service list
```
- Build aplicação em bytecode native
```
mvn clean install -Pnative
```
- Criando um configmap de exemplo
```
kubectl create configmap banking \
        --from-file=application.yaml
```
- Criando um secret de exemplo
```
kubectl create secret generic db-credentials \
        --from-literal=username=admin \
        --from-literal=password=secret \
        --from-literal=db.username=quarkus_banking \
        --from-literal=db.password=quarkus_banking
```
