package com.example.wh40k;

import java.util.*;

import static com.example.wh40k.W40kUnit.W40kUnitSlot.*;

/**
 * Created by user on 23.03.2015.
 */
public class UnitSelection {
    public void setMinHq(int minHq) {
        min.put(HQ, minHq);
    }

    public void setMaxFort(int maxFort) {
        max.put(FORTIFICATION, maxFort);
    }

    public void setMaxLoW(int maxLoW) {
        max.put(LORD_OF_WAR, maxLoW);
    }

    public void setMaxHeavySupport(int maxHeavySupport) {
        max.put(HEAVY_SUPPORT, maxHeavySupport);
    }

    public void setMaxFastAttack(int maxFastAttack) {
        max.put(FAST_ATTACK, maxFastAttack);
    }

    public void setMaxElites(int maxElites) {
        max.put(ELITE, maxElites);
    }

    public void setMaxTroops(int maxTroops) {
        max.put(TROOPS, maxTroops);
    }

    public void setMaxHq(int maxHq) {
        max.put(HQ, maxHq);
    }

    public void setMinFort(int minFort) {
        min.put(FORTIFICATION, minFort);
    }

    public void setMinLoW(int minLoW) {
        min.put(LORD_OF_WAR, minLoW);
    }

    public void setMinHeavySupport(int minHeavySupport) {
        min.put(HEAVY_SUPPORT, minHeavySupport);
    }

    public void setMinFastAttack(int minFastAttack) {
        min.put(FAST_ATTACK, minFastAttack);
    }

    public void setMinElites(int minElites) {
        min.put(ELITE, minElites);
    }

    public void setMinTroops(int minTroops) {
        min.put(TROOPS, minTroops);
    }

    private HashMap<W40kUnit.W40kUnitSlot, Integer> min = new HashMap<W40kUnit.W40kUnitSlot, Integer>();
    private HashMap<W40kUnit.W40kUnitSlot, Integer> max = new HashMap<W40kUnit.W40kUnitSlot, Integer>();
    private HashMap<W40kUnit.W40kUnitSlot, Integer> cur = new HashMap<W40kUnit.W40kUnitSlot, Integer>();
    private Integer maxCost;


    public UnitSelection(Integer maxCost) {
        this.maxCost = maxCost;
        for(W40kUnit.W40kUnitSlot item : W40kUnit.W40kUnitSlot.values()) {
            min.put(item, 0);
            cur.put(item, 0);
            max.put(item, Integer.MAX_VALUE);
        }
    }

