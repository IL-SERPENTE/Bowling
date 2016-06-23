package net.samagames.bowling.image;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Rigner for project Bowling.
 */
public class BImage
{
    private BufferedImage bufferedImage;
    private ImageConfiguration imageConfiguration;

    public BImage(ImageConfiguration imageConfiguration) throws IOException
    {
        this.imageConfiguration = imageConfiguration;
        this.bufferedImage = ImageIO.read(new File(imageConfiguration.getFilePath()));
    }

    public void draw(String username, int[] scores)
    {
        Graphics2D graphics2D = this.bufferedImage.createGraphics();
        graphics2D.setColor(Color.BLACK);

        graphics2D.setFont(Font.getFont(this.imageConfiguration.getUserNameFont().getName()).deriveFont((float)this.imageConfiguration.getUserNameFont().getSize()));
        graphics2D.drawString(username, this.imageConfiguration.getUserNamePos().getX(), this.imageConfiguration.getUserNamePos().getY());

        graphics2D.setFont(Font.getFont(this.imageConfiguration.getSubScoresFont().getName()).deriveFont((float)this.imageConfiguration.getSubScoresFont().getSize()));
        int[] summaries = new int[10];
        Arrays.fill(summaries, 0);
        for (int i = 0; i < 10; i++)
        {
            int score = scores[i * 2];
            if (score != -1)
                graphics2D.drawString(score == 10 ? "X" : String.valueOf(score), this.imageConfiguration.getScorePos().get(i).get(1).getX(), this.imageConfiguration.getScorePos().get(i).get(1).getY());
            int score2 = scores[i * 2 + 1];
            if (score2 != -1)
                graphics2D.drawString(score + score2 == 10 ? "/" : String.valueOf(score2), this.imageConfiguration.getScorePos().get(i).get(2).getX(), this.imageConfiguration.getScorePos().get(2).get(1).getY());
            if (i == 9)
            {
                int score3 = scores[i * 2 + 2];
                if (score3 != -1)
                    graphics2D.drawString(score3 == 10 ? "X" : String.valueOf(score3), this.imageConfiguration.getScorePos().get(i).get(3).getX(), this.imageConfiguration.getScorePos().get(i).get(3).getY());
            }

            if (scores[i * 2] == 10)
            {
                int n = 0;
                for (int j = i * 2 + 1; j < scores.length && n < 2; j++)
                    if (scores[j] != -1)
                    {
                        summaries[i] += scores[j];
                        n++;
                    }
                summaries[i] += 10;
            }
            else if (scores[i * 2] + scores[i * 2 + 1] == 10)
            {
                if (scores[i * 2 + 2] != -1)
                    summaries[i] += scores[i * 2 + 2];
                summaries[i] += 10;
            }
            else if (scores[i * 2] != -1)
            {
                summaries[i] += scores[i * 2];
                if (scores[i * 2 + 1] != -1)
                    summaries[i] += scores[i * 2 + 1];
            }
        }
        if (scores[20] != -1)
            summaries[9] += scores[20];

        graphics2D.setFont(Font.getFont(this.imageConfiguration.getScoreFont().getName()).deriveFont((float)this.imageConfiguration.getScoreFont().getSize()));
        int sum = 0;
        for (int i = 0; i < summaries.length; i++)
        {
            sum += summaries[i];
            graphics2D.drawString(String.valueOf(sum), this.imageConfiguration.getScorePos().get(i).get(0).getX(), this.imageConfiguration.getScorePos().get(i).get(0).getY());
        }
    }

    public String send() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(this.bufferedImage, "png", baos);

        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Client-ID " + this.imageConfiguration.getApiKey());
        urlConnection.setDoOutput(true);

        OutputStream outputStream = urlConnection.getOutputStream();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(this.bufferedImage, "png", os);
        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        os.close();

        byte[] buffer = new byte[8192];
        int n;
        while (-1 != (n = inputStream.read(buffer)))
            outputStream.write(buffer, 0, n);

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        InputStream is;
        boolean error = false;
        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            is = urlConnection.getInputStream();
        else
        {
            error = true;
            is = urlConnection.getErrorStream();
        }

        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(is);
        while (scanner.hasNext())
            sb.append(scanner.next());

        scanner.close();

        if (error)
            throw new IOException(sb.toString());

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        return "http://i.imgur.com/" + jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString() + ".png";
    }
}
