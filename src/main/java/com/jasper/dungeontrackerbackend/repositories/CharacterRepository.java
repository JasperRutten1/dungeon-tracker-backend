package com.jasper.dungeontrackerbackend.repositories;

import com.jasper.dungeontrackerbackend.entities.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {
    List<CharacterEntity> findByCampaignId(Long campaignId);
}
