# Ohjelmointistudio 1 - Web server 2021: osa 1

## Esitehtävä: Web-palvelu

**Tee tämä esitehtävä parisi kanssa tällä viikolla. Seuraavassa tehtävässä rakennamme tätä kirjastoa käyttäen oman pienen palvelun.**

Tässä esitehtävässä on tarkoitus seurata valmista vaiheittaista ohjetta ja tutustua **cask** -web frameworkiin.

Esitehtävällä on kolme tarkoitusta:

1. Pääsette kokeilemaan cask:ia ennen seuraavaa luentoa. Pohdimme silloin millaisen webipalvelun voisimme muokata esimerkin pohjalta.
2. Varmistatte, että teillä on toimiva ympäristö, jossa toteutatte ryhmän kanssa seuraavan miniprojektin
3. Pääset kokeilemaan versionhallintaa vielä kerran ennen seuraavaa ryhmätyötä.

---

## Cask mini web Framework

Käytämme tehtävässä Cask-nimistä web-sovelluskehystä (framework), joka tarjoaa työkalut
ja pohjan, jolle voidaan rakentaa tietyn kaltaisia sovelluksia. Cask hoitaa suuren osan webistä tulevien kutsujen esikäsittelystä sekä tiedonvälityksestä käyttäjän ja pikku palvelimemme välillä. Meidän tehtävämme on kertoa mitä scala-metodeja kutsua kun joku vierailee palvelumme eri sivuilla ja päättää millaisia webisivuja metodimme luovat. Cask välittää nuo sivut takaisin sivuston käyttäjälle.


## Esitehtävä, seuraa tutoriaalia

Sinun ei kuitenkaan tarvitse vielä tietää miten caskia kaytetään - **seuraat vain valmista tutoriaalia**. Tutoriaalissa rakennetaan vaiheittain chat-sivusto, jonka käyttäjät voivat keskustella keskenään selaimeen avaamansa webisivun kautta.

**Teemme kuitenkin muutaman asian eri tavoin kuin tutoriaalissa.**

1) Saatte valmiin pohjan jossa on tehty tutoriaalin aivan ensimmäiset askeleet. Lähinnä olemme muokanneet projektin IntelliJ-yhteensopivaksi ilman tarvetta käyttää Mill-työkalua.
2) Käytämme uudempia versioita kirjastoista. Ainoa asia joka muuttuu versiomuutosten vuoksi on, että koodissa ei tarvita (eikä tule käyttää) `render` -metodikutsua.
3) Tallennamme muutaman version tutoriaalia seurattaessa versionhallintaan. Tämän tarkoitus on harjoittaa vieläkin committien kirjoittelua, niin että siitä tulee luonteva osa työskentelyä.

Käsittelemme luennolla tarkemmin mitä tutoriaalissa tapahtui ja kuinka HTTP ja HTML esimerkissä toimivat.

### Tutoriaali

Seuraamme tehtävässä tutoriaalia sivulla https://www.lihaoyi.com/post/SimpleWebandApiServerswithScala.html

Aloita tutoriaalin seuraaminen luomalla itsellesi **fork** projektista https://version.aalto.fi/gitlab/oseppala/cask-demo-base-2021

1) Avaa selaimeen sivu https://version.aalto.fi/gitlab/oseppala/cask-demo-base-2021

2) Klikkaa tekstiä Fork sivun vasemman yläkulman lähellä. Hyväksy ehdotukset ja odota kunnes oma forkisi on valmis.

3) Valitse Clone-napin takaa "clone with SSH" ja kopioi sen osoite leikepöydälle. (pitäisi onnistua klikkaamalla harmaata leikepöydän symbolia)

4) Avaa IntelliJ ja File -> New -> Project from Version Control

5) Kun projekti on latautunut, valitse IntelliJ:n alalaidan välilehdistä sbt shell. Käynnistä sbt alalaidan ikkunan vasemmasta laidatsa vihreästä play-napista. Kun SBT on käynnistynyt, kirjoita ikkunaan komento `run`

6) Hetken kuluttua voit avata selaimella osoitteen `localhost:8080`

7) Sinun pitäisi nyt nähdä sivu jolla on otsikko *Hello!* ja sen alla teksti *World*.

8) Etsi sivulta https://www.lihaoyi.com/post/SimpleWebandApiServerswithScala.html otsikko "Serving HTML" ja selaimeen avaamaasi sivua vastaava kuva ja jatka tutoriaalin seuraamista.

9) Tutoriaalin joka askelella poistettavat rivit on merkitty "-"-merkeillä ja lisättävät "+"-merkeillä. Aina suoritettuasi jonkin muutoksen, tee `git commit`. Kiinnitä huomiota commit-viestiin. Ensimmäinen Commit viesti voisi olla vaikkapa "Improve page styling"

10) Jaa tutoriaalin lopuksi projekti ryhmäsi assistentin kanssa.

11) Valmista tuli!

---

## Uutta syntaksia esimerkeissä

Tehdessäsi tutoriaalia törmäät varmastikin muutamaan täysin uudenlaiseen scala-rakenteeseen.

#### DSL - hassuja operaattoreita ja backtickejä

Scalatags kirjastossa on tehty tietoinen valinta luoda uusi operaattori := jolla voidaan esitellä HTML attribuutti ja sen arvo. Kyse on kuitenkin vain Scala-metodikutsusta, jossa metodilla on totutusta poikkeva nimi ":=".

Esimerkissä on myös ` -merkkejä joidenkin sanojen kuten *type* ympärillä. Syynä tähän on se, että type on scala-kielen varattu sana ja sen kirjoittaminen ilman "hipsuja" aiheuttaisi virheilmoituksen jos sen nimistä muuttujaa, metodia tai luokkaa yrittäisi käyttää. Onneksi tällaisia tilanteita on harvoin. HTML-puolella sanat type ja class kuitenkin esiintyvät aivan eri merkityskessä kuin scalassa.

#### For-Yield

**html, head, link...** ovat tavallisia Scalatags-kirjaton metodikutsuja joilla on samat nimet kuin vastaavilla HTML-rakenteilla. Kyse on kuitenkin tavallisista metodikutsuista.

**For-yield** -rakenne on vaihtoehtoinen tapa kirjoittaa map, filter ja flatmap -kutsuja. Käytännössä on kyse for-lauseesta, joka palauttaa kokoelman. Yield-lauseella kerrotaan millaisia alkioita kokoelmaan tulee.

``` scala
val lista = List(1, 2, 3)

val powers  = for(a <- lista)
                yield a * a

val powers2 = lista.map(a => a * a)
```

#### Annotaatiot

**@annotaatiot** ovat mekanismi, jolla monissa ohjelmointikielissä liitetään metatietoa luokka tai funktiomäärittelyihin. Tässä tutoriaalissa annotaatiota käytettiin web-palvelun osoitteiden (reittien) liittämiseksi funktioihin, jotka käsittelevät näihin osoitteisiin tulevat web-pyynnöt.

Tyypillisesti näillä annetaan lisätietoa käytetylle kirjastolle, sovelluskehykselle tai suoraan scala-kääntäjälle.
