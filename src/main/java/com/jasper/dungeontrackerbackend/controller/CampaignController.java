package com.jasper.dungeontrackerbackend.controller;

import com.jasper.dungeontrackerbackend.config.annotations.AuthenticatedUser;
import com.jasper.dungeontrackerbackend.dto.campaign.CampaignResponse;
import com.jasper.dungeontrackerbackend.dto.campaign.CreateCampaignRequest;
import com.jasper.dungeontrackerbackend.entities.Campaign;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.services.CampaignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@AllArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@RequestBody CreateCampaignRequest request, @AuthenticatedUser User user){
        Campaign newCampaign = campaignService.createCampaign(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CampaignResponse.from(newCampaign));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> getAllMyCampaigns(@AuthenticatedUser User user) {
        List<Campaign> campaigns = campaignService.getCampaignsByOwner(user.getId());
        return ResponseEntity.ok(campaigns.stream().map(CampaignResponse::from).toList());
    }


}
