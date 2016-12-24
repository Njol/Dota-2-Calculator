"use strict";

// generated on Mon Jun 20 10:27:07 CEST 2016

var items = {
    abyssal_blade: new Env({
        "hp_regen": 15,
        "cost": 6400,
        "level": 1,
        "base_id": "abyssal_blade",
        "is_consumable": false,
        "hp": 250,
        "bonus_damage_mag": [{
            "damage": 85,
            "lifesteal_mult": 1,
            "mults": {"chance": {
                "is_ranged": 0.1,
                "is_melee": 0.25
            }},
            "cooldown": 2.3
        }],
        "str": 10,
        "built_from": [
            "basher",
            "vanguard"
        ],
        "mana_cost": 75,
        "name": "Abyssal Blade",
        "cooldown": 35,
        "block": [{"mults": {"amount": {
            "is_ranged": 20,
            "is_melee": 40
        }}}],
        "attack_damage_total": 10
    }),
    aether_lens: new Env({
        "hp_regen": 8,
        "cost": 2300,
        "mp": 250,
        "spell_damage_bonus_mult": 0.05,
        "level": 1,
        "base_id": "aether_lens",
        "built_from": [
            "energy_booster",
            "ring_of_health"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Aether Lens",
        "cooldown": 0
    }),
    ultimate_scepter: new Env({
        "agi": 10,
        "cost": 4200,
        "mp": 175,
        "level": 1,
        "base_id": "ultimate_scepter",
        "is_consumable": false,
        "hp": 175,
        "has_scepter": 1,
        "int": 10,
        "str": 10,
        "built_from": [
            "blade_of_alacrity",
            "ogre_axe",
            "point_booster",
            "staff_of_wizardry"
        ],
        "mana_cost": 0,
        "name": "Aghanim's Scepter",
        "cooldown": 0
    }),
    courier: new Env({
        "cost": 100,
        "level": 1,
        "base_id": "courier",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Animal Courier",
        "cooldown": 0
    }),
    arcane_boots: new Env({
        "cost": 1300,
        "mp": 250,
        "level": 1,
        "base_id": "arcane_boots",
        "built_from": [
            "boots",
            "energy_booster"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Arcane Boots",
        "cooldown": 55,
        "movement_speed": 50
    }),
    arcane_boots_active: new Env({
        "cost": 1300,
        "mp": 250,
        "level": 1,
        "base_id": "arcane_boots",
        "built_from": [
            "boots",
            "energy_booster"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Arcane Boots (active)",
        "cooldown": 55,
        "mp_regen": 2.4545454545454546,
        "movement_speed": 50
    }),
    armlet: new Env({
        "hp_regen": 7,
        "cost": 2370,
        "armor": 5,
        "level": 1,
        "base_id": "armlet",
        "built_from": [
            "blades_of_attack",
            "gloves",
            "helm_of_iron_will"
        ],
        "is_consumable": false,
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Armlet of Mordiggian",
        "cooldown": 0.7,
        "attack_damage_total": 9
    }),
    armlet_active: new Env({
        "hp_regen": -38,
        "cost": 2370,
        "level": 1,
        "base_id": "armlet",
        "is_consumable": false,
        "mp_regen": 0,
        "str": 25,
        "armor": 9,
        "built_from": [
            "blades_of_attack",
            "gloves",
            "helm_of_iron_will"
        ],
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Armlet of Mordiggian (active)",
        "cooldown": 0.7,
        "attack_damage_total": 40
    }),
    assault: new Env({
        "cost": 5250,
        "armor": 15,
        "level": 1,
        "base_id": "assault",
        "built_from": [
            "chainmail",
            "hyperstone",
            "platemail"
        ],
        "is_consumable": false,
        "attack_speed_hero": 35,
        "mana_cost": 0,
        "name": "Assault Cuirass",
        "cooldown": 0,
        "enemy_armor": -5,
        "attack_speed_all": 20
    }),
    boots_of_elves: new Env({
        "agi": 6,
        "cost": 450,
        "level": 1,
        "base_id": "boots_of_elves",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Band of Elvenskin",
        "cooldown": 0
    }),
    bfury: new Env({
        "hp_regen": 6,
        "cost": 4500,
        "level": 1,
        "base_id": "bfury",
        "is_consumable": false,
        "mults": {"attack_damage_base_bonus_mult_hero": {
            "enemy_is_creep_and_hero_is_melee": 0.6000000000000001,
            "enemy_is_creep_and_hero_is_ranged": 0.25
        }},
        "built_from": [
            "broadsword",
            "claymore",
            "pers",
            "quelling_blade"
        ],
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 0,
        "name": "Battle Fury",
        "cooldown": 4,
        "cleave_mult": 0.35,
        "attack_damage_total": 55
    }),
    belt_of_strength: new Env({
        "str": 6,
        "cost": 450,
        "level": 1,
        "base_id": "belt_of_strength",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Belt of Strength",
        "cooldown": 0
    }),
    black_king_bar: new Env({
        "str": 10,
        "cost": 3975,
        "level": 1,
        "base_id": "black_king_bar",
        "built_from": [
            "mithril_hammer",
            "ogre_axe"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Black King Bar",
        "cooldown": 80,
        "attack_damage_total": 24
    }),
    blade_mail: new Env({
        "cost": 2200,
        "armor": 6,
        "level": 1,
        "base_id": "blade_mail",
        "built_from": [
            "broadsword",
            "chainmail",
            "robe"
        ],
        "is_consumable": false,
        "mana_cost": 25,
        "name": "Blade Mail",
        "cooldown": 18,
        "int": 10,
        "attack_damage_total": 22
    }),
    blade_of_alacrity: new Env({
        "agi": 10,
        "cost": 1000,
        "level": 1,
        "base_id": "blade_of_alacrity",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Blade of Alacrity",
        "cooldown": 0
    }),
    blades_of_attack: new Env({
        "cost": 420,
        "level": 1,
        "base_id": "blades_of_attack",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Blades of Attack",
        "cooldown": 0,
        "attack_damage_total": 9
    }),
    blight_stone: new Env({
        "cost": 300,
        "level": 1,
        "base_id": "blight_stone",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Blight Stone",
        "cooldown": 0,
        "enemy_armor": -2
    }),
    blink: new Env({
        "cost": 2250,
        "level": 1,
        "base_id": "blink",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Blink Dagger",
        "cooldown": 12
    }),
    bloodstone_0_charges: new Env({
        "hp_regen": 9,
        "cost": 4875,
        "mp": 425,
        "level": 1,
        "base_id": "bloodstone",
        "is_consumable": false,
        "hp": 475,
        "mp_regen": 0,
        "built_from": [
            "soul_booster",
            "soul_ring"
        ],
        "mp_regen_base_bonus_mult": 2,
        "mana_cost": 0,
        "name": "Bloodstone (0 charges)",
        "cooldown": 300,
        "attack_damage_total": 0
    }),
    bloodstone_12_charges: new Env({
        "hp_regen": 9,
        "cost": 4875,
        "mp": 425,
        "level": 1,
        "base_id": "bloodstone",
        "is_consumable": false,
        "hp": 475,
        "mp_regen": 12,
        "built_from": [
            "soul_booster",
            "soul_ring"
        ],
        "mp_regen_base_bonus_mult": 2,
        "mana_cost": 0,
        "name": "Bloodstone (12 charges)",
        "cooldown": 300,
        "attack_damage_total": 0
    }),
    bloodstone_24_charges: new Env({
        "hp_regen": 9,
        "cost": 4875,
        "mp": 425,
        "level": 1,
        "base_id": "bloodstone",
        "is_consumable": false,
        "hp": 475,
        "mp_regen": 24,
        "built_from": [
            "soul_booster",
            "soul_ring"
        ],
        "mp_regen_base_bonus_mult": 2,
        "mana_cost": 0,
        "name": "Bloodstone (24 charges)",
        "cooldown": 300,
        "attack_damage_total": 0
    }),
    bloodstone_6_charges: new Env({
        "hp_regen": 9,
        "cost": 4875,
        "mp": 425,
        "level": 1,
        "base_id": "bloodstone",
        "is_consumable": false,
        "hp": 475,
        "mp_regen": 6,
        "built_from": [
            "soul_booster",
            "soul_ring"
        ],
        "mp_regen_base_bonus_mult": 2,
        "mana_cost": 0,
        "name": "Bloodstone (6 charges)",
        "cooldown": 300,
        "attack_damage_total": 0
    }),
    bloodthorn: new Env({
        "cost": 7195,
        "level": 1,
        "base_id": "bloodthorn",
        "is_consumable": false,
        "int": 25,
        "crit": [{
            "chance": 0.2,
            "multiplier": 1.75
        }],
        "built_from": [
            "lesser_crit",
            "orchid"
        ],
        "attack_speed_hero": 30,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Bloodthorn",
        "cooldown": 11,
        "attack_damage_total": 60
    }),
    bloodthorn_active: new Env({
        "true_strike_chance_all": 1,
        "cost": 7195,
        "orchid_mult": 0.3,
        "level": 1,
        "base_id": "bloodthorn",
        "is_consumable": false,
        "mp_regen": -9.090909090909092,
        "int": 25,
        "crit": [
            {
                "chance": 0.2,
                "multiplier": 1.75
            },
            {
                "chance": 1,
                "multiplier": 1.45
            }
        ],
        "built_from": [
            "lesser_crit",
            "orchid"
        ],
        "attack_speed_hero": 30,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Bloodthorn (active)",
        "cooldown": 11,
        "attack_damage_total": 60
    }),
    boots: new Env({
        "cost": 400,
        "level": 1,
        "base_id": "boots",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Boots of Speed",
        "cooldown": 0,
        "movement_speed": 45
    }),
    travel_boots: new Env({
        "cost": 2400,
        "level": 1,
        "base_id": "travel_boots",
        "built_from": ["boots"],
        "is_consumable": false,
        "mana_cost": 75,
        "name": "Boots of Travel 1",
        "cooldown": 45,
        "movement_speed": 100
    }),
    travel_boots_2: new Env({
        "cost": 4400,
        "level": 2,
        "base_id": "travel_boots_2",
        "built_from": [
            "recipe_travel_boots",
            "travel_boots"
        ],
        "is_consumable": false,
        "mana_cost": 75,
        "name": "Boots of Travel 2",
        "cooldown": 45,
        "movement_speed": 100
    }),
    bottle: new Env({
        "hp_regen": 2.25,
        "movement_speed_bonus_mult": 0,
        "cost": 660,
        "level": 1,
        "base_id": "bottle",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Bottle (3 uses every 2 minutes)",
        "cooldown": 0.5,
        "mp_regen": 1.5
    }),
    bracer: new Env({
        "str": 6,
        "agi": 3,
        "cost": 525,
        "level": 1,
        "base_id": "bracer",
        "built_from": [
            "circlet",
            "gauntlets"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Bracer",
        "cooldown": 0,
        "int": 3,
        "attack_damage_total": 3
    }),
    broadsword: new Env({
        "cost": 1200,
        "level": 1,
        "base_id": "broadsword",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Broadsword",
        "cooldown": 0,
        "attack_damage_total": 18
    }),
    buckler: new Env({
        "str": 2,
        "agi": 2,
        "cost": 800,
        "armor": 5,
        "level": 1,
        "base_id": "buckler",
        "built_from": [
            "branches",
            "chainmail"
        ],
        "is_consumable": false,
        "mana_cost": 10,
        "name": "Buckler",
        "cooldown": 25,
        "int": 2
    }),
    buckler_active: new Env({
        "agi": 2,
        "cost": 800,
        "level": 1,
        "base_id": "buckler",
        "is_consumable": false,
        "mp_regen": -0.4,
        "int": 2,
        "str": 2,
        "armor": 7,
        "built_from": [
            "branches",
            "chainmail"
        ],
        "mana_cost": 10,
        "name": "Buckler (active)",
        "cooldown": 25
    }),
    butterfly: new Env({
        "agi": 35,
        "cost": 5775,
        "level": 1,
        "base_id": "butterfly",
        "built_from": [
            "eagle",
            "quarterstaff",
            "talisman_of_evasion"
        ],
        "is_consumable": false,
        "attack_speed_hero": 30,
        "mana_cost": 0,
        "name": "Butterfly",
        "cooldown": 25,
        "enemy_hit_chance_hero": 0.65,
        "attack_damage_total": 30
    }),
    butterfly_active: new Env({
        "agi": 35,
        "movement_speed_bonus_mult": 0.35,
        "cost": 5775,
        "level": 1,
        "base_id": "butterfly",
        "is_consumable": false,
        "enemy_hit_chance_hero": 1,
        "mp_regen": 0,
        "built_from": [
            "eagle",
            "quarterstaff",
            "talisman_of_evasion"
        ],
        "attack_speed_hero": 30,
        "mana_cost": 0,
        "name": "Butterfly (active)",
        "cooldown": 25,
        "attack_damage_total": 30
    }),
    chainmail: new Env({
        "cost": 550,
        "armor": 5,
        "level": 1,
        "base_id": "chainmail",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Chainmail",
        "cooldown": 0
    }),
    circlet: new Env({
        "str": 2,
        "agi": 2,
        "cost": 165,
        "level": 1,
        "base_id": "circlet",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Circlet",
        "cooldown": 0,
        "int": 2
    }),
    clarity: new Env({
        "hp_regen": 0,
        "cost": 50,
        "level": 1,
        "base_id": "clarity",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Clarity",
        "cooldown": 0,
        "mp_regen": 3.8000000000000003
    }),
    claymore: new Env({
        "cost": 1400,
        "level": 1,
        "base_id": "claymore",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Claymore",
        "cooldown": 0,
        "attack_damage_total": 21
    }),
    cloak: new Env({
        "cost": 550,
        "level": 1,
        "base_id": "cloak",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Cloak",
        "cooldown": 0,
        "vulnerability_mag": 0.85
    }),
    crimson_guard: new Env({
        "hp_regen": 8,
        "agi": 2,
        "cost": 3550,
        "level": 1,
        "base_id": "crimson_guard",
        "is_consumable": false,
        "hp": 250,
        "int": 2,
        "str": 2,
        "armor": 5,
        "built_from": [
            "buckler",
            "vanguard"
        ],
        "mana_cost": 0,
        "name": "Crimson Guard",
        "cooldown": 46,
        "block": [{"mults": {"amount": {
            "is_ranged": 16,
            "is_melee": 32
        }}}]
    }),
    crimson_guard_active: new Env({
        "hp_regen": 8,
        "agi": 2,
        "cost": 3550,
        "level": 1,
        "base_id": "crimson_guard",
        "is_consumable": false,
        "hp": 250,
        "mp_regen": 0,
        "int": 2,
        "str": 2,
        "armor": 7,
        "built_from": [
            "buckler",
            "vanguard"
        ],
        "mana_cost": 0,
        "name": "Crimson Guard (active)",
        "cooldown": 46,
        "block": [{
            "chance": 1,
            "mults": {"amount": {
                "is_ranged": 55,
                "is_melee": 55
            }}
        }]
    }),
    lesser_crit: new Env({
        "cost": 2120,
        "crit": [{
            "chance": 0.2,
            "multiplier": 1.75
        }],
        "level": 1,
        "base_id": "lesser_crit",
        "built_from": [
            "blades_of_attack",
            "broadsword"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Crystalys",
        "cooldown": 0,
        "attack_damage_total": 30
    }),
    greater_crit: new Env({
        "cost": 5520,
        "crit": [{
            "chance": 0.3,
            "multiplier": 2.2
        }],
        "level": 1,
        "base_id": "greater_crit",
        "built_from": [
            "demon_edge",
            "lesser_crit"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Daedalus",
        "cooldown": 0,
        "attack_damage_total": 81
    }),
    dagon: new Env({
        "str": 3,
        "agi": 3,
        "cost": 2720,
        "level": 1,
        "base_id": "dagon",
        "built_from": [
            "null_talisman",
            "staff_of_wizardry"
        ],
        "is_consumable": false,
        "mana_cost": 180,
        "name": "Dagon 1",
        "cooldown": 35,
        "int": 16,
        "attack_damage_total": 9
    }),
    dagon_active: new Env({
        "agi": 3,
        "spell_dps_mag_single": 11.428571428571429,
        "cost": 2720,
        "level": 1,
        "base_id": "dagon",
        "is_consumable": false,
        "mp_regen": -5.142857142857143,
        "int": 16,
        "str": 3,
        "built_from": [
            "null_talisman",
            "staff_of_wizardry"
        ],
        "mana_cost": 180,
        "name": "Dagon 1 (active)",
        "cooldown": 35,
        "attack_damage_total": 9
    }),
    dagon_2: new Env({
        "str": 3,
        "agi": 3,
        "cost": 3970,
        "level": 2,
        "base_id": "dagon_2",
        "is_consumable": false,
        "mana_cost": 180,
        "name": "Dagon 2",
        "cooldown": 30,
        "int": 19,
        "attack_damage_total": 9
    }),
    dagon_2_active: new Env({
        "agi": 3,
        "spell_dps_mag_single": 16.666666666666668,
        "cost": 3970,
        "level": 2,
        "base_id": "dagon_2",
        "is_consumable": false,
        "mp_regen": -6,
        "int": 19,
        "str": 3,
        "mana_cost": 180,
        "name": "Dagon 2 (active)",
        "cooldown": 30,
        "attack_damage_total": 9
    }),
    dagon_3: new Env({
        "str": 3,
        "agi": 3,
        "cost": 5220,
        "level": 3,
        "base_id": "dagon_3",
        "is_consumable": false,
        "mana_cost": 180,
        "name": "Dagon 3",
        "cooldown": 25,
        "int": 22,
        "attack_damage_total": 9
    }),
    dagon_3_active: new Env({
        "agi": 3,
        "spell_dps_mag_single": 24,
        "cost": 5220,
        "level": 3,
        "base_id": "dagon_3",
        "is_consumable": false,
        "mp_regen": -7.2,
        "int": 22,
        "str": 3,
        "mana_cost": 180,
        "name": "Dagon 3 (active)",
        "cooldown": 25,
        "attack_damage_total": 9
    }),
    dagon_4: new Env({
        "str": 3,
        "agi": 3,
        "cost": 6470,
        "level": 4,
        "base_id": "dagon_4",
        "is_consumable": false,
        "mana_cost": 180,
        "name": "Dagon 4",
        "cooldown": 20,
        "int": 25,
        "attack_damage_total": 9
    }),
    dagon_4_active: new Env({
        "agi": 3,
        "spell_dps_mag_single": 35,
        "cost": 6470,
        "level": 4,
        "base_id": "dagon_4",
        "is_consumable": false,
        "mp_regen": -9,
        "int": 25,
        "str": 3,
        "mana_cost": 180,
        "name": "Dagon 4 (active)",
        "cooldown": 20,
        "attack_damage_total": 9
    }),
    dagon_5: new Env({
        "str": 3,
        "agi": 3,
        "cost": 7720,
        "level": 5,
        "base_id": "dagon_5",
        "is_consumable": false,
        "mana_cost": 180,
        "name": "Dagon 5",
        "cooldown": 15,
        "int": 28,
        "attack_damage_total": 9
    }),
    dagon_5_active: new Env({
        "agi": 3,
        "spell_dps_mag_single": 53.333333333333336,
        "cost": 7720,
        "level": 5,
        "base_id": "dagon_5",
        "is_consumable": false,
        "mp_regen": -12,
        "int": 28,
        "str": 3,
        "mana_cost": 180,
        "name": "Dagon 5 (active)",
        "cooldown": 15,
        "attack_damage_total": 9
    }),
    demon_edge: new Env({
        "cost": 2400,
        "level": 1,
        "base_id": "demon_edge",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Demon Edge",
        "cooldown": 0,
        "attack_damage_total": 46
    }),
    desolator: new Env({
        "cost": 3500,
        "level": 1,
        "base_id": "desolator",
        "built_from": [
            "blight_stone",
            "mithril_hammer",
            "mithril_hammer"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Desolator",
        "cooldown": 0,
        "enemy_armor": -7,
        "attack_damage_total": 50
    }),
    diffusal_blade: new Env({
        "agi": 25,
        "cost": 3150,
        "level": 1,
        "base_id": "diffusal_blade",
        "mults": {"illusions_bonus_damage_phy": {
            "is_ranged": 10,
            "is_melee": 20
        }},
        "built_from": [
            "blade_of_alacrity",
            "blade_of_alacrity",
            "robe"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Diffusal Blade 1",
        "cooldown": 2,
        "int": 10,
        "bonus_damage_phy": [{
            "damage": 40,
            "lifesteal_mult": 1,
            "chance": 1
        }]
    }),
    diffusal_blade_2: new Env({
        "agi": 35,
        "cost": 3850,
        "level": 2,
        "base_id": "diffusal_blade_2",
        "mults": {"illusions_bonus_damage_phy": {
            "is_ranged": 10,
            "is_melee": 20
        }},
        "built_from": [
            "diffusal_blade",
            "recipe_diffusal_blade"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Diffusal Blade 2",
        "cooldown": 2,
        "int": 15,
        "bonus_damage_phy": [{
            "damage": 40,
            "lifesteal_mult": 1,
            "chance": 1
        }]
    }),
    rapier: new Env({
        "cost": 6200,
        "level": 1,
        "base_id": "rapier",
        "built_from": [
            "demon_edge",
            "relic"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Divine Rapier",
        "cooldown": 0,
        "attack_damage_total": 330
    }),
    dragon_lance: new Env({
        "str": 14,
        "agi": 14,
        "cost": 1900,
        "level": 1,
        "base_id": "dragon_lance",
        "built_from": [
            "boots_of_elves",
            "boots_of_elves",
            "ogre_axe"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Dragon Lance",
        "cooldown": 0
    }),
    ancient_janggo: new Env({
        "hp_regen": 3,
        "movement_speed_bonus_mult": 0.05,
        "agi": 3,
        "cost": 1785,
        "level": 1,
        "base_id": "ancient_janggo",
        "is_consumable": false,
        "attack_speed_all": 5,
        "int": 9,
        "str": 6,
        "built_from": [
            "bracer",
            "ring_of_regen",
            "wind_lace"
        ],
        "mana_cost": 0,
        "name": "Drum of Endurance",
        "cooldown": 30,
        "attack_damage_total": 3
    }),
    ancient_janggo_active: new Env({
        "hp_regen": 3,
        "movement_speed_bonus_mult": 0.18,
        "agi": 3,
        "cost": 1785,
        "level": 1,
        "base_id": "ancient_janggo",
        "is_consumable": false,
        "attack_speed_all": 30,
        "mp_regen": 0,
        "int": 9,
        "str": 6,
        "built_from": [
            "bracer",
            "ring_of_regen",
            "wind_lace"
        ],
        "mana_cost": 0,
        "name": "Drum of Endurance (active)",
        "cooldown": 30,
        "attack_damage_total": 3
    }),
    dust: new Env({
        "cost": 180,
        "level": 1,
        "base_id": "dust",
        "is_consumable": true,
        "mana_cost": 5,
        "name": "Dust of Appearance",
        "cooldown": 30
    }),
    dust_active: new Env({
        "cost": 180,
        "level": 1,
        "base_id": "dust",
        "is_consumable": true,
        "mana_cost": 5,
        "name": "Dust of Appearance (active)",
        "cooldown": 30,
        "mp_regen": -0.16666666666666666,
        "enemy_movement_speed_bonus_mult": -0.2
    }),
    eagle: new Env({
        "agi": 25,
        "cost": 3200,
        "level": 1,
        "base_id": "eagle",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Eaglesong",
        "cooldown": 0
    }),
    echo_sabre: new Env({
        "cost": 2650,
        "level": 1,
        "base_id": "echo_sabre",
        "is_consumable": false,
        "int": 10,
        "str": 10,
        "mults": {"attacks_per_second": {"is_melee": 0.2}},
        "built_from": [
            "oblivion_staff",
            "ogre_axe"
        ],
        "attack_speed_hero": 10,
        "mp_regen_base_bonus_mult": 0.75,
        "mana_cost": 0,
        "name": "Echo Sabre",
        "cooldown": 5,
        "attack_damage_total": 15
    }),
    enchanted_mango: new Env({
        "hp_regen": 1,
        "cost": 125,
        "level": 1,
        "base_id": "enchanted_mango",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Enchanted Mango (eaten every minute)",
        "cooldown": 0,
        "mp_regen": 2.5
    }),
    energy_booster: new Env({
        "cost": 900,
        "mp": 250,
        "level": 1,
        "base_id": "energy_booster",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Energy Booster",
        "cooldown": 0
    }),
    ethereal_blade: new Env({
        "str": 10,
        "agi": 40,
        "cost": 4700,
        "level": 1,
        "base_id": "ethereal_blade",
        "built_from": [
            "eagle",
            "ghost"
        ],
        "is_consumable": false,
        "mana_cost": 100,
        "name": "Ethereal Blade",
        "cooldown": 20,
        "int": 10
    }),
    ethereal_blade_enemy: new Env({
        "agi": 40,
        "spell_dps_mag_single": 3.75,
        "cost": 4700,
        "level": 1,
        "base_id": "ethereal_blade",
        "is_consumable": false,
        "enemy_vulnerability_phy": 0.95,
        "mp_regen": -5,
        "int": 10,
        "str": 10,
        "vulnerability_phy": 0.95,
        "mults": {"spell_dps_mag_single": {"primary_attribute_value": 0.1}},
        "built_from": [
            "eagle",
            "ghost"
        ],
        "mana_cost": 100,
        "name": "Ethereal Blade (active) (cast on enemy)",
        "cooldown": 20,
        "enemy_vulnerability_mag": 1.4,
        "enemy_movement_speed_bonus_mult": -0.8
    }),
    ethereal_blade_self: new Env({
        "agi": 40,
        "cost": 4700,
        "level": 1,
        "base_id": "ethereal_blade",
        "is_consumable": false,
        "enemy_vulnerability_phy": 0.95,
        "mp_regen": -5,
        "int": 10,
        "str": 10,
        "vulnerability_phy": 0.95,
        "built_from": [
            "eagle",
            "ghost"
        ],
        "mana_cost": 100,
        "name": "Ethereal Blade (active) (cast on self)",
        "cooldown": 20,
        "vulnerability_mag": 1.4
    }),
    cyclone: new Env({
        "cost": 2735,
        "level": 1,
        "base_id": "cyclone",
        "built_from": [
            "staff_of_wizardry",
            "void_stone",
            "wind_lace"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 175,
        "name": "Eul's Scepter of Divinity",
        "cooldown": 23,
        "movement_speed": 40,
        "int": 10
    }),
    skadi: new Env({
        "agi": 25,
        "cost": 5675,
        "mp": 250,
        "level": 1,
        "base_id": "skadi",
        "is_consumable": false,
        "hp": 225,
        "int": 25,
        "str": 25,
        "enemy_attack_speed": -45,
        "built_from": [
            "orb_of_venom",
            "point_booster",
            "ultimate_orb",
            "ultimate_orb"
        ],
        "mana_cost": 0,
        "name": "Eye of Skadi",
        "cooldown": 0,
        "enemy_movement_speed_bonus_mult": -0.35
    }),
    faerie_fire: new Env({
        "cost": 75,
        "level": 1,
        "base_id": "faerie_fire",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Faerie Fire",
        "cooldown": 5,
        "attack_damage_total": 2
    }),
    faerie_fire_active: new Env({
        "hp_regen": 15,
        "cost": 75,
        "level": 1,
        "base_id": "faerie_fire",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Faerie Fire (active)",
        "cooldown": 5,
        "mp_regen": 0,
        "attack_damage_total": 0
    }),
    flying_courier: new Env({
        "cost": 150,
        "level": 1,
        "base_id": "flying_courier",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Flying Courier",
        "cooldown": 0
    }),
    force_staff: new Env({
        "hp_regen": 4,
        "cost": 2225,
        "level": 1,
        "base_id": "force_staff",
        "built_from": [
            "ring_of_regen",
            "staff_of_wizardry"
        ],
        "is_consumable": false,
        "mana_cost": 25,
        "name": "Force Staff",
        "cooldown": 20,
        "int": 10
    }),
    gauntlets: new Env({
        "str": 3,
        "cost": 150,
        "level": 1,
        "base_id": "gauntlets",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Gauntlets of Strength",
        "cooldown": 0
    }),
    gem: new Env({
        "cost": 900,
        "level": 1,
        "base_id": "gem",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Gem of True Sight",
        "cooldown": 0
    }),
    ghost: new Env({
        "str": 5,
        "agi": 5,
        "cost": 1500,
        "level": 1,
        "base_id": "ghost",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ghost Scepter",
        "cooldown": 25,
        "int": 5
    }),
    ghost_active: new Env({
        "agi": 5,
        "cost": 1500,
        "level": 1,
        "base_id": "ghost",
        "is_consumable": false,
        "enemy_vulnerability_phy": 0.96,
        "mp_regen": 0,
        "int": 5,
        "str": 5,
        "vulnerability_phy": 0.96,
        "mana_cost": 0,
        "name": "Ghost Scepter (active)",
        "cooldown": 25,
        "vulnerability_mag": 1.4
    }),
    glimmer_cape: new Env({
        "cost": 1850,
        "level": 1,
        "base_id": "glimmer_cape",
        "built_from": [
            "cloak",
            "shadow_amulet"
        ],
        "is_consumable": false,
        "attack_speed_hero": 20,
        "mana_cost": 90,
        "name": "Glimmer Cape",
        "cooldown": 16,
        "vulnerability_mag": 0.85
    }),
    glimmer_cape_active: new Env({
        "cost": 1850,
        "level": 1,
        "base_id": "glimmer_cape",
        "built_from": [
            "cloak",
            "shadow_amulet"
        ],
        "is_consumable": false,
        "attack_speed_hero": 20,
        "mana_cost": 90,
        "name": "Glimmer Cape (active)",
        "cooldown": 16,
        "vulnerability_mag": 0.39999999999999997,
        "mp_regen": -5.625
    }),
    gloves: new Env({
        "cost": 500,
        "level": 1,
        "base_id": "gloves",
        "is_consumable": false,
        "attack_speed_hero": 20,
        "mana_cost": 0,
        "name": "Gloves of Haste",
        "cooldown": 0
    }),
    guardian_greaves: new Env({
        "hp_regen": 4,
        "agi": 5,
        "cost": 5275,
        "mp": 250,
        "level": 1,
        "base_id": "guardian_greaves",
        "is_consumable": false,
        "int": 5,
        "movement_speed": 55,
        "str": 5,
        "armor": 7,
        "built_from": [
            "arcane_boots",
            "mekansm"
        ],
        "mana_cost": 0,
        "name": "Guardian Greaves",
        "cooldown": 40
    }),
    guardian_greaves_active: new Env({
        "hp_regen": 10.25,
        "agi": 5,
        "cost": 5275,
        "mp": 250,
        "level": 1,
        "base_id": "guardian_greaves",
        "is_consumable": false,
        "mp_regen": 4,
        "int": 5,
        "movement_speed": 55,
        "str": 5,
        "armor": 7,
        "built_from": [
            "arcane_boots",
            "mekansm"
        ],
        "mana_cost": 0,
        "name": "Guardian Greaves (active)",
        "cooldown": 40
    }),
    guardian_greaves_low_hp: new Env({
        "hp_regen": 19,
        "agi": 5,
        "cost": 5275,
        "mp": 250,
        "level": 1,
        "base_id": "guardian_greaves",
        "is_consumable": false,
        "int": 5,
        "movement_speed": 55,
        "str": 5,
        "armor": 22,
        "built_from": [
            "arcane_boots",
            "mekansm"
        ],
        "mana_cost": 0,
        "name": "Guardian Greaves (low HP)",
        "cooldown": 40
    }),
    hand_of_midas: new Env({
        "cost": 2050,
        "level": 1,
        "base_id": "hand_of_midas",
        "built_from": ["gloves"],
        "is_consumable": false,
        "attack_speed_hero": 30,
        "mana_cost": 0,
        "name": "Hand of Midas",
        "cooldown": 100
    }),
    headdress: new Env({
        "str": 2,
        "hp_regen": 3,
        "agi": 2,
        "cost": 575,
        "level": 1,
        "base_id": "headdress",
        "built_from": [
            "branches",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Headdress",
        "cooldown": 0,
        "int": 2
    }),
    flask: new Env({
        "hp_regen": 50,
        "cost": 110,
        "level": 1,
        "base_id": "flask",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Healing Salve",
        "cooldown": 0,
        "mp_regen": 0
    }),
    heart: new Env({
        "str": 45,
        "cost": 5500,
        "level": 1,
        "base_id": "heart",
        "mults": {"hp_regen": {"hp": 0.0475}},
        "built_from": [
            "reaver",
            "vitality_booster"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Heart of Tarrasque",
        "cooldown": 7,
        "hp": 250
    }),
    heavens_halberd: new Env({
        "str": 20,
        "cost": 3750,
        "level": 1,
        "base_id": "heavens_halberd",
        "mults": {
            "enemy_attack_speed": {
                "is_ranged": -10,
                "is_melee": -20
            },
            "enemy_movement_speed_bonus_mult": {
                "is_ranged": -0.1,
                "is_melee": -0.2
            }
        },
        "built_from": [
            "sange",
            "talisman_of_evasion"
        ],
        "is_consumable": false,
        "mana_cost": 100,
        "name": "Heaven's Halberd",
        "cooldown": 30,
        "enemy_hit_chance_hero": 0.75,
        "attack_damage_total": 25
    }),
    helm_of_iron_will: new Env({
        "hp_regen": 3,
        "cost": 900,
        "armor": 5,
        "level": 1,
        "base_id": "helm_of_iron_will",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Helm of Iron Will",
        "cooldown": 0
    }),
    helm_of_the_dominator: new Env({
        "hp_regen": 3,
        "cost": 1800,
        "armor": 5,
        "level": 1,
        "base_id": "helm_of_the_dominator",
        "built_from": [
            "helm_of_iron_will",
            "lifesteal"
        ],
        "is_consumable": false,
        "mana_cost": 75,
        "name": "Helm of the Dominator",
        "cooldown": 60,
        "lifesteal": 0.15,
        "attack_damage_total": 20
    }),
    helm_of_the_dominator_ogre: new Env({
        "hp_regen": 3,
        "cost": 1800,
        "level": 1,
        "base_id": "helm_of_the_dominator",
        "is_consumable": false,
        "lifesteal": 0.15,
        "mp_regen": -1.25,
        "armor": 13,
        "built_from": [
            "helm_of_iron_will",
            "lifesteal"
        ],
        "mana_cost": 75,
        "name": "Helm of the Dominator (active) (casting Ogre Frostmage)",
        "cooldown": 60,
        "attack_damage_total": 20
    }),
    helm_of_the_dominator_wolf: new Env({
        "hp_regen": 3,
        "cost": 1800,
        "level": 1,
        "base_id": "helm_of_the_dominator",
        "is_consumable": false,
        "lifesteal": 0.15,
        "mp_regen": -1.25,
        "attack_damage_base_bonus_mult_all": 0.3,
        "armor": 5,
        "built_from": [
            "helm_of_iron_will",
            "lifesteal"
        ],
        "mana_cost": 75,
        "name": "Helm of the Dominator (active) (passive Alpha Wolf)",
        "cooldown": 60,
        "attack_damage_total": 20
    }),
    helm_of_the_dominator_hellbear: new Env({
        "hp_regen": 3,
        "cost": 1800,
        "level": 1,
        "base_id": "helm_of_the_dominator",
        "is_consumable": false,
        "lifesteal": 0.15,
        "attack_speed_all": 15,
        "mp_regen": -1.25,
        "armor": 5,
        "built_from": [
            "helm_of_iron_will",
            "lifesteal"
        ],
        "mana_cost": 75,
        "name": "Helm of the Dominator (active) (passive Hellbear Smasher)",
        "cooldown": 60,
        "attack_damage_total": 20
    }),
    helm_of_the_dominator_troll: new Env({
        "hp_regen": 3,
        "cost": 1800,
        "level": 1,
        "base_id": "helm_of_the_dominator",
        "is_consumable": false,
        "lifesteal": 0.15,
        "mp_regen": 1.75,
        "armor": 5,
        "built_from": [
            "helm_of_iron_will",
            "lifesteal"
        ],
        "mana_cost": 75,
        "name": "Helm of the Dominator (active) (passive Hill Troll Priest)",
        "cooldown": 60,
        "attack_damage_total": 20
    }),
    hood_of_defiance: new Env({
        "hp_regen": 8,
        "cost": 1725,
        "level": 1,
        "base_id": "hood_of_defiance",
        "built_from": [
            "cloak",
            "ring_of_health",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 75,
        "name": "Hood of Defiance",
        "cooldown": 60,
        "vulnerability_mag": 0.7
    }),
    hood_of_defiance_active: new Env({
        "hp_regen": 8,
        "eff_hp_mag": 325,
        "cost": 1725,
        "level": 1,
        "base_id": "hood_of_defiance",
        "built_from": [
            "cloak",
            "ring_of_health",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 75,
        "name": "Hood of Defiance (active)",
        "cooldown": 60,
        "vulnerability_mag": 0.7,
        "mp_regen": -1.25
    }),
    hurricane_pike: new Env({
        "hp_regen": 4,
        "str": 15,
        "agi": 20,
        "cost": 4375,
        "level": 1,
        "base_id": "hurricane_pike",
        "built_from": [
            "dragon_lance",
            "force_staff"
        ],
        "is_consumable": false,
        "mana_cost": 25,
        "name": "Hurricane Pike",
        "cooldown": 14,
        "int": 10
    }),
    hyperstone: new Env({
        "cost": 2000,
        "level": 1,
        "base_id": "hyperstone",
        "is_consumable": false,
        "attack_speed_hero": 55,
        "mana_cost": 0,
        "name": "Hyperstone",
        "cooldown": 0
    }),
    infused_raindrop: new Env({
        "eff_hp_mag": 120,
        "cost": 225,
        "armor": 0,
        "level": 1,
        "base_id": "infused_raindrop",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Infused Raindrop",
        "cooldown": 4,
        "mp_regen": 0.85,
        "vulnerability_mag": 1
    }),
    branches: new Env({
        "str": 1,
        "agi": 1,
        "cost": 50,
        "level": 1,
        "base_id": "branches",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Iron Branch",
        "cooldown": 0,
        "int": 1
    }),
    iron_talon: new Env({
        "cost": 500,
        "armor": 2,
        "level": 1,
        "base_id": "iron_talon",
        "mults": {"attack_damage_base_bonus_mult_hero": {
            "enemy_is_creep_and_hero_is_melee": 0.3999999999999999,
            "enemy_is_creep_and_hero_is_ranged": 0.1499999999999999
        }},
        "built_from": [
            "quelling_blade",
            "ring_of_protection"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Iron Talon",
        "cooldown": 14
    }),
    iron_talon_active: new Env({
        "spell_dps_pure_single": 17.142857142857142,
        "cost": 500,
        "armor": 2,
        "level": 1,
        "base_id": "iron_talon",
        "mults": {"attack_damage_base_bonus_mult_hero": {
            "enemy_is_creep_and_hero_is_melee": 0.3999999999999999,
            "enemy_is_creep_and_hero_is_ranged": 0.1499999999999999
        }},
        "built_from": [
            "quelling_blade",
            "ring_of_protection"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Iron Talon (active) (used on 600HP creeps)",
        "cooldown": 14,
        "mp_regen": 0
    }),
    javelin: new Env({
        "cost": 1500,
        "level": 1,
        "base_id": "javelin",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Javelin",
        "cooldown": 0,
        "bonus_damage_mag": [{
            "damage": 85,
            "lifesteal_mult": 1,
            "chance": 0.25
        }],
        "attack_damage_total": 10
    }),
    sphere: new Env({
        "hp_regen": 6,
        "agi": 15,
        "cost": 4800,
        "level": 1,
        "base_id": "sphere",
        "is_consumable": false,
        "int": 15,
        "str": 15,
        "built_from": [
            "pers",
            "ultimate_orb"
        ],
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 0,
        "name": "Linken's Sphere",
        "cooldown": 13,
        "attack_damage_total": 15
    }),
    lotus_orb: new Env({
        "hp_regen": 6,
        "cost": 4000,
        "mp": 250,
        "level": 1,
        "base_id": "lotus_orb",
        "is_consumable": false,
        "armor": 10,
        "built_from": [
            "energy_booster",
            "pers",
            "platemail"
        ],
        "mp_regen_base_bonus_mult": 1.25,
        "mana_cost": 75,
        "name": "Lotus Orb",
        "cooldown": 15,
        "attack_damage_total": 10
    }),
    maelstrom: new Env({
        "cost": 2800,
        "level": 1,
        "base_id": "maelstrom",
        "built_from": [
            "gloves",
            "mithril_hammer"
        ],
        "is_consumable": false,
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Maelstrom",
        "cooldown": 0,
        "bonus_damage_mag": [{
            "damage": 120,
            "lifesteal_mult": 0,
            "chance": 0.25,
            "max_additional_enemies": 4,
            "cooldown": 0
        }],
        "attack_damage_total": 24
    }),
    magic_stick: new Env({
        "hp_regen": 0.5,
        "cost": 200,
        "level": 1,
        "base_id": "magic_stick",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Magic Stick (used fully charged every 5 minutes)",
        "cooldown": 13,
        "mp_regen": 0.5
    }),
    magic_wand: new Env({
        "hp_regen": 0.8500000000000001,
        "agi": 4,
        "cost": 465,
        "level": 1,
        "base_id": "magic_wand",
        "is_consumable": false,
        "mp_regen": 0.8500000000000001,
        "int": 4,
        "str": 4,
        "built_from": [
            "branches",
            "branches",
            "circlet",
            "magic_stick"
        ],
        "mana_cost": 0,
        "name": "Magic Wand (used fully charged every 5 minutes)",
        "cooldown": 13
    }),
    manta: new Env({
        "agi": 26,
        "cost": 4950,
        "level": 1,
        "base_id": "manta",
        "is_consumable": false,
        "movement_speed": 10,
        "int": 10,
        "str": 10,
        "built_from": [
            "ultimate_orb",
            "yasha"
        ],
        "attack_speed_hero": 15,
        "mana_cost": 125,
        "name": "Manta Style",
        "cooldown": 45
    }),
    manta_active: new Env({
        "agi": 26,
        "cost": 4950,
        "level": 1,
        "base_id": "manta",
        "is_consumable": false,
        "mp_regen": -2.7777777777777777,
        "movement_speed": 10,
        "int": 10,
        "illusion": [{
            "mults": {
                "damage_dealt_mult": {
                    "is_ranged": -0.72,
                    "is_melee": -0.67
                },
                "damage_taken_mult": {
                    "is_ranged": 3,
                    "is_melee": 2.5
                }
            },
            "count": 2
        }],
        "str": 10,
        "built_from": [
            "ultimate_orb",
            "yasha"
        ],
        "attack_speed_hero": 15,
        "mana_cost": 125,
        "name": "Manta Style (active)",
        "cooldown": 45
    }),
    mantle: new Env({
        "cost": 150,
        "level": 1,
        "base_id": "mantle",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Mantle of Intelligence",
        "cooldown": 0,
        "int": 3
    }),
    mask_of_madness: new Env({
        "cost": 1800,
        "level": 1,
        "base_id": "mask_of_madness",
        "built_from": ["lifesteal"],
        "is_consumable": false,
        "mana_cost": 25,
        "name": "Mask of Madness",
        "cooldown": 25,
        "lifesteal": 0.2
    }),
    mask_of_madness_active: new Env({
        "movement_speed_bonus_mult": 0.17,
        "cost": 1800,
        "level": 1,
        "base_id": "mask_of_madness",
        "is_consumable": false,
        "lifesteal": 0.2,
        "mp_regen": -1,
        "vulnerability_all": 1.25,
        "built_from": ["lifesteal"],
        "attack_speed_hero": 100,
        "mana_cost": 25,
        "name": "Mask of Madness (active)",
        "cooldown": 25
    }),
    medallion_of_courage: new Env({
        "cost": 1175,
        "armor": 7,
        "level": 1,
        "base_id": "medallion_of_courage",
        "built_from": [
            "blight_stone",
            "chainmail",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Medallion of Courage",
        "cooldown": 7
    }),
    medallion_of_courage_active: new Env({
        "cost": 1175,
        "armor": 0,
        "level": 1,
        "base_id": "medallion_of_courage",
        "built_from": [
            "blight_stone",
            "chainmail",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Medallion of Courage (active)",
        "cooldown": 7,
        "enemy_armor": -7,
        "mp_regen": 0
    }),
    mekansm: new Env({
        "hp_regen": 4,
        "agi": 5,
        "cost": 2275,
        "level": 1,
        "base_id": "mekansm",
        "is_consumable": false,
        "int": 5,
        "str": 5,
        "armor": 5,
        "built_from": [
            "buckler",
            "headdress"
        ],
        "mana_cost": 225,
        "name": "Mekansm",
        "cooldown": 65
    }),
    mekansm_active: new Env({
        "hp_regen": 7.846153846153847,
        "agi": 5,
        "cost": 2275,
        "level": 1,
        "base_id": "mekansm",
        "is_consumable": false,
        "mp_regen": -3.4615384615384617,
        "int": 5,
        "str": 5,
        "armor": 7,
        "built_from": [
            "buckler",
            "headdress"
        ],
        "mana_cost": 225,
        "name": "Mekansm (active)",
        "cooldown": 65
    }),
    mithril_hammer: new Env({
        "cost": 1600,
        "level": 1,
        "base_id": "mithril_hammer",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Mithril Hammer",
        "cooldown": 0,
        "attack_damage_total": 24
    }),
    mjollnir: new Env({
        "cost": 5700,
        "level": 1,
        "base_id": "mjollnir",
        "built_from": [
            "hyperstone",
            "maelstrom"
        ],
        "is_consumable": false,
        "attack_speed_hero": 80,
        "mana_cost": 50,
        "name": "Mjollnir",
        "cooldown": 35,
        "bonus_damage_mag": [{
            "damage": 150,
            "lifesteal_mult": 0,
            "chance": 0.25,
            "max_additional_enemies": 12,
            "cooldown": 0
        }],
        "attack_damage_total": 24
    }),
    mjollnir_active: new Env({
        "cost": 5700,
        "bonus_damage_mag_on_attacked": [{
            "damage": 200,
            "chance": 0.2,
            "max_additional_enemies": 4,
            "cooldown": 1
        }],
        "level": 1,
        "base_id": "mjollnir",
        "is_consumable": false,
        "bonus_damage_mag": [{
            "damage": 150,
            "lifesteal_mult": 0,
            "chance": 0.25,
            "max_additional_enemies": 12,
            "cooldown": 0
        }],
        "mp_regen": -1.4285714285714286,
        "built_from": [
            "hyperstone",
            "maelstrom"
        ],
        "attack_speed_hero": 80,
        "mana_cost": 50,
        "name": "Mjollnir (active)",
        "cooldown": 35,
        "attack_damage_total": 24
    }),
    monkey_king_bar: new Env({
        "cost": 5400,
        "level": 1,
        "base_id": "monkey_king_bar",
        "true_strike_chance_hero": 1,
        "built_from": [
            "demon_edge",
            "javelin",
            "javelin"
        ],
        "is_consumable": false,
        "attack_speed_hero": 15,
        "mana_cost": 0,
        "name": "Monkey King Bar",
        "cooldown": 0,
        "bonus_damage_mag": [{
            "damage": 160,
            "lifesteal_mult": 1,
            "chance": 0.35
        }],
        "attack_damage_total": 66
    }),
    moon_shard: new Env({
        "cost": 4000,
        "level": 1,
        "base_id": "moon_shard",
        "built_from": [
            "hyperstone",
            "hyperstone"
        ],
        "is_consumable": false,
        "attack_speed_hero": 120,
        "mana_cost": 0,
        "name": "Moon Shard",
        "cooldown": 0
    }),
    moon_shard_consumed: new Env({
        "cost": 4000,
        "level": 1,
        "base_id": "moon_shard",
        "built_from": [
            "hyperstone",
            "hyperstone"
        ],
        "is_consumable": false,
        "attack_speed_hero": 60,
        "mana_cost": 0,
        "name": "Moon Shard (consumed)",
        "cooldown": 0
    }),
    lifesteal: new Env({
        "cost": 900,
        "level": 1,
        "base_id": "lifesteal",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Morbid Mask",
        "cooldown": 0,
        "lifesteal": 0.15
    }),
    mystic_staff: new Env({
        "cost": 2700,
        "level": 1,
        "base_id": "mystic_staff",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Mystic Staff",
        "cooldown": 0,
        "int": 25
    }),
    necronomicon: new Env({
        "str": 8,
        "cost": 2650,
        "level": 1,
        "base_id": "necronomicon",
        "built_from": [
            "belt_of_strength",
            "staff_of_wizardry"
        ],
        "is_consumable": false,
        "mana_cost": 50,
        "name": "Necronomicon 1",
        "cooldown": 95,
        "int": 15
    }),
    necronomicon_active: new Env({
        "summon": [
            {
                "attack_damage_basic": 45,
                "mults": {"attack_damage_basic_bonus": {"enemy_has_mana": 15}},
                "base_attack_time": 0.75
            },
            {
                "attack_damage_pierce": 60,
                "base_attack_time": 1
            }
        ],
        "movement_speed_bonus_mult": 0.05,
        "cost": 2650,
        "level": 1,
        "base_id": "necronomicon",
        "is_consumable": false,
        "attack_speed_all": 5,
        "mp_regen": -0.5263157894736842,
        "int": 15,
        "str": 8,
        "mults": {"spell_dps_mag_single": {"enemy_has_mana": 1.25}},
        "built_from": [
            "belt_of_strength",
            "staff_of_wizardry"
        ],
        "mana_cost": 50,
        "name": "Necronomicon 1 (active)",
        "cooldown": 95
    }),
    necronomicon_2: new Env({
        "str": 12,
        "cost": 3850,
        "level": 2,
        "base_id": "necronomicon_2",
        "built_from": [
            "necronomicon",
            "recipe_necronomicon"
        ],
        "is_consumable": false,
        "mana_cost": 50,
        "name": "Necronomicon 2",
        "cooldown": 95,
        "int": 21
    }),
    necronomicon_2_active: new Env({
        "summon": [
            {
                "attack_damage_basic": 60,
                "mults": {"attack_damage_basic_bonus": {"enemy_has_mana": 30}},
                "base_attack_time": 0.75
            },
            {
                "attack_damage_pierce": 90,
                "base_attack_time": 1
            }
        ],
        "movement_speed_bonus_mult": 0.07,
        "cost": 3850,
        "level": 2,
        "base_id": "necronomicon_2",
        "is_consumable": false,
        "attack_speed_all": 7,
        "mp_regen": -0.5263157894736842,
        "int": 21,
        "str": 12,
        "mults": {"spell_dps_mag_single": {"enemy_has_mana": 2.5}},
        "built_from": [
            "necronomicon",
            "recipe_necronomicon"
        ],
        "mana_cost": 50,
        "name": "Necronomicon 2 (active)",
        "cooldown": 95
    }),
    necronomicon_3: new Env({
        "str": 16,
        "cost": 5050,
        "level": 3,
        "base_id": "necronomicon_3",
        "built_from": [
            "necronomicon_2",
            "recipe_necronomicon"
        ],
        "is_consumable": false,
        "mana_cost": 50,
        "name": "Necronomicon 3",
        "cooldown": 95,
        "int": 24
    }),
    necronomicon_3_active: new Env({
        "summon": [
            {
                "attack_damage_basic": 75,
                "mults": {"attack_damage_basic_bonus": {"enemy_has_mana": 45}},
                "base_attack_time": 0.75
            },
            {
                "attack_damage_pierce": 120,
                "base_attack_time": 1
            }
        ],
        "movement_speed_bonus_mult": 0.09,
        "cost": 5050,
        "level": 3,
        "base_id": "necronomicon_3",
        "is_consumable": false,
        "attack_speed_all": 9,
        "mp_regen": -0.5263157894736842,
        "int": 24,
        "str": 16,
        "mults": {"spell_dps_mag_single": {"enemy_has_mana": 3.75}},
        "built_from": [
            "necronomicon_2",
            "recipe_necronomicon"
        ],
        "mana_cost": 50,
        "name": "Necronomicon 3 (active)",
        "cooldown": 95
    }),
    null_talisman: new Env({
        "str": 3,
        "agi": 3,
        "cost": 470,
        "level": 1,
        "base_id": "null_talisman",
        "built_from": [
            "circlet",
            "mantle"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Null Talisman",
        "cooldown": 0,
        "int": 6,
        "attack_damage_total": 3
    }),
    oblivion_staff: new Env({
        "cost": 1650,
        "level": 1,
        "base_id": "oblivion_staff",
        "built_from": [
            "quarterstaff",
            "robe",
            "sobi_mask"
        ],
        "is_consumable": false,
        "attack_speed_hero": 10,
        "mp_regen_base_bonus_mult": 0.75,
        "mana_cost": 0,
        "name": "Oblivion Staff",
        "cooldown": 0,
        "int": 6,
        "attack_damage_total": 15
    }),
    ward_observer: new Env({
        "cost": 65,
        "level": 1,
        "base_id": "ward_observer",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Observer Ward",
        "cooldown": 1
    }),
    octarine_core: new Env({
        "hp_regen": 0,
        "cost": 5900,
        "mp": 425,
        "level": 1,
        "base_id": "octarine_core",
        "is_consumable": false,
        "hp": 425,
        "spell_cooldown_mult": 0.75,
        "int": 25,
        "mults": {"spell_lifesteal": {
            "enemy_is_hero": 0.25,
            "enemy_is_creep": 0.05
        }},
        "built_from": [
            "mystic_staff",
            "soul_booster"
        ],
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Octarine Core",
        "cooldown": 0
    }),
    ogre_axe: new Env({
        "str": 10,
        "cost": 1000,
        "level": 1,
        "base_id": "ogre_axe",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ogre Club",
        "cooldown": 0
    }),
    orb_of_venom: new Env({
        "spell_dps_mag_single": 3,
        "cost": 275,
        "level": 1,
        "base_id": "orb_of_venom",
        "mults": {"enemy_movement_speed_bonus_mult": {
            "is_ranged": -0.04,
            "is_melee": -0.12
        }},
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Orb of Venom",
        "cooldown": 0
    }),
    orchid: new Env({
        "cost": 4075,
        "level": 1,
        "base_id": "orchid",
        "built_from": [
            "oblivion_staff",
            "oblivion_staff"
        ],
        "is_consumable": false,
        "attack_speed_hero": 30,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Orchid Malevolence",
        "cooldown": 18,
        "int": 25,
        "attack_damage_total": 30
    }),
    orchid_active: new Env({
        "cost": 4075,
        "orchid_mult": 0.3,
        "level": 1,
        "base_id": "orchid",
        "is_consumable": false,
        "mp_regen": -5.555555555555555,
        "int": 25,
        "built_from": [
            "oblivion_staff",
            "oblivion_staff"
        ],
        "attack_speed_hero": 30,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Orchid Malevolence (active)",
        "cooldown": 18,
        "attack_damage_total": 30
    }),
    pers: new Env({
        "hp_regen": 6,
        "cost": 1700,
        "level": 1,
        "base_id": "pers",
        "built_from": [
            "ring_of_health",
            "void_stone"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 1.25,
        "mana_cost": 0,
        "name": "Perseverance",
        "cooldown": 0,
        "attack_damage_total": 10
    }),
    phase_boots: new Env({
        "cost": 1240,
        "level": 1,
        "base_id": "phase_boots",
        "is_consumable": false,
        "movement_speed": 45,
        "armor": 0,
        "built_from": [
            "blades_of_attack",
            "blades_of_attack",
            "boots"
        ],
        "attack_speed_hero": 0,
        "mana_cost": 0,
        "name": "Phase Boots",
        "cooldown": 8,
        "block": [{"mults": {"amount": {
            "is_ranged": 0,
            "is_melee": 0
        }}}],
        "attack_damage_total": 24
    }),
    phase_boots_active: new Env({
        "cost": 1240,
        "level": 1,
        "base_id": "phase_boots",
        "is_consumable": false,
        "mp_regen": 0,
        "movement_speed": 45,
        "armor": 0,
        "mults": {"movement_speed_bonus_mult": {
            "is_ranged": 0.0625,
            "is_melee": 0.075
        }},
        "built_from": [
            "blades_of_attack",
            "blades_of_attack",
            "boots"
        ],
        "attack_speed_hero": 0,
        "mana_cost": 0,
        "name": "Phase Boots (active)",
        "cooldown": 8,
        "block": [{"mults": {"amount": {
            "is_ranged": 0,
            "is_melee": 0
        }}}],
        "attack_damage_total": 24
    }),
    pipe: new Env({
        "hp_regen": 12,
        "cost": 3100,
        "level": 1,
        "base_id": "pipe",
        "built_from": [
            "headdress",
            "hood_of_defiance"
        ],
        "is_consumable": false,
        "mana_cost": 100,
        "name": "Pipe of Insight",
        "cooldown": 60,
        "vulnerability_mag": 0.7
    }),
    pipe_active: new Env({
        "hp_regen": 12,
        "eff_hp_mag": 400,
        "cost": 3100,
        "level": 1,
        "base_id": "pipe",
        "built_from": [
            "headdress",
            "hood_of_defiance"
        ],
        "is_consumable": false,
        "mana_cost": 100,
        "name": "Pipe of Insight (active)",
        "cooldown": 60,
        "vulnerability_mag": 0.7,
        "mp_regen": -1.6666666666666667
    }),
    platemail: new Env({
        "cost": 1400,
        "armor": 10,
        "level": 1,
        "base_id": "platemail",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Platemail",
        "cooldown": 0
    }),
    point_booster: new Env({
        "cost": 1200,
        "mp": 175,
        "level": 1,
        "base_id": "point_booster",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Point Booster",
        "cooldown": 0,
        "hp": 175
    }),
    poor_mans_shield: new Env({
        "agi": 6,
        "cost": 500,
        "level": 1,
        "base_id": "poor_mans_shield",
        "built_from": [
            "slippers",
            "slippers",
            "stout_shield"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Poor Man's Shield",
        "cooldown": 0,
        "block": [{
            "chance": 0.53,
            "mults": {
                "amount": {
                    "is_ranged": 10,
                    "is_melee": 20
                },
                "chance": {"enemy_is_hero": 1}
            }
        }]
    }),
    power_treads_agi: new Env({
        "agi": 9,
        "cost": 1350,
        "level": 1,
        "base_id": "power_treads",
        "built_from": [
            "belt_of_strength",
            "boots",
            "gloves"
        ],
        "is_consumable": false,
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Power Treads (agility)",
        "cooldown": 0,
        "movement_speed": 45,
        "attack_damage_total": 0
    }),
    power_treads_int: new Env({
        "cost": 1350,
        "level": 1,
        "base_id": "power_treads",
        "built_from": [
            "belt_of_strength",
            "boots",
            "gloves"
        ],
        "is_consumable": false,
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Power Treads (intelligence)",
        "cooldown": 0,
        "movement_speed": 45,
        "attack_damage_total": 0,
        "int": 9
    }),
    power_treads_str: new Env({
        "str": 9,
        "cost": 1350,
        "level": 1,
        "base_id": "power_treads",
        "built_from": [
            "belt_of_strength",
            "boots",
            "gloves"
        ],
        "is_consumable": false,
        "attack_speed_hero": 25,
        "mana_cost": 0,
        "name": "Power Treads (strength)",
        "cooldown": 0,
        "movement_speed": 45,
        "attack_damage_total": 0
    }),
    quarterstaff: new Env({
        "cost": 875,
        "level": 1,
        "base_id": "quarterstaff",
        "is_consumable": false,
        "attack_speed_hero": 10,
        "mana_cost": 0,
        "name": "Quarterstaff",
        "cooldown": 0,
        "attack_damage_total": 10
    }),
    quelling_blade: new Env({
        "cost": 200,
        "level": 1,
        "base_id": "quelling_blade",
        "mults": {"attack_damage_base_bonus_mult_hero": {
            "enemy_is_creep_and_hero_is_melee": 0.3999999999999999,
            "enemy_is_creep_and_hero_is_ranged": 0.1499999999999999
        }},
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Quelling Blade",
        "cooldown": 4
    }),
    radiance: new Env({
        "cost": 5150,
        "spell_dps_mag_aoe": 50,
        "level": 1,
        "base_id": "radiance",
        "built_from": ["relic"],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Radiance",
        "cooldown": 0,
        "enemy_hit_chance_all": 0.83,
        "attack_damage_total": 65
    }),
    reaver: new Env({
        "str": 25,
        "cost": 3000,
        "level": 1,
        "base_id": "reaver",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Reaver",
        "cooldown": 0
    }),
    refresher: new Env({
        "hp_regen": 12,
        "cost": 5200,
        "level": 1,
        "base_id": "refresher",
        "built_from": [
            "pers",
            "pers"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 2.5,
        "mana_cost": 375,
        "name": "Refresher Orb",
        "cooldown": 195,
        "attack_damage_total": 20
    }),
    ring_of_aquila: new Env({
        "agi": 9,
        "cost": 985,
        "level": 1,
        "base_id": "ring_of_aquila",
        "is_consumable": false,
        "mp_regen": 0.65,
        "int": 3,
        "str": 3,
        "armor": 2,
        "built_from": [
            "ring_of_basilius",
            "wraith_band"
        ],
        "mana_cost": 0,
        "name": "Ring of Aquila",
        "cooldown": 0,
        "attack_damage_total": 10
    }),
    ring_of_basilius: new Env({
        "cost": 500,
        "armor": 2,
        "level": 1,
        "base_id": "ring_of_basilius",
        "built_from": [
            "ring_of_protection",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ring of Basilius",
        "cooldown": 0,
        "mp_regen": 0.65,
        "attack_damage_total": 7
    }),
    ring_of_health: new Env({
        "hp_regen": 6,
        "cost": 850,
        "level": 1,
        "base_id": "ring_of_health",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ring of Health",
        "cooldown": 0
    }),
    ring_of_protection: new Env({
        "cost": 175,
        "armor": 2,
        "level": 1,
        "base_id": "ring_of_protection",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ring of Protection",
        "cooldown": 0
    }),
    ring_of_regen: new Env({
        "hp_regen": 2,
        "cost": 325,
        "level": 1,
        "base_id": "ring_of_regen",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ring of Regen",
        "cooldown": 0
    }),
    robe: new Env({
        "cost": 450,
        "level": 1,
        "base_id": "robe",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Robe of the Magi",
        "cooldown": 0,
        "int": 6
    }),
    rod_of_atos: new Env({
        "cost": 3100,
        "level": 1,
        "base_id": "rod_of_atos",
        "built_from": [
            "staff_of_wizardry",
            "staff_of_wizardry",
            "vitality_booster"
        ],
        "is_consumable": false,
        "mana_cost": 50,
        "name": "Rod of Atos",
        "cooldown": 10,
        "hp": 350,
        "int": 30
    }),
    rod_of_atos_active: new Env({
        "cost": 3100,
        "level": 1,
        "base_id": "rod_of_atos",
        "built_from": [
            "staff_of_wizardry",
            "staff_of_wizardry",
            "vitality_booster"
        ],
        "is_consumable": false,
        "mana_cost": 50,
        "name": "Rod of Atos (active)",
        "cooldown": 10,
        "hp": 350,
        "mp_regen": -5,
        "int": 30,
        "enemy_movement_speed_bonus_mult": -0.6
    }),
    relic: new Env({
        "cost": 3800,
        "level": 1,
        "base_id": "relic",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Sacred Relic",
        "cooldown": 0,
        "attack_damage_total": 60
    }),
    sobi_mask: new Env({
        "cost": 325,
        "level": 1,
        "base_id": "sobi_mask",
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Sage's Mask",
        "cooldown": 0
    }),
    sange: new Env({
        "str": 16,
        "cost": 2050,
        "level": 1,
        "base_id": "sange",
        "mults": {
            "enemy_attack_speed": {
                "is_ranged": -10,
                "is_melee": -20
            },
            "enemy_movement_speed_bonus_mult": {
                "is_ranged": -0.1,
                "is_melee": -0.2
            }
        },
        "built_from": [
            "belt_of_strength",
            "ogre_axe"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Sange",
        "cooldown": 0,
        "attack_damage_total": 10
    }),
    sange_and_yasha: new Env({
        "agi": 16,
        "movement_speed_bonus_mult": 0.16,
        "cost": 4100,
        "level": 1,
        "base_id": "sange_and_yasha",
        "is_consumable": false,
        "str": 16,
        "mults": {
            "enemy_attack_speed": {
                "is_ranged": -13,
                "is_melee": -26
            },
            "enemy_movement_speed_bonus_mult": {
                "is_ranged": -0.13,
                "is_melee": -0.26
            }
        },
        "built_from": [
            "sange",
            "yasha"
        ],
        "attack_speed_hero": 16,
        "mana_cost": 0,
        "name": "Sange and Yasha",
        "cooldown": 0,
        "attack_damage_total": 16
    }),
    satanic: new Env({
        "str": 25,
        "cost": 5900,
        "armor": 5,
        "level": 1,
        "base_id": "satanic",
        "built_from": [
            "helm_of_the_dominator",
            "reaver"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Satanic",
        "cooldown": 35,
        "lifesteal": 0.25,
        "attack_damage_total": 20
    }),
    satanic_active: new Env({
        "cost": 5900,
        "level": 1,
        "base_id": "satanic",
        "is_consumable": false,
        "lifesteal": 2,
        "mp_regen": 0,
        "str": 25,
        "armor": 5,
        "built_from": [
            "helm_of_the_dominator",
            "reaver"
        ],
        "mana_cost": 0,
        "name": "Satanic (active)",
        "cooldown": 35,
        "attack_damage_total": 20
    }),
    sheepstick: new Env({
        "str": 10,
        "agi": 10,
        "cost": 5650,
        "level": 1,
        "base_id": "sheepstick",
        "built_from": [
            "mystic_staff",
            "ultimate_orb",
            "void_stone"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Scythe of Vyse",
        "cooldown": 25,
        "int": 35
    }),
    sheepstick_active: new Env({
        "agi": 10,
        "cost": 5650,
        "level": 1,
        "base_id": "sheepstick",
        "is_consumable": false,
        "mp_regen": -4,
        "int": 35,
        "str": 10,
        "built_from": [
            "mystic_staff",
            "ultimate_orb",
            "void_stone"
        ],
        "mp_regen_base_bonus_mult": 1.5,
        "mana_cost": 100,
        "name": "Scythe of Vyse (active)",
        "cooldown": 25,
        "enemy_base_movement_speed": 140
    }),
    ward_sentry: new Env({
        "cost": 200,
        "level": 1,
        "base_id": "ward_sentry",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Sentry Ward",
        "cooldown": 1
    }),
    shadow_amulet: new Env({
        "cost": 1300,
        "level": 1,
        "base_id": "shadow_amulet",
        "is_consumable": false,
        "attack_speed_hero": 20,
        "mana_cost": 0,
        "name": "Shadow Amulet",
        "cooldown": 10
    }),
    invis_sword: new Env({
        "cost": 2700,
        "level": 1,
        "base_id": "invis_sword",
        "built_from": [
            "claymore",
            "shadow_amulet"
        ],
        "is_consumable": false,
        "attack_speed_hero": 30,
        "mana_cost": 75,
        "name": "Shadow Blade",
        "cooldown": 28,
        "attack_damage_total": 22
    }),
    invis_sword_active: new Env({
        "movement_speed_bonus_mult": 0.2,
        "cost": 2700,
        "level": 1,
        "base_id": "invis_sword",
        "is_consumable": false,
        "mp_regen": -2.6785714285714284,
        "mults": {"spell_dps_phy_single": {"hit_chance_hero": 6.25}},
        "built_from": [
            "claymore",
            "shadow_amulet"
        ],
        "attack_speed_hero": 30,
        "mana_cost": 75,
        "name": "Shadow Blade (active)",
        "cooldown": 28,
        "attack_damage_total": 22
    }),
    shivas_guard: new Env({
        "cost": 4700,
        "armor": 15,
        "level": 1,
        "base_id": "shivas_guard",
        "enemy_attack_speed": -45,
        "built_from": [
            "mystic_staff",
            "platemail"
        ],
        "is_consumable": false,
        "mana_cost": 100,
        "name": "Shiva's Guard",
        "cooldown": 30,
        "int": 30
    }),
    shivas_guard_active: new Env({
        "spell_dps_mag_single": 6.666666666666667,
        "cost": 4700,
        "level": 1,
        "base_id": "shivas_guard",
        "is_consumable": false,
        "mp_regen": -3.3333333333333335,
        "int": 30,
        "armor": 15,
        "enemy_attack_speed": -45,
        "built_from": [
            "mystic_staff",
            "platemail"
        ],
        "mana_cost": 100,
        "name": "Shiva's Guard (active)",
        "cooldown": 30,
        "enemy_movement_speed_bonus_mult": -0.4
    }),
    silver_edge: new Env({
        "agi": 15,
        "cost": 5100,
        "level": 1,
        "base_id": "silver_edge",
        "is_consumable": false,
        "int": 15,
        "str": 15,
        "built_from": [
            "invis_sword",
            "ultimate_orb"
        ],
        "attack_speed_hero": 30,
        "mana_cost": 75,
        "name": "Silver Edge",
        "cooldown": 24,
        "attack_damage_total": 30
    }),
    silver_edge_active: new Env({
        "agi": 15,
        "movement_speed_bonus_mult": 0.2,
        "cost": 5100,
        "level": 1,
        "base_id": "silver_edge",
        "is_consumable": false,
        "mp_regen": -3.125,
        "int": 15,
        "str": 15,
        "enemy_damage_phy_base_bonus_mult": -0.5,
        "mults": {"spell_dps_phy_single": {"hit_chance_hero": 9.375}},
        "built_from": [
            "invis_sword",
            "ultimate_orb"
        ],
        "attack_speed_hero": 30,
        "mana_cost": 75,
        "name": "Silver Edge (active)",
        "cooldown": 24,
        "attack_damage_total": 30
    }),
    basher: new Env({
        "str": 10,
        "cost": 2700,
        "level": 1,
        "base_id": "basher",
        "built_from": [
            "belt_of_strength",
            "javelin"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Skull Basher",
        "cooldown": 2.3,
        "bonus_damage_mag": [{
            "damage": 85,
            "lifesteal_mult": 1,
            "mults": {"chance": {
                "is_ranged": 0.1,
                "is_melee": 0.25
            }},
            "cooldown": 2.3
        }],
        "attack_damage_total": 10
    }),
    slippers: new Env({
        "agi": 3,
        "cost": 150,
        "level": 1,
        "base_id": "slippers",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Slippers of Agility",
        "cooldown": 0
    }),
    smoke_of_deceit: new Env({
        "cost": 50,
        "level": 1,
        "base_id": "smoke_of_deceit",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Smoke of Deceit",
        "cooldown": 1,
        "movement_speed": 15
    }),
    solar_crest: new Env({
        "cost": 2875,
        "armor": 10,
        "level": 1,
        "base_id": "solar_crest",
        "built_from": [
            "medallion_of_courage",
            "talisman_of_evasion"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.75,
        "mana_cost": 0,
        "name": "Solar Crest",
        "cooldown": 7,
        "enemy_hit_chance_hero": 0.75
    }),
    solar_crest_active: new Env({
        "cost": 2875,
        "level": 1,
        "base_id": "solar_crest",
        "is_consumable": false,
        "enemy_armor": -10,
        "enemy_hit_chance_hero": 1,
        "mp_regen": 0,
        "armor": 0,
        "built_from": [
            "medallion_of_courage",
            "talisman_of_evasion"
        ],
        "mp_regen_base_bonus_mult": 0.75,
        "mana_cost": 0,
        "name": "Solar Crest (active)",
        "cooldown": 7,
        "enemy_hit_chance_all": 0.75
    }),
    soul_booster: new Env({
        "hp_regen": 0,
        "cost": 3200,
        "mp": 425,
        "level": 1,
        "base_id": "soul_booster",
        "built_from": [
            "energy_booster",
            "point_booster",
            "vitality_booster"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Soul Booster",
        "cooldown": 0,
        "hp": 425
    }),
    soul_ring: new Env({
        "hp_regen": 3,
        "cost": 775,
        "level": 1,
        "base_id": "soul_ring",
        "built_from": [
            "ring_of_regen",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Soul Ring",
        "cooldown": 30
    }),
    soul_ring_active: new Env({
        "hp_regen": -2,
        "cost": 775,
        "level": 1,
        "base_id": "soul_ring",
        "built_from": [
            "ring_of_regen",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Soul Ring (active)",
        "cooldown": 30,
        "mp_regen": 5
    }),
    staff_of_wizardry: new Env({
        "cost": 1000,
        "level": 1,
        "base_id": "staff_of_wizardry",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Staff of Wizardry",
        "cooldown": 0,
        "int": 10
    }),
    stout_shield: new Env({
        "cost": 200,
        "level": 1,
        "base_id": "stout_shield",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Stout Shield",
        "cooldown": 0,
        "block": [{
            "chance": 0.5,
            "mults": {"amount": {
                "is_ranged": 8,
                "is_melee": 16
            }}
        }]
    }),
    talisman_of_evasion: new Env({
        "cost": 1700,
        "level": 1,
        "base_id": "talisman_of_evasion",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Talisman of Evasion",
        "cooldown": 0,
        "enemy_hit_chance_hero": 0.75
    }),
    tango: new Env({
        "hp_regen": 7.1875,
        "cost": 125,
        "level": 1,
        "base_id": "tango",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Tango",
        "cooldown": 0,
        "mp_regen": 0
    }),
    tome_of_knowledge: new Env({
        "cost": 150,
        "level": 1,
        "base_id": "tome_of_knowledge",
        "is_consumable": true,
        "mana_cost": 0,
        "name": "Tome of Knowledge",
        "cooldown": 0
    }),
    tpscroll: new Env({
        "cost": 50,
        "level": 1,
        "base_id": "tpscroll",
        "is_consumable": true,
        "mana_cost": 75,
        "name": "Town Portal Scroll",
        "cooldown": 70
    }),
    tranquil_boots_mixed: new Env({
        "hp_regen": 6,
        "cost": 900,
        "armor": 3,
        "level": 1,
        "base_id": "tranquil_boots",
        "built_from": [
            "boots",
            "ring_of_protection",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Tranquil Boots (broken half the time)",
        "cooldown": 13,
        "movement_speed": 70
    }),
    tranquil_boots_broken: new Env({
        "hp_regen": 0,
        "cost": 900,
        "armor": 3,
        "level": 1,
        "base_id": "tranquil_boots",
        "built_from": [
            "boots",
            "ring_of_protection",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Tranquil Boots (broken)",
        "cooldown": 13,
        "movement_speed": 55
    }),
    tranquil_boots_intact: new Env({
        "hp_regen": 12,
        "cost": 900,
        "armor": 3,
        "level": 1,
        "base_id": "tranquil_boots",
        "built_from": [
            "boots",
            "ring_of_protection",
            "ring_of_regen"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Tranquil Boots (intact)",
        "cooldown": 13,
        "movement_speed": 85
    }),
    ultimate_orb: new Env({
        "str": 10,
        "agi": 10,
        "cost": 2100,
        "level": 1,
        "base_id": "ultimate_orb",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Ultimate Orb",
        "cooldown": 0,
        "int": 10
    }),
    urn_of_shadows: new Env({
        "str": 6,
        "cost": 875,
        "level": 1,
        "base_id": "urn_of_shadows",
        "built_from": [
            "gauntlets",
            "gauntlets",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Urn of Shadows",
        "cooldown": 7
    }),
    urn_of_shadows_damaging2: new Env({
        "str": 6,
        "spell_dps_pure_single": 2.5,
        "cost": 875,
        "level": 1,
        "base_id": "urn_of_shadows",
        "built_from": [
            "gauntlets",
            "gauntlets",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Urn of Shadows (damaging once per minute)",
        "cooldown": 7
    }),
    urn_of_shadows_damaging: new Env({
        "str": 6,
        "spell_dps_pure_single": 18.75,
        "cost": 875,
        "level": 1,
        "base_id": "urn_of_shadows",
        "built_from": [
            "gauntlets",
            "gauntlets",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Urn of Shadows (damaging)",
        "cooldown": 7
    }),
    urn_of_shadows_healing2: new Env({
        "str": 6,
        "hp_regen": 6.666666666666667,
        "cost": 875,
        "level": 1,
        "base_id": "urn_of_shadows",
        "built_from": [
            "gauntlets",
            "gauntlets",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Urn of Shadows (healing once per minute)",
        "cooldown": 7
    }),
    urn_of_shadows_healing: new Env({
        "str": 6,
        "hp_regen": 50,
        "cost": 875,
        "level": 1,
        "base_id": "urn_of_shadows",
        "built_from": [
            "gauntlets",
            "gauntlets",
            "sobi_mask"
        ],
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 0.5,
        "mana_cost": 0,
        "name": "Urn of Shadows (healing)",
        "cooldown": 7
    }),
    vanguard: new Env({
        "hp_regen": 8,
        "cost": 2150,
        "level": 1,
        "base_id": "vanguard",
        "built_from": [
            "ring_of_health",
            "stout_shield",
            "vitality_booster"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Vanguard",
        "cooldown": 0,
        "hp": 250,
        "block": [{"mults": {"amount": {
            "is_ranged": 16,
            "is_melee": 32
        }}}]
    }),
    veil_of_discord: new Env({
        "hp_regen": 6,
        "agi": 6,
        "cost": 2240,
        "level": 1,
        "base_id": "veil_of_discord",
        "is_consumable": false,
        "int": 12,
        "str": 6,
        "armor": 6,
        "built_from": [
            "helm_of_iron_will",
            "null_talisman",
            "null_talisman"
        ],
        "mana_cost": 50,
        "name": "Veil of Discord",
        "cooldown": 20,
        "attack_damage_total": 6
    }),
    veil_of_discord_active: new Env({
        "hp_regen": 6,
        "agi": 6,
        "cost": 2240,
        "level": 1,
        "base_id": "veil_of_discord",
        "is_consumable": false,
        "mp_regen": -2.5,
        "int": 12,
        "str": 6,
        "armor": 6,
        "built_from": [
            "helm_of_iron_will",
            "null_talisman",
            "null_talisman"
        ],
        "mana_cost": 50,
        "name": "Veil of Discord (active)",
        "cooldown": 20,
        "enemy_vulnerability_mag": 1.25,
        "attack_damage_total": 6
    }),
    vitality_booster: new Env({
        "cost": 1100,
        "level": 1,
        "base_id": "vitality_booster",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Vitality Booster",
        "cooldown": 0,
        "hp": 250
    }),
    vladmir: new Env({
        "hp_regen": 3,
        "agi": 2,
        "cost": 2275,
        "level": 1,
        "base_id": "vladmir",
        "is_consumable": false,
        "mp_regen": 0.8,
        "int": 2,
        "attack_damage_base_bonus_mult_all": 0.15,
        "str": 2,
        "armor": 4,
        "mults": {"lifesteal": {
            "is_ranged": 0.1,
            "is_melee": 0.15
        }},
        "built_from": [
            "headdress",
            "lifesteal",
            "ring_of_basilius"
        ],
        "mana_cost": 0,
        "name": "Vladmir's Offering",
        "cooldown": 0
    }),
    void_stone: new Env({
        "cost": 850,
        "level": 1,
        "base_id": "void_stone",
        "is_consumable": false,
        "mp_regen_base_bonus_mult": 1,
        "mana_cost": 0,
        "name": "Void Stone",
        "cooldown": 0
    }),
    wind_lace: new Env({
        "cost": 235,
        "level": 1,
        "base_id": "wind_lace",
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Wind Lace",
        "cooldown": 0,
        "movement_speed": 20
    }),
    wraith_band: new Env({
        "str": 3,
        "agi": 6,
        "cost": 485,
        "level": 1,
        "base_id": "wraith_band",
        "built_from": [
            "circlet",
            "slippers"
        ],
        "is_consumable": false,
        "mana_cost": 0,
        "name": "Wraith Band",
        "cooldown": 0,
        "int": 3,
        "attack_damage_total": 3
    }),
    yasha: new Env({
        "agi": 16,
        "movement_speed_bonus_mult": 0.08,
        "cost": 2050,
        "level": 1,
        "base_id": "yasha",
        "built_from": [
            "blade_of_alacrity",
            "boots_of_elves"
        ],
        "is_consumable": false,
        "attack_speed_hero": 15,
        "mana_cost": 0,
        "name": "Yasha",
        "cooldown": 0
    }),
    __unknown__: new Env({
        "level": 1,
        "base_id": "__unknown__",
        "is_consumable": false,
        "name": "unknown item"
    })
};