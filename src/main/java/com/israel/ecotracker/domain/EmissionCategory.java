package com.israel.ecotracker.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "emission_categories")
public class EmissionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String unit;

    @Column(name = "co2_per_unit", nullable = false)
    private Double co2PerUnit;

    public EmissionCategory() {}

    public EmissionCategory(String name, String unit, Double co2PerUnit) {
        this.name = name;
        this.unit = unit;
        this.co2PerUnit = co2PerUnit;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getCo2PerUnit() { return co2PerUnit; }
    public void setCo2PerUnit(Double co2PerUnit) { this.co2PerUnit = co2PerUnit; }
}