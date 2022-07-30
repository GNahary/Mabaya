package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Set;

// Immutable (well, semi-immutable)! Assuming a campaign can NOT be changed (only deleted, if irrelevant, for example)!
public record Campaign(String name, LocalDateTime startDate, Float bid, Set<String> campaignProducts) {}