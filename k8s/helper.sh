#!/bin/bash

# =========================
# Microservices Dev & Deploy Cheat Sheet
# =========================

# ----------
# 1️⃣ IDV Microservice Build & Deploy
# ----------

cd ~/IdeaRepo/DemoPatrianna/KuberSpetz/idv-microservice
mvn clean package

docker build -t cyberspetzdocker/idv-microservice:latest .
docker push cyberspetzdocker/idv-microservice:latest

kubectl apply -f ../k8s/idv-deployment.yaml
kubectl apply -f ../k8s/idv-service.yaml
kubectl apply -f ../k8s/idv-hpa.yaml

kubectl rollout restart deployment idv-microservice
kubectl scale deployment idv-microservice --replicas=2

# ----------
# 2️⃣ Account Microservice Build & Deploy
# ----------

cd ~/IdeaRepo/DemoPatrianna/KuberSpetz/account-microservice
mvn clean package

docker build -t cyberspetzdocker/account-microservice:latest .
docker push cyberspetzdocker/account-microservice:latest

kubectl apply -f ../k8s/account-deployment.yaml
kubectl apply -f ../k8s/account-service.yaml
kubectl apply -f ../k8s/account-hpa.yaml

kubectl rollout restart deployment account-microservice

# ----------
# 3️⃣ Port-Forwarding
# ----------

# IDV Service
kubectl port-forward svc/idv-service 8082:80 &

# Account Service
kubectl port-forward svc/account-service 8083:80 &

# port forwarding
kubectl port-forward svc/zipkin 9411:9411 &
kubectl port-forward service/postgres 5432:5432

# ----------
# 4️⃣ Kafka Topic Operations
# ----------

# Create Kafka Topic
kubectl exec -it <kafka-pod-name> -- /bin/bash -c "kafka-topics.sh --create --topic account-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1"

# Consume Messages Listener
kubectl exec -it kafka-54f65795fc-gmc7b -- /bin/bash
#inside
kubectl exec -it <kafka-pod-name> -- /bin/bash -c "kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic account-topic --from-beginning"

# ----------
# 5️⃣ Test Microservices
# ----------

# Account Withdraw API
curl -X POST -d "accountNumber=2&amount=100" http://localhost:8082/account/withdraw
# Idv API
curl -X POST -d "accountNumber=123" http://localhost:8083/verify\?id\=123

# ----------
# 6️⃣ Clean Apply All K8s Manifests
# ----------

kubectl apply -f k8s/

# ----------
# 7️⃣ Monitoring & Verification
# ----------

kubectl get pods
kubectl get svc
kubectl get deployment
kubectl get hpa

# ----------
# 8️⃣ Cleanup Deployments
# ----------

# IDV Microservice Cleanup
kubectl delete deployment idv-microservice
kubectl delete service idv-service
kubectl delete hpa idv-hpa

# Account Microservice Cleanup
kubectl delete deployment account-microservice
kubectl delete service account-service
kubectl delete hpa account-hpa

# ----------
# 8️⃣ Resources check
# ----------

#Quick view of CPU & memory usage by pod.
kubectl top pods

Check resource requests/limits.


#Quick view of CPU & memory usage by pod.
kubectl top pods	

#Check resource requests/limits.
kubectl describe pod <name>	

#Check autoscaling CPU metrics per replica.
kubectl get hpa

# =========================
# End of Cheat Sheet
# =========================
