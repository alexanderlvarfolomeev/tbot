package com.aorise.bot.handlers;

import net.kodehawa.lib.imageboards.DefaultImageBoards;
import net.kodehawa.lib.imageboards.ImageBoard;
import net.kodehawa.lib.imageboards.boards.DefaultBoards;
import net.kodehawa.lib.imageboards.entities.Rating;
import net.kodehawa.lib.imageboards.entities.exceptions.QueryParseException;
import net.kodehawa.lib.imageboards.entities.impl.DanbooruImage;
import net.kodehawa.lib.imageboards.entities.impl.KonachanImage;
import net.kodehawa.lib.imageboards.requests.RequestFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class BooruHandler {
    public void getDanbooruByTag(String tag) {
//        ImageBoard.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 OPR/78.0.4093.186");
//        System.out.println(temp(0, 1, tag, Rating.SAFE));
//        DefaultImageBoards.KONACHAN.search(1, tag, Rating.SAFE).async(i -> System.out.println(i.get(0).getFile_url()), t -> t.printStackTrace(System.out));

//        KonachanImage image = DefaultImageBoards.KONACHAN.search(1, tag, Rating.SAFE).blocking().get(0);
//        System.out.println(image.getFile_url());
//        System.out.println(image.getParsedFileUrl());
    }

    private String temp(int page, int limit, String search, Rating rating) {
        DefaultBoards board = DefaultBoards.KONACHAN;
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme(board.getScheme())
                .host(board.getHost())
                .addPathSegments(board.getPath())
                .query(board.getQuery())
                .addQueryParameter("limit", String.valueOf(limit));

        if (page != 0)
            urlBuilder.addQueryParameter(board.getPageMarker(), String.valueOf(page));
        if (search != null) {
            StringBuilder tags = new StringBuilder(search.toLowerCase().trim());
            if (rating != null) {
                // I call cursed.
                boolean appendLongTag = (board == DefaultBoards.GELBOORU || board == DefaultBoards.R34 || board == DefaultBoards.SAFEBOORU);
                tags.append(" rating:").append(appendLongTag ? rating.getLongName() : rating.getShortName());
            }

            // Why, just, why, why would you return anything but?
            // Other imageboards do this aswell, but we can't filter on all
            // Use BoardImage#isPending to check yourself
//            if (getImageType() == FurryImage.class || getImageType() == SafeFurryImage.class || getImageType() == DanbooruImage.class) {
//                tags.append(" status:active");
//            }

            urlBuilder.addQueryParameter("tags", tags.toString());
        }

        HttpUrl url = urlBuilder.build();
        return (String) new RequestFactory(new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build()).makeRequest(url, response -> {
            try (ResponseBody body = response.body()) {
                if (body == null) {
                    return Collections.emptyList();
                }
                String s = body.string();

                body.close();

                return s;
            } catch (IOException e) {
                throw new QueryParseException(e);
            }
        }).blocking();
    }
}
