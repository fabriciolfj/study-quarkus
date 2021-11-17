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

###### Circuit breaker
- requestVolumeThreshold: número de solicitações, utilizado para calcular a abertura de um circuito
- failureRatio: percentual com base na propriedade acima, para abertura do circuito
- delay: tempo que o circuito permanece aberto, antes de aceitar uma solicitação
- delayUnit: unidade de tempo
- successThreshold: número de solicitações com sucesso, para fechar o circuito
- failOn: lista de exceções que participaram do circuito
- skipOn: exeções ignoradas

###### openapi
- antigamente conhecido como swagger, para implementarmos em produção, devemos:
  - pegar o yaml por: localhost:8080/q/openapi
  - colocar no diretório: resources/meta-inf
  - a opção deve ficar false: quarkus.swagger-ui.always-include=true
  - obs: para desenv funciona sem tal necessidade, apenas para pro (%prod)
