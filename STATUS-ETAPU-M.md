# IceClient – etap M (mod Fabric 1.21.4): stan rzetelnie

## Napisane
Podstawa moda, podmiana menu głównego i pauzy, własne przyciski (wygląd + animacja najechania), tło zimowe
na ekranach poza grą, płynne przejścia, animacja ładowania z kostką Rubika, ekran „Menu IceClient",
ustawienia IceClient (zapisywane do pliku), konfiguracja GitHub Actions.

## Sprawdzone lokalnie (bez Minecrafta)
- 17 testów logiki (łagodzenie animacji, kolory, śnieg, ustawienia, kostka Rubika) – przeszły.
- Kostka: pełna sekwencja układa kostkę; 4 ćwierć obroty każdej warstwy = stan wyjściowy.
- Wyrenderowane klatki kostki obejrzane w obrazku (poprawna bryła i obrót warstwy).
- Składnia całego kodu Javy (parsowanie) bez błędów.

## NIE skompilowane
Kod zależny od Minecrafta (ekrany, mixiny, rysowanie) NIE był kompilowany: w moim środowisku nie ma sieci,
więc nie pobrałem Minecrafta ani Fabrica. Pierwsza kompilacja w GitHub Actions może pokazać błędy
(nazwy klas/metod 1.21.4). Naprawię je po wklejeniu logu.

## NIE sprawdzone w grze
Wygląd, animacje, działanie mixinów i przejść – nic z tego nie widziałem w uruchomionym Minecrafcie.

## Intro ładowania: pochodzenie i licencja
- „Reimagined Intro" (autor NoobTrieu) jest na Modrinth na licencji All Rights Reserved, a jego animacja pochodzi
  z materiału Minecraft Dungeons (własność Mojang/Microsoft). Dlatego żadnych jego plików ani klatek nie używamy.
- Intro IceClient to własna realizacja w kodzie (bez obrazów i wideo): kostki zlatują się w kostkę, litery logo
  wjeżdżają kolejno, po napisie przechodzi błysk, kostka miesza się i układa. Nie widziałem oryginału, więc
  to inspiracja ogólnym kinowym stylem, nie odtworzenie. Nie używamy logo ani nazwy Mojang.
- To nie jest porada prawna.

## Ograniczenia tej wersji
- Wybór świata i listy serwerów to ekrany Minecrafta z tłem IceClient i przyciskami IceClient (nie pełna przebudowa list).
- Esc z F3 (pauza bez menu) też otwiera ekran pauzy IceClient.
- HUD, Mody, Kosmetyki w „Menu IceClient" są wyszarzone (niezaimplementowane).
- Tylko Minecraft 1.21.4.

## Lista testów w grze
1. Gra startuje z modem (w logu: „IceClient zaladowany").
2. Ekran ładowania pokazuje kostkę, która się miesza i układa.
3. Menu główne: przyciski wsuwają się, reagują na najechanie, kostka się obraca.
4. „Jeden gracz": wybór świata ma zimowe tło; wejście do świata działa.
5. Esc w grze: pauza IceClient; „Wróć do gry" działa; „Zapisz i wyjdź" wraca do menu.
6. Menu IceClient → Ustawienia: zmiana akcentu, animacji, śniegu, przejść działa od razu i po restarcie.
7. „Wiele graczy" otwiera listę serwerów; „Opcje Minecrafta" otwiera opcje.
8. „Wyjdź z gry" zamyka grę.
