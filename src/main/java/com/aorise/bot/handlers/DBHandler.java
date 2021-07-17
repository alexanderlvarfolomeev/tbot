package com.aorise.bot.handlers;

import com.aorise.db.service.*;
import org.springframework.stereotype.Component;

@Component
public class DBHandler {
    private final TagService tagService;
    private final SyntagService syntagService;
    private final RestrictionService restrictionService;
    private final RateService rateService;
    private final ChatContextService chatContextService;

    public DBHandler(TagService tagService, SyntagService syntagService, RestrictionService restrictionService, RateService rateService, ChatContextService chatContextService) {
        this.tagService = tagService;
        this.syntagService = syntagService;
        this.restrictionService = restrictionService;
        this.rateService = rateService;
        this.chatContextService = chatContextService;
    }

    public TagService getTagService() {
        return tagService;
    }

    public SyntagService getSyntagService() {
        return syntagService;
    }

    public RestrictionService getRestrictionService() {
        return restrictionService;
    }

    public RateService getRateService() {
        return rateService;
    }

    public ChatContextService getChatContextService() {
        return chatContextService;
    }
}
