kubectl port-forward \
    -n monitoring \
    service/grafana 3000:3000