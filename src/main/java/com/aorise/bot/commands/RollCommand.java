package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RollCommand extends FileBotCommand {
    public RollCommand(String commandId, String description) {
        super(commandId, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message msg, String[] arguments) {
        String initText = stripCommand(msg);
        try {
            Dices dices = Dices.parse(initText);
            int rolled = dices.roll(FileBot.getRandom());
            sendMessage(bot, msg, Integer.toString(rolled), true);
            return "Rolled";
        } catch (ParseException | NumberFormatException e) {
            sendMessage(bot, msg, "Can't parse.", true);
            return e.getMessage();
        }
    }

    private interface Dices {
        int roll(Random random);

        class Sum implements Dices {
            private final List<Dices> dices;

            public Sum(List<Dices> dices) {
                this.dices = dices;
            }

            public int roll(Random random) {
                return dices.stream().mapToInt(d -> d.roll(random)).sum();
            }
        }

        class Dice implements Dices {
            private final int count;
            private final int face;

            public Dice(int count, int face) {
                this.count = count;
                this.face = face;
            }

            public int roll(Random random) {
                return count + random.ints(count, 0, face).sum();
            }
        }

        class Const implements Dices {
            private final int c;

            public Const(int c) {
                this.c = c;
            }

            public int roll(Random random) {
                return c;
            }
        }

        static Dices parse(String text) throws ParseException {
            Parser parser = new Parser(text);
            return parser.parse();
        }
    }

    private static class Parser {
        private final String text;
        private int pos;

        private Parser(String text) {
            this.pos = 0;
            this.text = text;
        }

        private char get() throws ParseException {
            if (eol()) {
                throw new ParseException("EOL", pos);
            }
            return text.charAt(pos);
        }

        private boolean eol() {
            return pos >= text.length();
        }

        private boolean checkAndIncrement(Predicate<Character> predicate) throws ParseException {
            if (!eol() && predicate.test(get())) {
                pos++;
                return true;
            } else {
                return false;
            }
        }

        public Dices parse() throws ParseException {
            return parseDices();
        }

        private int parseNumber() throws ParseException {
            int startPos = pos;
            while (checkAndIncrement(Character::isDigit)) {
            }
            if (startPos == pos) {
                throw new ParseException("Where is number?", pos);
            }

            int res = Integer.parseInt(text.substring(startPos, pos));
            if (res < 0 || res > 999) {
                throw new NumberFormatException(String.format("%d? Really? Have a mercy, pseudo-random generator is not a magic box.", res));
            }
            if (res == 0) {
                throw new NumberFormatException("0? I can, but I won't.");
            }
            return res;
        }

        private Dices.Sum parseDices() throws ParseException {
            List<Dices> dicesList = new ArrayList<>();
            dicesList.add(parseDice());
            while (parseDelim()) {
                dicesList.add(parseDice());
            }
            if (!eol()) {
                throw new ParseException("Odd characters", pos);
            }
            return new Dices.Sum(dicesList);
        }

        private boolean parseDelim() throws ParseException {
            skipSpaces();
            boolean res = check('+');
            skipSpaces();
            return res;
        }

        private void skipSpaces() throws ParseException {
            while (checkAndIncrement(Character::isWhitespace)) {}
        }

        private boolean check(char c) throws ParseException {
            return checkAndIncrement(ch -> ch == c);
        }

        private Dices parseDice() throws ParseException {
            int count = get() == 'd' ? 1 : parseNumber();
            if (check('d')) {
                int face = parseNumber();
                return new Dices.Dice(count, face);
            } else {
                return new Dices.Const(count);
            }
        }
    }
}
