🎵 Java Swing Music Player

A desktop music player application built using Java Swing and Java Sound API.
This project demonstrates GUI design, event handling, audio playback, and basic media-player features in a single Java file.

✨ Features

🔐 Login Screen

Simple username validation

🎶 WAV Audio Playback

Supports .wav files using javax.sound.sampled

▶️ ⏸ ⏭ ⏮ Playback Controls

Play, Pause, Next, Previous

📃 Playlist Management

Add songs dynamically

View song list

⏱ Real-Time Progress Tracking

Running time & total duration

Progress bar updates automatically

🔊 Volume Control

Adjustable volume slider

Mute / Unmute option

🔁 Repeat Mode

Replay the current song

❤️ Favorite Button

Mark/unmark current song

🎨 Attractive UI

Album art placeholder

Instrument-themed footer

Emoji-based modern controls

🖼️ UI Overview

Login Page

Username field

Login button

Home Page

Playlist panel

Song info & progress bar

Album art section

Playback controls

Volume & repeat controls

Instrument showcase panel

🛠️ Technologies Used

Java (JDK 8+)

Java Swing – GUI

Java Sound API – Audio playback

AWT – Layout & colors

📂 Project Structure
music_player.java   # Complete application in a single file
README.md           # Project documentation

▶️ How to Run
1️⃣ Prerequisites

Java JDK 8 or later installed

IDE (IntelliJ IDEA / Eclipse) or terminal

2️⃣ Compile & Run
javac music_player.java
java music_player


Or simply run the file from your IDE.

🎵 Supported Audio Format

✅ WAV only

❌ MP3 / AAC / FLAC not supported (Java Sound API limitation)

⚠️ Notes & Limitations

Only WAV files are supported

Playlist is not saved after closing the app

Album art is a placeholder

Repeat applies to the current track only

🚀 Future Enhancements

MP3 support (using external libraries)

Playlist save/load feature

Seekable progress bar (drag to change time)

Animated album art

Equalizer controls

Dark mode UI
