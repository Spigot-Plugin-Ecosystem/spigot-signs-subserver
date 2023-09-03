![Header](./img/readme-header.png)

# Signs (Unterserver)
Dieses Plugin ist für die Unterserver eines BungeeCord-Netzwerks entwickelt.
Es aktualisiert die Serverinformationen wie die Spieleranzahl und die MOTD in einer MySQL-Datenbank, sodass das [Signs-Plugin für Lobby-Server](https://github.com/Spigot-Plugin-Ecosystem/spigot-signs-lobby) aktuelle Daten auf den Schildern anzeigen kann.

## Befehle
#### `/maintenance [ on | off | status ]` (Berechtigung: `maintenance.toggle`)
Schaltet den Wartungsmodus ein oder aus, bzw. gibt den aktuellen Status aus.
Beim Einschalten des Wartungsmodus werden alle Spieler vom Server gekickt, die nicht die Berechtigung `maintenance.bypass` haben.
Ebenfalls ist es auch nur den Spielern mit der genannten Berechtigung möglich, den Server während des eingeschalteten Wartungsmodus zu betreten.

#### `/motd [ motd ]` (Berechtigung: `motd`)
Passt die MOTD (Nachricht des Tages) an. 
Im Parameter `motd` können Farbcodes (`&<code>`) verwendet werden.
Die MOTD wird bei jedem Neustart des Servers übernommen, sie kann aber auch von anderen Plugins überschrieben werden.

## Weitere Berechtigungen
- `maintenance.bypass` - Erlaubt den Aufenthalt auf dem Server während des eingeschalteten Wartungsmodus

## Setup
1. Installiere das Plugin auf einem Unterserver, für den in der Lobby ein Schild existieren soll.
   - Füge dazu die Plugin-JAR-Datei im `plugins`-Verzeichnis des Servers ein.
2. Starte den Server (neu), damit das Plugin geladen wird.
3. Bearbeite die Datei `config.yml` im Plugin-Verzeichnis und trage dort die Zugangsdaten zur MySQL-Datenbank ein (`host`, `port`, `database`, `username` und `password`). Beachte, dass die Zugangsdaten identisch mit denen des Signs-Plugins für Lobby-Server sein müssen.
4. Bearbeite die Datei `messages.yml`, im Plugin-Verzeichnis um die Nachrichten, die das Plugin sendet, anzupassen.
5. Starte den Server neu, um die Änderungen an der Konfigurationsdatei zu übernehmen. In diesem Zuge werden auch - sofern noch nicht geschehen - die Datenbanktabellen automatisch erstellt.
6. Installiere das Plugin auf allen anderen Unterservern, für die in der Lobby ein Schild existieren soll. Befolge dabei unbedingt die folgenden Anweisungen, damit es nicht zu unerwünschten Seiteneffekten kommt:
   1. Kopiere das Plugin-Verzeichnis sowie die Plugin-JAR-Datei an einen separaten Ort, nicht auf einen Unterserver.
   2. Lösche - falls existent - die Datei `server.yml` in der Kopie des Plugin-Verzeichnisses. Dort wird gespeichert, für welchen Unterserver der Datenbankeintrag bearbeitet werden soll. Wird die Datei nicht gelöscht, würde immer der Eintrag des Unterservers bearbeitet werden, auf dem das Plugin aufgesetzt wurde.
   3. Kopiere nun die Kopie des Plugin-Verzeichnisses und die Plugin-JAR-Datei in den `plugins`-Ordner aller Unterserver, auf denen das Plugin installiert werden soll.
7. Starte alle Unterserver, auf denen das Plugin installiert wurde (neu).
   - Beim ersten Start des Servers ist auch mit der Datenbankkonfiguration der BungeeCord-Servername noch unbekannt.
   - Daher existiert noch kein Datenbankeintrag für den Server und somit zeigen auch die Schilder noch an, dass der Server offline sei.
8. Trete jedem Unterserver, auf dem das Plugin installiert wurde, bei und führe ggf. den Befehl `/maintenance off` aus, um den Wartungsmodus zu deaktivieren.
   - Das geht am effizientesten, wenn in der Lobby bereits ein Schild existiert, auf das geklickt wird. Das ist auch möglich wenn es anzeigt, dass der Server offline sei.
   - Mit dem Beitritt wird der BungeeCord-Servername bei der Proxy abgefragt und anschließend in der Datei `server.yml` gespeichert.
   - Nun wird auch der Datenbankeintrag für den Server erstellt und die Schilder zeigen an, dass der Server online (oder im Wartungsmodus, wenn der Befehl nicht ausgeführt wurde) ist.

## Technische Details
#### Unterstützte Minecraft-Versionen
1.20 - 1.20.1

#### MySQL-Datenbank
Zur Verwendung dieses Plugins wird eine MySQL-Datenbank benötigt. 
