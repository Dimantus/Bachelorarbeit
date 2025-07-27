# Chatbot-Architekturvergleich: Spring MVC vs. WebFlux

Dieses Repository enthält den Quellcode und die Begleitdaten zur Bachelorarbeit:  

Im Rahmen der Arbeit wurde ein webbasiertes Chatsystem mit Frontend und Backend entwickelt, das die Kommunikation mit einem Sprachmodell ermöglicht. Ziel war es, zwei unterschiedliche Backend-Architekturen – Spring MVC (blockierend) und Spring WebFlux (nicht-blockierend) – miteinander zu vergleichen. Untersucht wurde, wie sich diese Architekturen unter hoher Last auf Antwortverhalten und Systemressourcen auswirken.

---

## Inhalt des Repositories

| Ordner             | Beschreibung                                                                 |
|--------------------|------------------------------------------------------------------------------|
| `chatbot/`         | Frontend (React + Tailwind) und Backend (Spring Boot + Spring AI)            |
| `simuliertes-LLM/` | Simulation eines LLMs inkl. Streaming-Ausgabe (für kontrollierte Tests)      |
| `skripte/`         | Lasttest-Tools mit Node.js, Bash-Skripte zur Messung von Systemressourcen    |
| `ergebnisse-testing/` | Messergebnisse und Logdaten der Evaluation             |

---

## Technologien

### Frontend:
- React, TypeScript, Vite, Tailwind CSS, Framer Motion

### Backend:
- Java 21, Spring Boot, Spring MVC / WebFlux, Spring AI  
- Anbindung über LM Studio (lokale Ausführung von Sprachmodellen)

### Evaluation:
- Node.js (für Streaming-Lasttests)
- Docker (für Deployment und Isolation)
- Bash Skript zur Systembeobachtung

---

## Starten der Anwendung (Docker)
Für eine korrekte Auführung muss in den application.properties die richtigen Werte, für die Kommunikation mit dem LLM eingegeben werden.
Zudem muss eventuell in der Datei MyChatAPI im enum der Name des LLMs hinzugefügt werden.
```bash
docker build -t chatbot .
docker run -d -p 8080:8080 chatbot
```

Frontend & Backend sind anschließend unter `http://localhost:8080` erreichbar.  
Für die LLM-Kommunikation muss LM Studio lokal gestartet sein und unter `host.docker.internal` erreichbar sein.

---

## Evaluation & Methodik

Die Evaluation vergleicht Spring MVC und WebFlux unter realitätsnaher Last:
- 10.000 parallele Anfragen in Batches
- simuliertes LLM mit künstlichen Delays
- Vergleich: Antwortzeit (TTFB, Dauer), CPU-, RAM- und Thread-Nutzung

Metriken werden u.a. über Bash- und Node.js-Skripte erhoben.

---

## Lizenz & Hinweise

Dieses Projekt wurde im Rahmen einer Bachelorarbeit erstellt und dient ausschließlich Demonstrations- und Analysezwecken.  
Die Nutzung von LLMs basiert auf lokalen Ressourcen (LM Studio) – es werden keine externen APIs benötigt.

---

## Autor

Serhat Nazlier  
Bachelorarbeit, TH Köln, 2025  
Betreuung: Prof. Dr. Hoai Viet Nguyen & Furkan Sengül
