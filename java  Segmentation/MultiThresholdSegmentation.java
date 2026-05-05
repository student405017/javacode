import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MultiThresholdSegmentation {

    public static void main(String[] args) {
        try {
            // 1. 讀取原始影像
            File input = new File("C:/Users/User/Desktop/java/article-5e2002f2a1ffb.jpg");
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth();
            int height = image.getHeight();

            // 建立結果影像 (RGB 類型)
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 2. 設定閾值 (可根據直方圖 T_opt 調整)
            // 例如：低於 T1 為背景，高於 T2 為前景，中間為過渡區
            int T1 = 85; 
            int T2 = 150;

            // 3. 遍歷像素進行處理
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // 取得亮度 (假設已是灰階，取紅、綠、藍任一通道即可)
                    Color color = new Color(image.getRGB(x, y));
                    int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                    if (gray < T1) {
                        // 背景：設為黑色
                        result.setRGB(x, y, Color.BLACK.getRGB());
                    } else if (gray >= T1 && gray < T2) {
                        // 中間區域：設為灰色
                        result.setRGB(x, y, Color.GRAY.getRGB());
                    } else {
                        // 前景：設為白色
                        result.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }

            // 4. 輸出結果
            File output = new File("segmented_penguins.jpg");
            ImageIO.write(result, "jpg", output);
            System.out.println("影像分割完成！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}