package com.jasper.dungeontrackerbackend.services;

import com.jasper.dungeontrackerbackend.dto.campaign.CreateCampaignRequest;
import com.jasper.dungeontrackerbackend.entities.Campaign;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.repositories.CampaignRepository;
import com.jasper.dungeontrackerbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public Campaign createCampaign(CreateCampaignRequest createCampaignRequest, UUID ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Campaign campaign = Campaign.builder()
                .name(createCampaignRequest.name())
                .description(createCampaignRequest.description())
                .owner(owner)
                .build();

        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    public List<Campaign> getCampaignsByOwner(UUID ownerId) {
        return campaignRepository.findByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    public Optional<Campaign> getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId);
    }

    @Transactional
    public void deleteCampaign(Long campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new IllegalArgumentException("Campaign not found with ID: " + campaignId);
        }
        campaignRepository.deleteById(campaignId);
    }
}
