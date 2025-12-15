SignUpLogInJWT
Descrizione

Questo progetto è un caso di studio sviluppato per approfondire l'uso di Spring Security, JWT (JSON Web Tokens) e i concetti di autenticazione e autorizzazione in applicazioni web basate su Spring Boot.

L'obiettivo principale è stato implementare un sistema di registrazione, login e gestione utenti con sicurezza basata su token, esplorando le best practice di sicurezza moderne.

Tecnologie utilizzate

Java 21

Spring Boot 4

Spring Security

JWT (JSON Web Tokens)

Spring Data JPA

PostgreSQL

Jakarta Mail (per invio email di verifica account)

Gradle (build tool)

Postman (per test API)

Funzionalità principali

Registrazione utente con validazione email.

Login con JWT, generazione e gestione del token.

Endpoint protetti accessibili solo con token valido.

Recupero informazioni utente autenticato tramite /users/me.

Autorizzazione basata su ruoli (default ROLE_USER).

Invio email di verifica account con codice generato dinamicamente.

CORS configurato per consentire richieste da frontend locali o remoti.

Architettura del progetto

controller/ → gestione degli endpoint REST

service/ → logica di business, generazione token, invio email

config/ → configurazioni Spring Security, filtro JWT e CORS

model/ → entità JPA (User, Role…)

repository/ → interfacce Spring Data JPA per accesso DB

Setup e avvio

Clona il repository:

git clone https://github.com/<tuo-username>/SignUpLogInJWT.git


Configura il database PostgreSQL (ad esempio con Supabase) e aggiorna application.properties o .env.

Avvia il progetto:

./gradlew bootRun


Testa le API con Postman:

POST /auth/register → registrazione utente

POST /auth/login → login, ricezione JWT

GET /users/me → informazioni utente autenticato (richiede Bearer token)

Note sullo studio

Questo progetto è stato pensato principalmente come esercizio di apprendimento e sperimentazione su:

Configurazione di Spring Security in modalità stateless.

Generazione, validazione e gestione di JWT.

Integrazione con database e invio email di verifica.

Uso di filtro custom per intercettare le richieste e autenticare gli utenti.

Miglioramenti futuri

Gestione ruoli multipli e autorizzazioni avanzate.

Refresh token per sessioni prolungate.

Logging centralizzato e gestione errori più dettagliata.

Test unitari e di integrazione per tutte le funzionalità di sicurezza.
