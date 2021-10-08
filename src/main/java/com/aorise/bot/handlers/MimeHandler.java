package com.aorise.bot.handlers;

import com.aorise.bot.BotConst.Type;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class MimeHandler {
    private final TypeGuesser guesser;

    public MimeHandler() {
        this(new MP4AnimationGuesser().andThen(WebpGuesser::new).andThen(ImageOptimisticGuesser::new));
    }

    public MimeHandler(TypeGuesser guesser) {
        this.guesser = guesser;
    }

    public static String getMime(Path path) {
        return URLConnection.guessContentTypeFromName(path.getFileName().toString());
    }

    public Type getType(Path path) {
        return guesser.getType(path);
    }

    public MediaMessageWrapper wrap(String chatId, Path path) {
        Type type = getType(path);
        return switch (type) {
            case PHOTO -> new MediaMessageWrapper.PhotoWrapper(chatId, path);
            case ANIMATION -> new MediaMessageWrapper.AnimationWrapper(chatId, path);
            case VIDEO -> new MediaMessageWrapper.VideoWrapper(chatId, path);
            case UNKNOWN -> new MediaMessageWrapper.DocumentWrapper(chatId, path);
        };
    }

    public interface TypeGuesser {
        Type getType(Path path);
        default TypeGuesser andThen(Function<TypeGuesser, TypeGuesser> f) {
            return f.apply(this);
        }
    }

    public static class MP4AnimationGuesser implements TypeGuesser {
        private final Map<String, Type> mime2type = Map.of(
                "image/png", Type.PHOTO,
                "image/jpeg", Type.PHOTO,
                "image/gif", Type.ANIMATION,
                "video/mp4", Type.ANIMATION,
                "video/quicktime", Type.VIDEO,
                "video/webm", Type.VIDEO);

        public Type getType(Path path) {
            String mime = getMime(path);
            return mime == null ? Type.UNKNOWN : mime2type.getOrDefault(mime, Type.UNKNOWN);
        }
    }

    public static class WebpGuesser implements TypeGuesser {
        private final TypeGuesser guesser;

        public WebpGuesser(TypeGuesser guesser) {
            this.guesser = guesser;
        }

        public Type getType(Path path) {
            if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".webp")) {
                return Type.PHOTO;
            } else {
                return guesser.getType(path);
            }
        }
    }

    public static class ImageOptimisticGuesser implements TypeGuesser {
        private final TypeGuesser guesser;

        public ImageOptimisticGuesser(TypeGuesser guesser) {
            this.guesser = guesser;
        }

        public Type getType(Path path) {
            Type res = guesser.getType(path);
            return res == Type.UNKNOWN ? Type.PHOTO : res;
        }
    }

    public interface MediaMessageWrapper {
        Message execute(AbsSender sender) throws TelegramApiException;
        void setReplyMarkup(ReplyKeyboard keyboard);

        class PhotoWrapper implements MediaMessageWrapper {
            private final SendPhoto photo;

            public PhotoWrapper(String chatId, Path path) {
                this.photo = new SendPhoto(chatId, new InputFile(path.toFile()));
            }

            public Message execute(AbsSender sender) throws TelegramApiException {
                return sender.execute(photo);
            }

            public void setReplyMarkup(ReplyKeyboard keyboard) {
                photo.setReplyMarkup(keyboard);
            }
        }

        class AnimationWrapper implements MediaMessageWrapper {
            private final SendAnimation animation;

            public AnimationWrapper(String chatId, Path path) {
                this.animation = new SendAnimation(chatId, new InputFile(path.toFile()));
            }

            public Message execute(AbsSender sender) throws TelegramApiException {
                return sender.execute(animation);
            }

            public void setReplyMarkup(ReplyKeyboard keyboard) {
                animation.setReplyMarkup(keyboard);
            }
        }

        class VideoWrapper implements MediaMessageWrapper {
            private final SendVideo video;

            public VideoWrapper(String chatId, Path path) {
                this.video = new SendVideo(chatId, new InputFile(path.toFile()));
            }

            public Message execute(AbsSender sender) throws TelegramApiException {
                return sender.execute(video);
            }

            public void setReplyMarkup(ReplyKeyboard keyboard) {
                video.setReplyMarkup(keyboard);
            }
        }

        class DocumentWrapper implements MediaMessageWrapper {
            private final SendDocument document;

            public DocumentWrapper(String chatId, Path path) {
                this.document = new SendDocument(chatId, new InputFile(path.toFile()));
            }

            public Message execute(AbsSender sender) throws TelegramApiException {
                return sender.execute(document);
            }

            public void setReplyMarkup(ReplyKeyboard keyboard) {
                document.setReplyMarkup(keyboard);
            }
        }
    }
}
