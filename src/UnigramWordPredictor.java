import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

/**
 * A class for predicting the next word in a sequence using a unigram model.
 * The model is trained on input text and maps each word to a list of 
 * words that directly follow it in the text.
 */
public class UnigramWordPredictor implements WordPredictor {
  private Map<String, List<String>> neighborMap;
  private Tokenizer tokenizer;

  /**
   * Constructs a UnigramWordPredictor with the specified tokenizer.
   * 
   * @param tokenizer the tokenizer used to process the input text
   */
  public UnigramWordPredictor(Tokenizer tokenizer) {
    this.tokenizer = tokenizer;
  }

  /**
   * Trains the predictor using the text provided by the Scanner.
   * The method tokenizes the text and builds a map where each word 
   * is associated with a list of words that immediately follow it 
   * in the text. The resultant map is stored in the neighborMap
   * instance variable.
   * 
   * For example:
   * If the input text is: "The cat sat. The cat slept. The dog barked."
   * After tokenizing, the tokens would be: ["the", "cat", "sat", ".", "the", "cat", "slept", ".", "the", "dog", "barked", "."]
   * 
   * The resulting map (neighborMap) would be:
   * {
   *   "the" -> ["cat", "cat", "dog"],
   *   "cat" -> ["sat", "slept"],
   *   "sat" -> ["."],
   *   "." -> ["the", "the"],
   *   "slept" -> ["."],
   *   "dog" -> ["barked"],
   *   "barked" -> ["."]
   * }
   * 
   * The order of the map and the order of each list is not important.
   * 
   * @param scanner the Scanner to read the training text from
   */
  public void train(Scanner scanner) {
    List<String> trainingWords = tokenizer.tokenize(scanner);

    // TODO: Convert the trainingWords into neighborMap here
    neighborMap = new HashMap<String, List<String>>(); 

    for (int i=0; i < trainingWords.size()-1; i++)
    {
      
      List<String> wordFollowUpList = new ArrayList<String>();
 
      if (!neighborMap.containsKey(trainingWords.get(i)))
      {                                  
        wordFollowUpList.add(trainingWords.get(i+1));
        neighborMap.put(trainingWords.get(i), wordFollowUpList);
      }
      else
      {
        List<String> currentWordsList = neighborMap.get(trainingWords.get(i));
        currentWordsList.add(trainingWords.get(i+1));
        neighborMap.put(trainingWords.get(i), currentWordsList);
      }
    }
    System.out.println(neighborMap);
  }

  /**
   * Predicts the next word based on the given context.
   * The prediction is made by randomly selecting from all words 
   * that follow the last word in the context in the training data.
   * 
   * For example:
   * If the input text is: "The cat sat. The cat slept. The dog barked."
   * 
   * The resulting map (neighborMap) would be:
   * {
   *   "the" -> ["cat", "cat", "dog"],
   *   "cat" -> ["sat", "slept"],
   *   "sat" -> ["."],
   *   "." -> ["the", "the"],
   *   "slept" -> ["."],
   *   "dog" -> ["barked"],
   *   "barked" -> ["."]
   * }
   * 
   * When predicting the next word given a context, the predictor should use 
   * the neighbor map to select a word based on the observed frequencies in 
   * the training data. For example:
   * 
   * - If the last word in the context is "the", the next word should be randomly chosen 
   *   from ["cat", "cat", "dog"]. In this case, "cat" has a 2/3 probability 
   *   of being selected, and "dog" has a 1/3 probability, reflecting the 
   *   original distribution of words following "the" in the text.
   * 
   * - If the last word in the context is "cat", the next word should be randomly chosen 
   *   from ["sat", "slept"], giving each an equal 1/2 probability.
   * 
   * - If the last word in the context is ".", the next word should be randomly chosen 
   *   from ["the", "the"], meaning "the" will always be selected 
   *   since it's the only option.
   * 
   * - If the last word in the context is "dog", the next word should be "barked" because 
   *   "barked" is the only word that follows "dog" in the training data.
   * 
   * The probabilities of selecting each word should match the relative 
   * frequencies of the words that follow in the original training data. 
   * 
   * @param context a list of words representing the current context
   * @return the predicted next word, or null if no prediction can be made
   */
  public String predictNextWord(List<String> context) {
    // TODO: Return a predicted word given the words preceding it
    // Hint: only the last word in context should be looked at
    String prediction = "";

    if (prediction == "")
    {
      int max = getNeighborMap().keySet().size()-1;
      int min = 0;

      Random r = new Random();
      int randomNum = r.nextInt(max-min+1) + min;
      
      // now im trying to get a key with a INDEX number

      //System.out.println("Key SET: " + getNeighborMap().keySet().toString()); 
      List<String> test = new ArrayList<String>();
      test.addAll(getNeighborMap().keySet());
      //System.out.println("TEST LIST: " + test);
      
      prediction = test.get(randomNum);
    }

    //System.out.println("test:" + getNeighborMap());

    return prediction;
    //return null;
  }
  
  /**
   * Returns a copy of the neighbor map. The neighbor map is a mapping 
   * from each word to a list of words that have followed it in the training data.
   * 
   * You do not need to modify this method for your project.
   * 
   * @return a copy of the neighbor map
   */
  public Map<String, List<String>> getNeighborMap() {
    Map<String, List<String>> copy = new HashMap<>();

    for (Map.Entry<String, List<String>> entry : neighborMap.entrySet()) {
      List<String> newList = new ArrayList<>(entry.getValue());
      copy.put(entry.getKey(), newList);
    }

    return copy;
  }
}
