# Jak zbudować IceClient na GitHubie (pierwszy build)

Korzeń repozytorium = zawartość folderu `iceclient-mod/` z ZIP-a (tam leżą `build.gradle`, `src/`, `.github/`).
Workflow jest w `.github/workflows/build.yml` i buduje od razu z korzenia repozytorium (nic nie rozpakowuje).

## 1. Utwórz repozytorium
1. Wejdź na https://github.com/new, wpisz nazwę (np. `iceclient-mod`), **nie** zaznaczaj „Add a README”.
2. Gałąź domyślna musi się nazywać `main` (workflow uruchamia się po pushu na `main`).

## 2. Wyślij pliki (wybierz jedną drogę)

### A) Git w terminalu (zalecane – pewnie wyśle też ukryty folder `.github`)
```
unzip iceclient-mod-BUILD-FIX-20261101.zip
cd iceclient-mod
git init -b main
git add .
git commit -m "IceClient: pierwszy build"
git remote add origin https://github.com/TWOJA_NAZWA/iceclient-mod.git
git push -u origin main
```

### B) Przez przeglądarkę
1. Rozpakuj ZIP-a.
2. W pustym repozytorium kliknij „uploading an existing file”.
3. Przeciągnij **zawartość** folderu `iceclient-mod/` (razem z folderem `.github`), nie sam folder `iceclient-mod`.
4. Po wysłaniu sprawdź, że w repozytorium istnieje ścieżka `.github/workflows/build.yml`.
   Jeśli jej nie ma, workflow się nie uruchomi (wtedy użyj drogi A).

## 3. Uruchom build
- Po pushu na `main` build startuje sam. Ręcznie: zakładka **Actions** → „Build IceClient mod” → **Run workflow**.
- Pierwszy build pobiera Minecrafta i zależności, więc trwa kilka minut.

## 4. Pobierz wynik
1. Wejdź w zakończony przebieg (zielony ptaszek) → sekcja **Artifacts** na dole.
2. Pobierz `iceclient-mod-jar` (to ZIP, w środku jest plik `.jar` moda, np. `iceclient-0.1.0+mc1.21.4.jar`).
3. Jeśli build jest czerwony: pobierz artifact `build-log` (i `test-reports`) i wklej log do rozmowy.

## 5. Test w grze
Minecraft Java 1.21.4 + Fabric Loader 0.16.10 lub nowszy + Fabric API `0.114.0+1.21.4`.
Wrzuć `.jar` moda i Fabric API do folderu `.minecraft/mods`. Lista rzeczy do sprawdzenia: `STATUS-ETAPU-M.md`.
