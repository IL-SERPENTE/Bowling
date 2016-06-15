package net.samagames.bowling.image;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.api.SamaGamesAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project Bowling.
 */
public class ImageConfiguration
{
    private String path;
    private String apiKey;
    private BPos userNamePos;
    private BPos summaryPos;
    private BFont userNameFont;
    private BFont scoreFont;
    private BFont subScoresFont;
    private List<List<BPos>> scorePos;

    public ImageConfiguration() throws NullPointerException
    {
        JsonObject jsonObject = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("image", null).getAsJsonObject();
        this.path = jsonObject.get("file").getAsString();
        this.apiKey = jsonObject.get("imgur-api-key").getAsString();

        JsonObject userNamePos = jsonObject.get("username").getAsJsonObject();
        this.userNamePos = new BPos(userNamePos.get("x").getAsInt(), userNamePos.get("y").getAsInt());

        JsonObject summaryPos = jsonObject.get("username").getAsJsonObject();
        this.summaryPos = new BPos(summaryPos.get("x").getAsInt(), summaryPos.get("y").getAsInt());

        JsonArray fonts = jsonObject.get("fonts").getAsJsonArray();
        this.userNameFont = new BFont(fonts.get(0).getAsJsonObject().get("name").getAsString(), fonts.get(0).getAsJsonObject().get("size").getAsInt());
        this.scoreFont = new BFont(fonts.get(1).getAsJsonObject().get("name").getAsString(), fonts.get(1).getAsJsonObject().get("size").getAsInt());
        this.subScoresFont = new BFont(fonts.get(2).getAsJsonObject().get("name").getAsString(), fonts.get(2).getAsJsonObject().get("size").getAsInt());

        this.scorePos = new ArrayList<>();
        JsonArray scores = jsonObject.get("scores").getAsJsonArray();
        for (int i = 0; i < 10; i++)
        {
            List<BPos> list = new ArrayList<>();
            JsonObject scorePos = scores.get(i).getAsJsonObject();
            list.add(new BPos(scorePos.get("x").getAsInt(), scorePos.get("y").getAsInt()));
            list.add(new BPos(scorePos.get("x2").getAsInt(), scorePos.get("y2").getAsInt()));
            list.add(new BPos(scorePos.get("x3").getAsInt(), scorePos.get("y3").getAsInt()));
            if (i == 9)
                list.add(new BPos(scorePos.get("x4").getAsInt(), scorePos.get("y4").getAsInt()));
            this.scorePos.add(list);
        }
    }

    public String getFilePath()
    {
        return path;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public BPos getUserNamePos()
    {
        return this.userNamePos;
    }

    public BFont getUserNameFont()
    {
        return this.userNameFont;
    }

    public BFont getScoreFont()
    {
        return this.scoreFont;
    }

    public BFont getSubScoresFont()
    {
        return this.subScoresFont;
    }

    public List<List<BPos>> getScorePos()
    {
        return this.scorePos;
    }

    public BPos getSummaryPos()
    {
        return summaryPos;
    }

    public static class BPos
    {
        private int x;
        private int y;

        private BPos(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }
    }

    public static class BFont
    {
        private String name;
        private int size;

        private BFont(String name, int size)
        {
            this.name = name;
            this.size = size;
        }

        public String getName()
        {
            return name;
        }

        public int getSize()
        {
            return size;
        }
    }
}
