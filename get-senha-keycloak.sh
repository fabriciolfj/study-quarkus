kubectl get secret credential-bank-keycloak \
        -n quarkus \
        -o go-template='{{range $k,$v := .data}}{{printf "%s: " $k}}
          {{if not $v}}{{$v}}{{else}}{{$v | base64decode}}{{end}}
            {{"\n"}}{{end}}'