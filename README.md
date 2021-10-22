# Chat_App

## Lancement de l'Application : 

- `cd in src`
- Compilation : ```javac -d ../classes/ -cp ../classes/ ./Model/*.java ./Service/*.java ./stream/*.java ./Utils/*.java```
- Lancement du serveur : `cd in classes` et `java stream.EchoServerMultiThreaded <port>`
- Lancement d'un client : `cd in classes` et `java stream.EchoClient <ip> <port>`
