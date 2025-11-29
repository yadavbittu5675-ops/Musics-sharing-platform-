import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class music_player {

    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel mainPanel;

    static ArrayList<String> songList = new ArrayList<>();
    static File currentSong = null;
    static Clip clip;

    public static void main(String[] args) {

        frame = new JFrame("🎵 Music Sharing Platform");
        frame.setSize(650, 520);
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

    static JPanel loginPage() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(25, 25, 40));

        JLabel title = new JLabel("🎧 Music Share Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(150, 40, 400, 50);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(120, 150, 150, 30);
        panel.add(userLabel);

        JTextField username = new JTextField();
        username.setBounds(250, 150, 250, 30);
        panel.add(username);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(250, 230, 150, 40);
        loginBtn.setBackground(new Color(110, 0, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            if (!username.getText().equals("")) {
                cardLayout.show(mainPanel, "home");
            } else {
                JOptionPane.showMessageDialog(frame, "Enter a username!");
            }
        });

        return panel;
    }

    static JPanel homePage() {

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(28, 28, 28));

        JLabel header = new JLabel("🎵 Music Sharing & Player", SwingConstants.CENTER);
        header.setBounds(0, 0, 650, 60);
        header.setOpaque(true);
        header.setBackground(new Color(70, 0, 120));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(header);

        JPanel albumPanel = new JPanel();
        albumPanel.setBounds(40, 90, 180, 180);
        albumPanel.setBackground(new Color(60, 0, 90));
        panel.add(albumPanel);

        JLabel albumArt = new JLabel("🎵", SwingConstants.CENTER);
        albumArt.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        albumPanel.add(albumArt);

        JLabel visualizer = new JLabel("▂▄▆█ ▂▄▆█ ▂▄▆█", SwingConstants.CENTER);
        visualizer.setFont(new Font("Segoe UI", Font.BOLD, 30));
        visualizer.setForeground(new Color(180, 0, 255));
        visualizer.setBounds(240, 120, 360, 40);
        panel.add(visualizer);

        JLabel selected = new JLabel("Selected: None");
        selected.setBounds(240, 170, 360, 30);
        selected.setForeground(Color.WHITE);
        selected.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(selected);

        JButton selectBtn = new JButton("🎶 Select Music");
        selectBtn.setBounds(240, 230, 160, 40);
        selectBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectBtn.setBackground(new Color(100, 0, 150));
        selectBtn.setForeground(Color.WHITE);
        panel.add(selectBtn);

        JButton shareBtn = new JButton("🚀 Share");
        shareBtn.setBounds(420, 230, 160, 40);
        shareBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        shareBtn.setBackground(new Color(0, 150, 100));
        shareBtn.setForeground(Color.WHITE);
        panel.add(shareBtn);

        JButton playBtn = new JButton("▶ Play");
        playBtn.setBounds(240, 290, 160, 40);
        playBtn.setBackground(new Color(0, 120, 180));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(playBtn);

        JButton pauseBtn = new JButton("⏸ Pause");
        pauseBtn.setBounds(420, 290, 160, 40);
        pauseBtn.setBackground(new Color(180, 0, 60));
        pauseBtn.setForeground(Color.WHITE);
        pauseBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(pauseBtn);

        JLabel volLabel = new JLabel("🔊 Volume", SwingConstants.CENTER);
        volLabel.setForeground(Color.WHITE);
        volLabel.setBounds(240, 350, 360, 20);
        panel.add(volLabel);

        JSlider volume = new JSlider(0, 100, 70);
        volume.setBounds(240, 375, 360, 40);
        panel.add(volume);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> songs = new JList<>(listModel);
        songs.setBackground(new Color(35, 35, 35));
        songs.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(songs);
        scroll.setBounds(40, 300, 180, 150);
        panel.add(scroll);

        selectBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose Music File");

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentSong = chooser.getSelectedFile();
                selected.setText("Selected: " + currentSong.getName());
            }
        });

        shareBtn.addActionListener(e -> {
            if (currentSong == null) {
                JOptionPane.showMessageDialog(frame, "Select a file first!");
            } else {
                songList.add(currentSong.getName());
                listModel.addElement(currentSong.getName());
                JOptionPane.showMessageDialog(frame, "🎉 Shared: " + currentSong.getName());
            }
        });

        playBtn.addActionListener(e -> {
            if (currentSong != null) {
                try {
                    if (clip != null && clip.isRunning()) {
                        clip.stop();
                    }

                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(currentSong);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);

                    setVolume(volume.getValue());

                    clip.start();
                    visualizer.setText("▂▄▆█ ▂▄▆█ ▂▄▆█");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Cannot play this file! Use WAV format.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a file first!");
            }
        });

        pauseBtn.addActionListener(e -> {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                visualizer.setText("▁▁▁ ▁▁▁ ▁▁▁");
            }
        });

        volume.addChangeListener(e -> setVolume(volume.getValue()));

        return panel;
    }

    static void setVolume(int value) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * value / 100f) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
                                       }
