package com.jiangyue.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AvatarGenerator {

    private static final int SIZE = 150;
    private static final double TEXT_RATIO = 0.68;
    private static final String[] COLORS = {
            "#FF0000", "#FF4500", "#FF8C00", "#FFA500", "#FFD700", "#FFFF00",
            "#FF1493", "#FF69B4", "#FFB6C1", "#FFA07A", "#FF6347", "#FF7F50",
            "#CD853F", "#DAA520", "#F4A460", "#DEB887", "#FFE4B5", "#FFF8DC",
            "#FFEFD5", "#FFFACD", "#FFFFE0", "#FFFFF0", "#FAFAD2", "#FDF5E6",
            "#F0E68C", "#BDB76B"
    };

    public static void generateAvatarImages(String outputDir) {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            BufferedImage image = createAvatar(letter, COLORS[i]);
            try {
                String filename = String.format("avatar_%s.png", letter);
                ImageIO.write(image, "PNG", new File(dir, filename));
                System.out.println("Generated: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static BufferedImage createAvatar(char letter, String colorHex) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // 设置抗锯齿和高品质渲染
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 填充背景色（纯色）
        Color bgColor = Color.decode(colorHex);
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, SIZE, SIZE);

        // 加载微软雅黑字体（Microsoft YaHei），fallback到默认sans-serif
        Font font = new Font("Microsoft YaHei", Font.BOLD, 1);
        FontRenderContext frc = g2d.getFontRenderContext();

        // 计算字体大小，使文字占68%面积
        int targetWidth = (int) (SIZE * TEXT_RATIO);
        int fontSize = 1;
        while (true) {
            font = font.deriveFont(Font.BOLD, fontSize);
            Rectangle2D bounds = font.getStringBounds(String.valueOf(letter), frc);
            if (bounds.getWidth() >= targetWidth || fontSize > 200) break;
            fontSize++;
        }

        // 设置文字属性并绘制
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(String.valueOf(letter));
        int textHeight = metrics.getHeight();
        int x = (SIZE - textWidth) / 2;
        int y = (SIZE + textHeight / 3) - metrics.getDescent(); // 垂直居中

        g2d.drawString(String.valueOf(letter), x, y);

        g2d.dispose();
        return image;
    }

    public static void main(String[] args) {
        generateAvatarImages("avatars/");
    }
}
