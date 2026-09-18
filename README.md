# publication-service

Service de gestion des annonces immobilières de la plateforme **DREAMHOUSE237**, développé en **Spring Boot**.

## Rôle

Cœur métier de la plateforme :

- CRUD des biens immobiliers (`BienImmobilier`) : location, vente, statut de publication (`ACTIVE`, `REJETEE`, ...)
- Upload et gestion des médias associés aux annonces (Cloudinary)
- Réception des emails/régions utilisateurs (queue `user-email-queue`) pour les notifications
- Activation d'une annonce une fois les frais de publication payés (consumer `payment-status`, voir ci-dessous)

## Stack

- Java / Spring Boot / Spring Data JPA
- MySQL (AWS RDS, `sslMode=REQUIRED`)
- RabbitMQ (Spring AMQP)
- Port interne `8085`

## Flux asynchrone (RabbitMQ)

| Classe | Queue | Rôle |
|---|---|---|
| `UserEmailConsumer` | `user-email-queue` | Reçoit email + région depuis `user-service` |
| `PaymentStatusConsumer` | `payment-status` | Reçoit le statut d'un paiement de frais de publication depuis `payment-service` et active/rejette le bien correspondant |
| `PaymentProducer` | `payment-queue` | Déclenche une demande de paiement côté `payment-service` |

## Architecture & découverte de service

S'enregistre auprès de `registry-service` (Eureka) et récupère sa configuration depuis `config-service`. Exposé via `proxy-service` sous le préfixe `/PUBLICATION-SERVICE/`.

## Variables d'environnement clés

| Variable | Description |
|---|---|
| `PUBLICATION_DB_USER` / `PUBLICATION_DB_PASSWORD` | Identifiants MySQL |
| `RABBITMQ_USER` / `RABBITMQ_PASSWORD` | Identifiants RabbitMQ |
| `CLOUDINARY_CLOUD_NAME` / `CLOUDINARY_API_KEY` / `CLOUDINARY_API_SECRET` | Stockage des médias des annonces |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Envoi d'emails (SMTP Gmail) |

## Développement local

```bash
./mvnw spring-boot:run
```

## Déploiement

Via **Docker Swarm** (voir [`infrastructure`](https://github.com/DREAMHOUSE-237/infrastructure)). Un push sur `dev` déclenche le pipeline CI/CD complet jusqu'au redéploiement en production.
