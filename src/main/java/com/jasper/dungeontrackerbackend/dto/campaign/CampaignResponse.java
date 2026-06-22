package com.jasper.dungeontrackerbackend.dto.campaign;

import com.jasper.dungeontrackerbackend.entities.Campaign;
import com.jasper.dungeontrackerbackend.entities.CharacterEntity;

import java.util.List;

public record CampaignResponse(
        String name,
        String description,
        List<Long> characters
) {
    public static CampaignResponse from(Campaign campaign) {
        return new CampaignResponse(
                campaign.getName(),
                campaign.getDescription(),
                campaign.getCharacters().stream().map(CharacterEntity::getId).toList()
        );
    }
}
