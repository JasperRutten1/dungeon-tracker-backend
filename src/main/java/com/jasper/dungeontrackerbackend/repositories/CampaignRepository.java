package com.jasper.dungeontrackerbackend.repositories;

import com.jasper.dungeontrackerbackend.entities.Campaign;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignRepository {
    List<Campaign> getByOwnerId(UUID ownerId);
}
