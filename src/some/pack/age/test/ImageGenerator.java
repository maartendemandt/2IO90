package some.pack.age.test;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;

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

    public static void generateImage(List<Point> points, int labelWidth, int labelHeight)
    {

        points.forEach(point -> {
            AxisAlignedBB aabb = point.getAABB(labelWidth, labelHeight);
            minX = Math.min(minX, aabb.getX());
            maxX = Math.max(maxX, aabb.getU());
            minY = Math.min(minY, aabb.getY());
            maxY = Math.max(maxY, aabb.getV());
        });
        int width = maxX - minX;
        int height = maxY - minY;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        points.stream()
              .filter(Point::isValid)
              .forEach(point -> {
                  g.setPaint(Color.BLACK);
                  AxisAlignedBB aabb = point.getAABB(labelWidth, labelHeight);
                  g.drawRect(aabb.getX(), aabb.getY(), aabb.getU() - aabb.getX(), aabb.getV() - aabb.getY());
                  g.setPaint(Color.RED);
                  g.setStroke(new BasicStroke(4));
                  g.fill(new Ellipse2D.Double(point.getX() - (POINT_SIZE / 2), point.getY() - (POINT_SIZE / 2), POINT_SIZE, POINT_SIZE));
              });
        File file = new File("images");
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            ImageIO.write(image, "PNG", new File(file, String.format("%d.png", System.currentTimeMillis())));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
