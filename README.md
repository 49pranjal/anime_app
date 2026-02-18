# 🎬 Anime Explorer — Jikan API

A modern Android app that fetches and displays top anime using the Jikan API.  
Built with clean MVVM architecture, offline-first approach, and robust error handling.

---

## 🚀 Features Implemented

### 🏠 Anime List Screen
- Fetches **Top Anime** from Jikan API
- Displays:
  - Title
  - Episode count
  - Rating (with star indicator)
  - Poster image
- Offline-first behavior using Room
- Auto-refresh when internet reconnects
- Proper loading and error states

---

### 📄 Anime Detail Screen
- Displays detailed anime information:
  - Title
  - Rating
  - Episodes
  - Genres
  - Cast/Producers
  - Synopsis
- Video Player/Poster shown based on availability of youtube_id
- **Watch on YouTube** button when embedded trailer available
- Graceful fallback when trailer embedding is restricted
- Cache-first detail loading

---

### 💾 Offline Support
- Top anime cached locally using Room
- Detail data cached per anime
- App works without internet
- Smart refresh when connectivity returns (list screen)

---

### 🌐 Network Resilience
- Custom NetworkMonitor using ConnectivityManager
- Automatic retry on reconnection (list)
- Offline fallback for detail screen
- No crashes during connectivity changes

---

### ⚠️ Error Handling
Handled gracefully for:

- ✅ API failures
- ✅ Database write failures
- ✅ No internet scenarios
- ✅ Empty state handling
- ✅ Trailer unavailable cases

---

## 🧠 Assumptions Made

- Jikan trailer responses often do not provide reliable `youtube_id` or direct playable URLs.
- In many cases, only `embed_url` is available and several trailers are **not embeddable due to YouTube restrictions**.
- When inline playback is restricted, showing the poster along with a **Watch on YouTube** button is considered acceptable and user-friendly behavior.
- Producers are used as a proxy for "Main Cast" since MAL API does not always provide consistent cast data.
- Paging was not implemented since it was not explicitly required.
- Anime list is refreshed fully on successful fetch (no diff sync).

---

## ⚠️ Known Limitations

- Some trailers cannot be played inline because Jikan often provides only `embed_url`, and many of these videos have **YouTube embed restrictions**.
- For such cases, the app intentionally shows the poster with a **Watch on YouTube** button that opens the trailer externally.
- No pagination for top anime (loads first page only).
- No pull-to-refresh on list (auto refresh on reconnect instead).
- Detail screen does not auto-refresh when network returns (intentional per requirement).

---

## 🧱 Architecture

### Pattern
- **MVVM (Model–View–ViewModel)**
- **Single Activity + Multiple Fragments**
- **Repository Pattern**
- **Offline-first approach**

---

### Layers
```
UI (Activity / Fragments)
↓
ViewModel (StateFlow for UI state)
↓
Repository
↓
Room Database ←→ Retrofit API
```
---

## 🛠️ Tech Stack

| Area | Technology |
|---|---|
Language | Kotlin |
UI | XML + ViewBinding |
Architecture | MVVM |
Async | Kotlin Coroutines + Flow |
Networking | Retrofit + OkHttp |
JSON Parsing | Gson |
Local Storage | Room Database |
Dependency Injection | Hilt |
Image Loading | Glide |
Navigation | Jetpack Navigation Component |

---

## 🗂️ Project Structure
```
│
├── data/
│ ├── local/ (Room entities, DAO, database)
│ ├── remote/ (API services, DTOs)
│ └── repository/ (AnimeRepo, Extensions)
│
├── di/ (Hilt modules)
│
├── ui/
│ ├── animelist/
│ ├── animedetail/
│ └── uiModels/
│
├── utils/ (NetworkMonitor, constants)
│
├── MainActivity/
│
└── AnimeApplication/
```
---

## ✨ UX & Design Considerations

- Clear loading, success, and error states
- Media-first layout for detail screen
- Accessible touch targets
- Proper typography hierarchy
- Status bar and toolbar color consistency
- Defensive UI for missing data

---

## 🔮 Possible Future Improvements

- Paging 3 integration  
- Pull-to-refresh  
- Shimmer loading  
- Unit tests and UI tests  
- Modularization  

---

## ▶️ How to Run

1. Clone the repository  
2. Open in Android Studio (latest stable)  
3. Sync Gradle  
4. Run on emulator or physical device  

_No API key required — Jikan is public._
