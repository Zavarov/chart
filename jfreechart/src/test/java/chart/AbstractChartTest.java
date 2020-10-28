package chart;

import vartas.chart.Chart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractChartTest <T extends Chart> {
    protected T chart;


    protected void save(String fileName){
        BufferedImage image = this.chart.create(1024, 768);
        Path output = Paths.get("target", fileName+".png");

        try {
            ImageIO.write(image, "png", output.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
