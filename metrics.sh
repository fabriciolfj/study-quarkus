kubectl create -f metrics/manifests-prometheus-grafana/setup
until kubectl get servicemonitors --all-namespaces ; do date; sleep 1;
echo ""; done
kubectl create -f metrics/manifests-prometheus-grafana/
kubectl apply -f metrics/servicemonitor.yml