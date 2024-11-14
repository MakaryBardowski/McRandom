package generators;

import java.util.Map;

public class PercentageRandomGenerator<T> extends AbstractRandomGenerator<T>{
    private static final float NORMALIZATION_COEFFICIENT = 0.01f;
    private static final String CHANCE_PERCENT_OVER_HUNDRED = "total chance needs to be exactly 100%%. Provided: %.5f%%";

    protected final Map<Float, T> resultsByChance;
    /***
     * returns T based on chances provided. total chance cannot exceed 100%.
     * If chances are less than 100%, null can be returned as result
     */
    public PercentageRandomGenerator(Map<Float,T> resultsByChance){
        validateResultsByChance(resultsByChance);
        this.resultsByChance = resultsByChance;
    }

    @Override
    public T getRandom() {
        var lowerBound = 0f;
        var randomFloat = random.nextFloat();
        for(Map.Entry<Float, T> entry : resultsByChance.entrySet()){
            var normalizedItemProbability = entry.getKey()*NORMALIZATION_COEFFICIENT;
            if(randomFloat >= lowerBound && randomFloat <= lowerBound + normalizedItemProbability){
                return entry.getValue();
            }
            lowerBound += normalizedItemProbability;
        }
        return null;
    }

    public static <T> void validateResultsByChance(Map<Float, T> resultsByChance){
        var chanceSum = resultsByChance.keySet().stream().reduce(0f, Float::sum);
        if(chanceSum != 100){
            throw new IllegalArgumentException(String.format(CHANCE_PERCENT_OVER_HUNDRED,chanceSum));
        }
    }
}
