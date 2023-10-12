# Eksamen DATA1600 Programutvikling, VÅR 2021
Oppgaven ble kodet på IntelliJ IDEA
Ved oppstart kan det være behov for å laste ned maven og velge hovedmodulen.
Og for dette prosjektet brukte vi JDK 1.8.

Vi har laget to ferdig brukere som du kan bruke for å logge inn.
1. Admin
Brukernavn: admin
Passord: admin123

2. Kunde
Brukernavn: kunde
Passord: kunde123

Programmet er laget som en nettbutikk for gårdsprodukter, som inneholder 7 sider totalt.
1. Innlogging: her velger du om å logge inn som en admin eller en kunde. Ellers kan du velge å registrere en ny bruker på nettsiden.
2. Admin_Meny: Admin kan velge mellom å få oversikt over brukere eller produkter i systemet.
3. Admin_VisBrukere: Oversikt over alle brukere; redigere info og slette brukere.
4. Admin_VisProdukter: Oversikt over alle produkter; redigere info, slette produkter og legge til nye produkter.
5. Kunde_Meny: Kunden kan velge mellom å vise alle produktene som er tilgjengelige på nettsiden, eller logge ut. 
6. Kunde_Handleside: Oversikt over produkter som er tilgjngelig, og kan begynne å handle. (Når man skal søke et bestemt kategori må man taste noe inn på input feltet for at innholde skal filtreres.)
7. RegistrerNyBruker: Her kan brukeren registrere seg, å velge mellom å lage en ny admin- eller kunde-bruker.


Grunnen til at vi har 8 ulike sider, speiler vår variasjon av tolkning av oppgaventeksten. Så vi valgte å inkludere alle ideene så besvarelsen av oppgaven
ble mer oppfattende og fullstendig. Vi konkluderte med at personen som bruker systemet kan være en admin, som registrerer produkter. Intuitivt så 
vil vi se at produktene faktisk blir registrert til nettbutikken. Også har vi en kundeside som ser de registrerte produktene og kan handle, med sitt eget
GUI.

Vi har tenkt på brukervennlighet, når det kommer til GUI-et i denne oppgaven. Fordi brukergrensesnittet gir brukeren muligheter til å bruke nettbutikken 
på en intuitiv måte. Feks. bruker en del av funksjonene våres på TableView, som gjør det lettere for brukeren å se informasjon, samtidig gjøre endringer 
hvis det trengs.

