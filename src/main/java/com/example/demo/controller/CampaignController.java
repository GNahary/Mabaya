package com.example.demo.controller;

import com.example.demo.model.Campaign;
import com.example.demo.service.CampaignManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("campaign")
public class CampaignController {

    @Autowired
    private CampaignManagementService campaignManagementService;

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Campaign addCampaign(@RequestBody Campaign campaign){
        campaignManagementService.processNewCampaign(campaign);
        return campaign;
    }

}
