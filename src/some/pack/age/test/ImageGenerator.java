package some.pack.age.test;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.models.labels.AbstractLabel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author DarkSeraphim.
 */
public class ImageGenerator
{
    private static int minX = Integer.MAX_VALUE;
    private static int maxX = Integer.MIN_VALUE;
    private static int minY = Integer.MAX_VALUE;
    private static int maxY = Integer.MIN_VALUE;

    private static final int POINT_SIZE = 20;

    public static void generateImage(List<AbstractLabel> points, int labelWidth, int labelHeight)
    {
        generateImage(points, labelWidth, labelHeight, String.valueOf(System.currentTimeMillis()));
    }

    public static void generateImage(List<AbstractLabel> points, int labelWidth, int labelHeight, String outName)
    {
        /*points.stream().filter(Point::isValid).forEach(point -> {
            AxisAlignedBB aabb = point.getAABB(labelWidth, labelHeight);
            minX = Math.min(minX, aabb.getX());
            maxX = Math.max(maxX, aabb.getU());
            minY = Math.min(minY, aabb.getY());
            maxY = Math.max(maxY, aabb.getV());
        });*/
        int width = 10000;
        int height = 10000;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        g.scale(1,-1);
        g.translate(0, -height);
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        points.stream()
              .forEach(point -> {
                  if (point.isValid())
                  {
                      g.setPaint(Color.BLACK);
                      AxisAlignedBB aabb = point.getAABB(labelWidth, labelHeight);
                      g.drawRect(aabb.getX(), aabb.getY(), aabb.getU() - aabb.getX(), (aabb.getV() - aabb.getY()));
                      g.setPaint(Color.BLUE);
                  }
                  else
                  {
                      g.setPaint(Color.RED);
                  }
                  g.setStroke(new BasicStroke(4));
                  g.fill(new Ellipse2D.Double(point.getX() - (POINT_SIZE / 2),
                                              point.getY() - (POINT_SIZE / 2),
                                              POINT_SIZE, POINT_SIZE));
              });
        File file = new File("images");
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            ImageIO.write(image, "PNG", new File(file, String.format("%s.png", outName)));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
