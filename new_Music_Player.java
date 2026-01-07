import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class new_Music_Player {

    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel mainPanel;

    static ArrayList<File> playlist = new ArrayList<>();
    static int currentIndex = -1;

    static Clip clip;
    static Timer timer;
    static boolean repeat = false;
    static boolean muted = false;
    static FloatControl volumeControl;

    public static void main(String[] args) {

        frame = new JFrame("🎵 Music Player");
        frame.setSize(800, 680);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPage(), "login");
        mainPanel.add(homePage(), "home");

        frame.add(mainPanel);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "login");
    }

    /* ================= LOGIN PAGE ================= */
    static JPanel loginPage() {

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 236, 210));

        JLabel title = new JLabel("🎧 Music Player Login", SwingConstants.CENTER);
        title.setBounds(0, 200, 800, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(255, 111, 0));
        panel.add(title);

        JTextField user = new JTextField();
        user.setBounds(280, 270, 240, 35);
        panel.add(user);

        JButton login = new JButton("🔐 Login");
        login.setBounds(340, 330, 120, 40);
        login.setBackground(new Color(255, 140, 0));
        login.setForeground(Color.WHITE);
        panel.add(login);

        login.addActionListener(e -> {
            if (user.getText().trim().length() >= 3)
                cardLayout.show(mainPanel, "home");
            else
                JOptionPane.showMessageDialog(frame, "Username must be at least 3 characters");
        });

        return panel;
    }

    /* ================= HOME PAGE ================= */
    static JPanel homePage() {

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 244, 230));

        /* HEADER */
        JLabel header = new JLabel("🎵 Java Music Player", SwingConstants.CENTER);
        header.setBounds(0, 0, 800, 60);
        header.setOpaque(true);
        header.setBackground(new Color(255, 111, 0));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(header);

        /* PLAYLIST */
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(30, 90, 180, 360);
        panel.add(scroll);

        /* SONG INFO */
        JLabel songLabel = new JLabel("No song selected", SwingConstants.CENTER);
        songLabel.setBounds(240, 80, 350, 25);
        songLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(songLabel);

        JLabel timeLabel = new JLabel("00:00 / 00:00", SwingConstants.CENTER);
        timeLabel.setBounds(240, 110, 350, 20);
        panel.add(timeLabel);

        JSlider progress = new JSlider(0, 100, 0);
        progress.setBounds(240, 140, 350, 30);
        panel.add(progress);

        /* ALBUM ART */
        JPanel albumArt = new JPanel(new BorderLayout());
        albumArt.setBounds(280, 180, 270, 180);
        albumArt.setBackground(new Color(255, 224, 178));
        albumArt.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
        albumArt.add(new JLabel("🎵 Album Art", SwingConstants.CENTER), BorderLayout.CENTER);
        panel.add(albumArt);

        /* CONTROLS */
        JButton prev = new JButton("⏮");
        JButton play = new JButton("▶");
        JButton pause = new JButton("⏸");
        JButton next = new JButton("⏭");

        JButton[] controls = {prev, play, pause, next};
        int x = 260;
        for (JButton b : controls) {
            b.setBounds(x, 380, 70, 40);
            b.setBackground(new Color(255, 140, 0));
            b.setForeground(Color.WHITE);
            panel.add(b);
            x += 80;
        }

        /* FAVORITE */
        JButton favBtn = new JButton("♡ Favorite");
        favBtn.setBounds(340, 430, 140, 35);
        favBtn.setBackground(new Color(255, 87, 34));
        favBtn.setForeground(Color.WHITE);
        panel.add(favBtn);

        favBtn.addActionListener(e ->
                favBtn.setText(favBtn.getText().startsWith("♡") ? "♥ Favorited" : "♡ Favorite")
        );

        /* ADD MUSIC */
        JButton addBtn = new JButton("🎶 Add Music");
        addBtn.setBounds(330, 480, 160, 40);
        addBtn.setBackground(new Color(255, 167, 38));
        addBtn.setForeground(Color.WHITE);
        panel.add(addBtn);

        /* NOW PLAYING PANEL */
        JPanel infoPanel = new JPanel(null);
        infoPanel.setBounds(620, 90, 160, 180);
        infoPanel.setBackground(new Color(255, 224, 178));
        infoPanel.setBorder(BorderFactory.createTitledBorder("📀 Now Playing"));

        infoPanel.add(new JLabel("Artist: Unknown")).setBounds(10, 30, 140, 20);
        infoPanel.add(new JLabel("Album: Demo")).setBounds(10, 60, 140, 20);
        infoPanel.add(new JLabel("Format: WAV")).setBounds(10, 90, 140, 20);
        infoPanel.add(new JLabel("Quality: 16-bit")).setBounds(10, 120, 140, 20);
        panel.add(infoPanel);

        /* VOLUME */
        JLabel volLabel = new JLabel("🔊 Volume");
        volLabel.setBounds(640, 300, 100, 20);
        panel.add(volLabel);

        JSlider volume = new JSlider(0, 100, 70);
        volume.setBounds(620, 325, 160, 40);
        panel.add(volume);

        JButton muteBtn = new JButton("🔇 Mute");
        muteBtn.setBounds(635, 370, 130, 35);
        panel.add(muteBtn);

        JCheckBox repeatBox = new JCheckBox("🔁 Repeat");
        repeatBox.setBounds(645, 415, 100, 25);
        repeatBox.setBackground(new Color(255, 244, 230));
        panel.add(repeatBox);

        /* INSTRUMENTS */
        JPanel instrumentPanel = new JPanel();
        instrumentPanel.setBounds(30, 540, 740, 55);
        instrumentPanel.setBackground(new Color(255, 183, 77));
        instrumentPanel.setBorder(BorderFactory.createTitledBorder("🎼 Instruments"));

        String[] instruments = {"🎸 Guitar", "🎹 Piano", "🥁 Drums", "🎻 Violin", "🎷 Sax"};
        for (String ins : instruments)
            instrumentPanel.add(new JLabel(ins));

        panel.add(instrumentPanel);

        /* FOOTER */
        JLabel footer = new JLabel("Ready | Java Swing Music Player 🎶", SwingConstants.CENTER);
        footer.setBounds(0, 605, 800, 35);
        footer.setOpaque(true);
        footer.setBackground(new Color(255, 111, 0));
        footer.setForeground(Color.WHITE);
        panel.add(footer);

        /* EVENTS */
        addBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".wav")) {
                    JOptionPane.showMessageDialog(frame, "Only WAV files supported");
                    return;
                }
                playlist.add(file);
                model.addElement(file.getName());
                if (currentIndex == -1) currentIndex = 0;
            }
        });

        play.addActionListener(e -> playSong(songLabel, progress, timeLabel));
        pause.addActionListener(e -> { if (clip != null) clip.stop(); });
        next.addActionListener(e -> {
            if (currentIndex < playlist.size() - 1) currentIndex++;
            playSong(songLabel, progress, timeLabel);
        });
        prev.addActionListener(e -> {
            if (currentIndex > 0) currentIndex--;
            playSong(songLabel, progress, timeLabel);
        });

        volume.addChangeListener(e -> setVolume(volume.getValue()));

        muteBtn.addActionListener(e -> {
            muted = !muted;
            setVolume(muted ? 0 : volume.getValue());
            muteBtn.setText(muted ? "🔊 Unmute" : "🔇 Mute");
        });

        repeatBox.addActionListener(e -> repeat = repeatBox.isSelected());

        return panel;
    }

    /* ================= PLAY SONG ================= */
    static void playSong(JLabel label, JSlider progress, JLabel timeLabel) {

        if (playlist.isEmpty()) return;

        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(playlist.get(currentIndex));
            clip = AudioSystem.getClip();
            clip.open(ais);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.start();
            label.setText("🎵 " + playlist.get(currentIndex).getName());

            if (timer != null) timer.stop();
            timer = new Timer(500, e -> {
                long cur = clip.getMicrosecondPosition() / 1_000_000;
                long total = clip.getMicrosecondLength() / 1_000_000;

                timeLabel.setText(String.format(
                        "%02d:%02d / %02d:%02d",
                        cur / 60, cur % 60, total / 60, total % 60
                ));

                progress.setValue((int) (100 * cur / total));
            });
            timer.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Unable to play song");
        }
    }

    /* ================= VOLUME ================= */
    static void setVolume(int value) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue(min + (max - min) * value / 100f);
        }
    }
          }
