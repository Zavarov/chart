package vartas.chart;

import org.jfree.chart.JFreeChart;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/*
 * Copyright (C) 2019 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public abstract class AbstractChartTest <T> {
    private AbstractChart<T> chart;

    protected void init(AbstractChart<T> chart){
        this.chart = chart;
    }

    @Test
    public void testGetTitle(){
        assertNull(chart.getTitle());
    }

    @Test
    public void testSetTitle(){
        chart.setTitle("title");
        assertEquals(chart.getTitle(), "title");
    }

    protected void save(String fileName){
        JFreeChart chart = this.chart.create();

        BufferedImage image = chart.createBufferedImage(1024,768);
        Path output = Paths.get("target", fileName+".png");

        try {
            ImageIO.write(image, "png", output.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
