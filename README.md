# Study Quarkus
- Apontar para o daemon do minikube
```
eval $(minikube -p minikube docker-env)
```
- Build imagem docker:
```
mvn clean package -Dquarkus.container-image.build=true
mvn clean package -Dquarkus.native.container-image.build=true
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
        
kubectl get secret db-credentials -o jsonpath='{.data}'
        
```
- Deletando o deployment (caso tente repetir um deploy novamente pelo quarkus)
```
kubectl delete -f /target/kubernetes/minikube.yaml.
```

- No quarkus possui 3 profiles já existentes:
  - %prod: utilizado para deploy da imagem ou em kubernetes
  - %test: utilizado para execucao dos testes
  - %dev: para desenvolvimento
  - caso queira utilizar outro profile, pode-se utilizar %profile e mvn clean compile -Dquarkus.profile=profile  

- Estatégias de fall tolerance
  - @asynchronous: executa o método em outra thread
  - @bulkhead: limita o número de requisições concorrentes
  - @circuitbreaker: evitar repetição de falhas
  - @fallback: lógica alternativa diante a alguma exceção
  - @retry: retentativas de execução, diante a alguma exceção
  - @timeout: tempo máximo para execução de alguma chamada externa
  - dependência:
```
mvn quarkus:add-extension -Dextensions="quarkus-smallrye-fault-tolerance"
```