    public List<W40kUnit> selection(List<W40kUnit> roster) {
        List<W40kUnit> fullRoster = new ArrayList<W40kUnit>();
        for(W40kUnit unit : roster) {
            //add poor unit
            fullRoster.add(unit);
            double effect = (double)unit.getBasicValue() / unit.getBasicCost();
            HashMap<W40kOptionSlot, W40kOption> bestOptions = new HashMap<W40kOptionSlot, W40kOption>();
            //choose best option in option slot(must be better than unit)
            for(W40kOptionSlot optionSlot : unit.getOptionSlots()) {
                W40kOption bestOption = new W40kOption();
                double bestEffect = effect;
                for (W40kOption option : optionSlot.getOptions()) {
                    if(option.getValue() <= 0)
                        continue;
                    double cost = option.getCost() + unit.getCost();
                    double optionEffect = (option.getValue() + unit.getValue()) / cost;
                    if(optionEffect <= bestEffect) {
                        continue;
                    } else {
                        bestOption = option;
                        bestEffect = optionEffect;
                    }
                }
                if(bestEffect > effect) {
                    bestOptions.put(optionSlot, bestOption);
                }
            }
            //NIY
            bestOptions = sortByValues(bestOptions);
            //No clone unit function and function to add upgrade
            W40kUnit cloneUnit = unit.clone();
            for(Map.Entry<W40kOptionSlot, W40kOption> entry : bestOptions.entrySet()) {
                W40kOption option = entry.getValue();
                for(int i = 0; i < Math.min(entry.getKey().getMax(), unit.getNumberOfModels()); i++) {
                    cloneUnit.addOption(option);
                }
                fullRoster.add(cloneUnit);
                cloneUnit = cloneUnit.clone();
            }
        }

        List<W40kUnit> result = new ArrayList<W40kUnit>();

        List<W40kUnit> hqUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> elitiesUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> fastAttackUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> fortUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> heavySupportUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> lowUnits = new ArrayList<W40kUnit>();
        List<W40kUnit> troopsUnits = new ArrayList<W40kUnit>();

        for (W40kUnit unit : fullRoster) {
            switch (unit.getSlot()) {
                case HQ:
                    hqUnits.add(unit);
                    break;
                case ELITE:
                    elitiesUnits.add(unit);
                    break;
                case FAST_ATTACK:
                    fastAttackUnits.add(unit);
                    break;
                case FORTIFICATION:
                    fortUnits.add(unit);
                    break;
                case HEAVY_SUPPORT:
                    heavySupportUnits.add(unit);
                    break;
                case LORD_OF_WAR:
                    lowUnits.add(unit);
                    break;
                case TROOPS:
                    troopsUnits.add(unit);
                    break;
                default:
                    break;
            }
        }
        AddCheapest(HQ, hqUnits, result);
        AddCheapest(ELITE, elitiesUnits, result);
        AddCheapest(FAST_ATTACK, fastAttackUnits, result);
        AddCheapest(FORTIFICATION, fortUnits, result);
        AddCheapest(HEAVY_SUPPORT, heavySupportUnits, result);
        AddCheapest(LORD_OF_WAR, lowUnits, result);
        AddCheapest(TROOPS, troopsUnits, result);

        Collections.sort(fullRoster, new Comparator<W40kUnit>() {

            @Override
            public int compare(W40kUnit u1, W40kUnit u2) {
                return (u1.getEfficiency() > u2.getEfficiency()) ? -1 :
                        (u1.getEfficiency() < u2.getEfficiency()) ? 1 : 0;
            }
        });
        int sum = 0;
        for(W40kUnit unit : result) {
            sum += unit.getCost();
        }
        if(sum > maxCost) {
            throw new RuntimeException("Cost limit is too low");
        }

        for(int i = 0; i < fullRoster.size(); i++) {
            W40kUnit unit = fullRoster.get(i);
            W40kUnit.W40kUnitSlot slot = unit.getSlot();
            if(cur.get(slot) < max.get(slot)) {
                Boolean isReplaced = false;
                if (cur.get(slot) > 0) {
                    for (int j = 0; j < result.size(); j++) {
                        if (result.get(j).getSlot() == slot &&
                                fullRoster.get(i).getEfficiency() > result.get(j).getEfficiency() &&
                                sum + (fullRoster.get(i).getCost() - result.get(j).getCost()) <= maxCost) {
                            result.set(j, fullRoster.get(i));
                            sum += (fullRoster.get(i).getCost() - result.get(j).getCost());
                            if (!fullRoster.get(i).getUnique()) {
                                i--;
                            }
                            isReplaced = true;
                            break;
                        }
                    }
                }
                if (isReplaced) {
                    continue;
                }
                if (sum + unit.getCost() < maxCost) {
                    result.add(unit);
                    sum += unit.getCost();
                    cur.put(slot, cur.get(slot) + 1);
                    if (!unit.getUnique()) {
                        i--;
                    }
                }
            }
        }
        return result;
    }

    private void AddCheapest(W40kUnit.W40kUnitSlot slot, List<W40kUnit> list, List<W40kUnit> result) {
        while(min.get(slot) > cur.get(slot)) {
            Collections.sort(list, new Comparator<W40kUnit>() {
                public int compare(W40kUnit o1, W40kUnit o2) {
                    return (o1.getCost() > o2.getCost()) ? -1 :
                            (o1.getCost() < o2.getCost()) ? 1 : 0;
                }
            });
            result.add(list.get(list.size() - 1)); //add cheapest
            cur.put(slot, cur.get(slot) + 1);
        }
    }

    private static HashMap<W40kOptionSlot, W40kOption> sortByValues(HashMap<W40kOptionSlot, W40kOption> map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator<Map.Entry<W40kOptionSlot, W40kOption>>() {
            public int compare(Map.Entry<W40kOptionSlot, W40kOption> o1, Map.Entry<W40kOptionSlot, W40kOption> o2) {
                double cost1 = (o1.getValue().getCost() != 0) ? o1.getValue().getCost() : 0.1;
                double cost2 = (o2.getValue().getCost() != 0) ? o2.getValue().getCost() : 0.1;
                return ((o1.getValue().getValue() / cost1) > (o2.getValue().getValue() / cost2)) ? -1 :
                       ((o1.getValue().getValue() / cost1) < (o2.getValue().getValue() / cost2)) ? 1 : 0;
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Integer maxCost) {
        this.maxCost = maxCost;
    }
}
