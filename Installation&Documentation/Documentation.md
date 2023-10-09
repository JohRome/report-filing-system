# Dokumentation: Johan's Disturbance Reporter

### Arbete utfört av
+ Johan Romeo - JIN23

### Beskrivning av projektet
I sin helhet låter applikationen användaren:
+ Fylla i en störningsrapport om något som har hänt i fastigheten, t.ex störande ljud ifrån grannar sent på kvällen.
+ Störningsrapporten skickas sedan med hjälp av en Kafka Producer, mot ett web API.
+ En Kafka Consumer lyssnar på det som skickas, kontrollerar så att innehållet är bra, och skickar sedan vidare ärendet in i en MongoDB.
+ En Kafka Consumer lyssnar på det som skickas och skriver ut innehållet till modulen som skickar störningsrapporten.
+ Detta ärende får ett MongoID och en boolean, IsSolved, som automatiskt är satt till false, eftersom ärendet först måste åtgärdas innan det kan sättas till true.
## Arbetet och dess genomförande
Arbetet började med att förstå konceptet i hur man använder Apache Kafka som integrationsplattform och jag använde mig då av [den här spellistan](https://www.youtube.com/playlist?list=PLGRDMO4rOGcNLwoack4ZiTyewUcF6y6BU).  
Jag valde sedan att komma på en idé om hur jag ville forma mitt egna program - Jag kom på vad för typ av data jag vill skicka, hur denna data ska skickas och behandlas och även vilken databas som ska lagra datan.
### Vad som varit svårt
Själva implementationen i applikationen har inte varit omöjlig, utan det svåraste har varit strukturen i projektet och att modulerna har varit bråkiga under hela projektets gång.  
En felplacerad klass i en modul kan ha förödande konsekvenser och göra programmet helt okörbart. Därför är det av största vikt att ha tydliga ansvarsområden för varje enskild modul, så att de ej "krockar" med en annan modul.  
Något som var besvärligt till en början var även att låta en modul ha en annan modul som en dependency.
### Beskriv lite olika lösningar du gjort samt motiveringar till dessa
+ **KafkaConfig-modulen**
    + TopicConfig:
        + *Lösning:* Skapande av Topic med ett valt namn, replikering och partitionering
        + *Motivering:* Tack vare att programmet har 3 Brokers får varje Broker en partition. Detta gör att programmet kan fortsätta fungera även om en Broker försvinner. Nackdelen med detta är att det är en större mängd data som skickas men jag anser att det är nödvändigt för att programmet ska fungera optimalt.
---

+ **KafkaMongoConsumer-modulen**
    + ReportEntity:
        + *Lösning:* Skapande av en mall för hur data ska se ut när den skickas till MongoDB
        + *Motivering:* Av det jag har testat hittills så är det smidigast att ha en entitet-klass som representerar data i form av ett Document som går in i MongoDB. Annoteringarna gör användningen ännu lättare, t.ex @Id som genererar ett automatiskt ObjectId.


+ MongoConsumer:
    + *Lösning:* Lyssnar på en angiven topic och hämtar data i JSON-format, utför en enklare kontroll av JSON så att allt stämmer och skickas sedan in i MongoDB samt fångar upp eventuella fel.
    + *Motivering:* Jag valde att göra en enklare kontroll av JSON-formatet för att förhindra att programmet kraschar samt att det tas emot som en JSON-sträng istället för ett Java-objekt. Detta medförde en större simplicitet för mig som utvecklare, dels på klient-sidan så som på server-sidan genom att låta MongoConsumer mappa om JSON till en ReportEntity. Felhantering är även viktigt för att förhindra att programmet kraschar.


+ ReportRepository:
    + *Lösning:* Gör det möjligt för MongoConsumer att enkelt spara data i MongoDB.
---

+ **KafkaProducer-modulen**
    + KafkaProducer:
        + *Lösning:* Ansvarar för att skicka data, genom en JSON-sträng som inparameter, till en specifik Topic med hjälp av en MessageBuilder. Ansvarar även för att fånga upp eventuella fel så att program inte kraschar.
        + *Motivering:* Genom att låta Producern skicka en JSON-sträng istället för ett Java-objekt slipper jag ha ännu en klass som behöver representera datan jag skickar. I en tidigare lösning hade jag just det, och det medförde massa trassel. Dels pga att jag inte visste till en början att man inte kunde "circulera" klasser som tillhörde en modul, utan behövde göra en helt separat modul som t.ex håller alla DTO-klasser. Enligt mig så är det enklare och snyggare att skicka en JSON-sträng och låta varje modul välja hur denne vill handskas med datan, genom Singel Responsibility principen.


+ RestController:
    + *Lösning:* Sätter upp ett API med en endpoint som gör det möjligt att låta användaren skicka data med en POST-request. Metoden tar emot en JSON-sträng samt fångar eventuella Response-fel.
    + *Motivering:* Egentligen samma motivering som för KafkaProducer. JSON-string blir enklare att hantera i det långa loppet, då jag anser att det är enklare att skicka en JSON-sträng än ett Java-objekt. Felhanteringen gör även att programmet inte kraschar lika lätt.
---

+ **POJOs-modulen**
    + *Lösning:* POJO klasser som behövs av både PostToAPI-modulen och KafkaMongoConsumer-modulen.
    + *Motivering:* Detta gör att andra moduler som behöver använda sig av dessa klasser kan göra det utan att behöva skapa egna klasser, vilket hade brutit mot DRY-principen samt att det blir snyggare.

---

+ **PostToAPI-modulen**
    + ApacheKafkaAPI:
        + *Lösning:* Här utförs själva HTTP POST-request med hjälp av Apache HTTP client och ser till att data som ska skickas är i JSON-format genom Spring Boots integrerade dependency, Jackson.
        + *Motivering:* Jag valde att använda mig av Apache HTTP client för att utföra POST-requesten, då jag tyckte att det var enkelt att använda och det var enkelt att få till en fungerande POST-request. Jag valde även att använda mig av Spring Boots integrerade dependency för att konvertera data till JSON-format. Detta gjorde att jag slapp använda mig av ytterligare dependencies.



+ Application:
    + *Lösning:* Ger användaren olika menyval och tillåter även att loopa programmet tills det att användaren bestämmer sig för att avsluta.


+ ConsoleConsumer:
    + *Lösning:* På begäran skrivs alla messages i en topic ut i konsolen till användaren i ett snyggare JSON-format tack vare en utility klass.
    + *Motivering:* Tidigare lösning var att Consumern skrev ut så fort något hade skickats mot API:et, men efter att ha följt en lektion av min lärare, Marcus.H, så bestämde jag mig att göra om det till nuvarande implementation istället. Dock så är metoden i fråga static, vilket jag personligen inte hade valt eftersom det kommer bli svårare att skriva tester på den, men den gör sitt jobb väl så jag är nöjd.


+ ReportDTO:
    + *Lösning:* Implementerar Serialized. Skapar en mall för hur JSON-strängen ska se ut när den skickas mot API:et.
    + *Motivering:* Här har jag däremot valt att skapa en DTO-klass för att representera data. Detta gör det lättare att lägga in användarens input till varje fält samt att det är lättare att låta ett bibliotek konvertera data till JSON-format istället för att skriva det själv.

+ ReportDTOHandler:
    + *Lösning:* Ansvarar för att skapa en ReportDTO.
    + *Motivering:* Jag ville ha en separat klass som utför skapandet istället för Application-klassen. Varför jag gjorde detta var för att jag inte ville sprida ut ansvarsområden över flera klasser, och istället låta en hantera detta. Detta medför att jag kan lättare felsöka om något skulle gå fel.

+ Sender(Interface):
    + *Lösning:* Gör det möjligt för klasser som implementerar dessa metoder att implementera dem hur de själva vill.
    + *Motivering:* Detta gör det möjligt att ha flera klasser med olika implementationer när det kommer till att utföra POST-request samt att göra om data till JSON-format. Detta kan vara användbart när man t.ex vill byta http-client, bibliotek för JSON, eller viljan att skicka mot en annan integrationsplattform.


+ Serialized(Interface):
    + *Lösning:* Ett interface avsedd för alla DTO-klasser.
    + *Motivering:* När DTO-klasser implementerar detta interface ges möjligheten för ApacheKafkaAPI, eller en annan klass som implementerar Sender-interface, att kunna ta emot en Serialized som inparameter, istället för varje enskild DTO. I skrivande stund är ReportDTO den enda klassen, men om det hade funnits fler klasser så slipper man att skriva en metod för varje klass.

---

+ **Utilities-modulen**
    + *Lösning:* Tidigare lösning var att ha nedanstående klasser inuti PostToAPI-modulen men jag valde att flytta dem till Utilities-modulen istället.
    + *Motivering:* Väljer jag att bygga ut programmet i framtiden kommer jag behöva dessa klasser och då är det bättre att kunna lägga till Utilities som en dependency istället för att skriva om allting på nytt.

        + Input:
            + *Lösning:* Ansvarar för att ta användarens input.
            + *Motivering:* Att se till att data som matas in är korrekt så att det förhindrar problem vidare i programmet är av största vikt. Används felaktiga värden, som "åäöÅÄÖ" t.ex, så bes användaren att mata in korrekt värde till det att kravet uppnås. Detta medför att programmet inte kraschar lika lätt och att det inte dyker upp konstiga tecken i MongoDB.

        + JSONFormatter:
            + *Lösning:* Ansvarar för att formatera JSON-strängar till ett snyggare format.
            + *Motivering:* Detta gör att det blir lättare att läsa JSON-strängar i konsolen. I nuläget hade jag egentligen kunnat skippa att bygga en hel klass för detta, eftersom det bara är en metod i dagsläget som använder sig utav den. Detta kommer dock att visa sig vara bra i framtiden när jag vill bygga vidare på projektet.

        + Output:
            + *Lösning:* Skriver ut menyalternativ för användaren samt meddelar om input är ej korrekt.
            + *Motivering:* I nuläget en liten klass, men återigen, när jag vidareutvecklar programmet kommer jag att behöva fler menyer bla, och då är det bra att ha en klass som sköter detta.
---

### Beskriv något som var besvärligt att få till
Implementationen av POST-request var ganska besvärlig. Det blev en hel del problem innan jag kom på att jag behövde annotera DTO-klassen med @JsonProperty. Detta gjorde att jag kunde få till en fungerande POST-request.
Något som har varit smått frustrerande och irriterande var återigen, modulerna. Vidare förklaring om detta nedan.
Det som än idag skaver är att jag inte lyckats automatisera ledarbytet när en Broker försvinner. I mitt fall är det Broker2 som måste överleva för att programmet ska kunna köras och för att MongoConsumer måste kunna fungera. Stänger man av Broker1 och Broker3 så fungerar fortfarande programmet men låter man dessa vara uppe utan Broker2 fungerar inte programmet öht. Detta medför att programmet måste förlita sig på att Broker2 är uppe och körandes. Detta är något jag vill lösa i framtiden.
### Beskriv om du fått byta lösning och varför i sådana fall
Min första lösning var att ha 3 olika program. Ett program skulle sköta Apache Kafka, ett program skulle sköta allt med POST-request samt att låta användaren fylla i formuläret, ett program skulle låta en användare, som i själva verket skulle vara en "hyresvärd"/"styrelse", utföra CRUDL-operationer mot databasen för att bla kunna markera ärendet som löst, aka IsSolved = true.
Detta visade sig bli otroligt rörigt, pga att du och jag som användare, blev då tvungna att ha flera fönster uppe och körandes samtidigt.
Jag valde därför att göra om programmet till ett enda program, där allt sköts i ett och samma fönster. Detta gjorde att jag kunde få en bättre överblick över vad som hände i programmet och det blev även lättare att felsöka.
---
Efter att ha experimenterat lite ytterligare efter ovan rad var skriven så bestämde jag mig för att ändra om lite i programmet och skapa nya moduler för att vissa delar i programmet kan återanvändas istället för att göras om på nytt.
## Slutsatser
Överlag är jag mycket visare och klokare än vad jag var när jag precis började med projektet. Detta är något jag kommer att ta med mig som minne och vidareutveckla på ännu mer i kommande kurser och i arbetslivet.
Programmet i sin helhet är ganska okomplicerat och det är lätt att förstå vad som händer. Att följa med i koden är inte heller så svårt.
### Vad gick bra
Något som gick bra var egentligen allt utom planering av moduler i tidigare försök till skapande av projektet. Jag har lärt mig att jag inte ska komplicera saker mer än vad som är nödvändigt.
### Vad gick dåligt
Något som var dåligt är helt klart att jag inte lyckats få löst Broker2-problemet. När detta är löst kommer vilken Broker som helst kunna ta över rollen som ledare och medför en större säkerhet i programmet.
### Vad har du lärt dig
Jag har lärt mig otroligt mycket under projektets gång och för att inte lista upp allting så listar jag upp enbart det viktigaste:
+ En grundlig överblick över hur man kan använda Spring Boot för att lätt kunna skriva en applikation från grunden.
+ Att kunna sätta upp ett Apache Kafka-kluster med 3 Brokers för att få en högre säkerhet i programmet.
+ Att snabbt och enkelt kunna sätta upp ett API med hjälp av Spring Boot för att låta en användare göra en POST-request.
+ Att kunna skicka data från klientsidan, till en server med hjälp av Apache Kafka som integrationslösning.
+ Förstå kraften av Spring Boot applikationer och hur mycket "hjälp på vägen" det medför när mycket av koden är redan skriven och med hjälp av @Annoteringar kunna diktera hur koden ska bete sig.
+ Förstå hur viktigt det är att ha tydliga ansvarsområden för varje modul och att inte låta en modul göra för mycket.
### Vad hade du gjort annorlunda om du gjort om projektet
Hade jag börjat om projektet från grunden hade jag helt klart velat implementera min första lösning; att ha olika program för de olika användningsområdena. Detta hade gjort programmet mer modulärt och efterliknat "Microservice Architecture", vilket är ett koncept jag kommer på egen hand utforska i.
### Vilka möjligheter ser du med de kunskaper du fått under kursen.
Efter snart avslutad kurs har jag fått en bra grund att stå på vad gäller integrationsplattformar. Just integrationsplattformar har öppnat upp en helt ny värld av vad som är möjligt att göra med, i detta fall, Apache Kafka.
Jag ser fram emot att få utforska mer av Apache Kafka och dess möjligheter, samt att få utforska fler integrationslösningar.
