import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CloneSetCounter {

    /**
     * これは酷いコード!
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Path targetClonePairPath = Paths.get(args[0]);
        Path targetCloneSetPath = Paths.get(args[1]);
        List<String> pairFile = Files.readAllLines(targetClonePairPath);
        List<String> setFile = Files.readAllLines(targetCloneSetPath);

        Set<ClonePair> pairs = parsePairFile(pairFile);
        Set<ClonePair> type3Pairs = pairs.stream().filter(pair -> pair.isType3()).collect(Collectors.toSet());

        Set<Set<ClonePair>> sets = parseSetFile(setFile);
        Set<Set<ClonePair>> type3Sets = sets.stream()
                                                  .filter(set -> set.stream()
                                                              .anyMatch(pair -> type3Pairs.contains(pair)))
                                                  .collect(Collectors.toSet());
        Set<Set<ClonePair>> type2Sets = sets.stream()
                                                  .filter(set -> set.stream()
                                                              .allMatch(pair -> !type3Pairs.contains(pair)))
                                                  .collect(Collectors.toSet());

        System.out.println("### Inputs ####");
        System.out.println("Pairs File : " + targetClonePairPath.getFileName());
        System.out.println("Sets File : " + targetCloneSetPath.getFileName());
        System.out.println("### Results ###");
        System.out.println("# clone pair\t : " + pairs.size());
        System.out.println("\t# type-3 \t : " + type3Pairs.size() + " (" + (double) type3Pairs.size() / pairs.size() * 100 + " %)");
        int type2PairsSize = pairs.size() - type3Pairs.size();
        System.out.println("\t# type-1,2\t : " + type2PairsSize + " (" + (double) type2PairsSize / pairs.size() * 100 + " %)");
        System.out.println("# clone set \t : " + sets.size());
        System.out.println("\t# type-3 \t : " + type3Sets.size() + " (" + (double) type3Sets.size() / sets.size() * 100 + " %)");
        System.out.println("\t# type-1,2\t : " + type2Sets.size() + " (" + (double) type2Sets.size() / sets.size() * 100 + " %)");
    }

    private static Set<Set<ClonePair>> parseSetFile(List<String> setFile) {
        Set<Set<ClonePair>> sets = new HashSet<>();
        List<Fragment> fragments = new ArrayList<>();
        for (int index = 0; index < setFile.size(); index++) {
            String line = setFile.get(index);
            if (!line.startsWith("<class ")) continue;
            while (!setFile.get(++index).startsWith("</class")) {
                String fragmentStr = setFile.get(index);
                fragments.add(new Fragment(fragmentStr.split("\"")[3], Integer.valueOf(fragmentStr.split("\"")[5]), Integer.valueOf(fragmentStr.split("\"")[1])));
            }
            Set<List<Integer>> combinations = generateCombinations(fragments.size());
            Set<ClonePair> set = combinations.stream()
                                                   .map(lt -> new ClonePair(fragments.get(lt.get(0)), fragments.get(lt.get(1))))
                                                   .collect(Collectors.toSet());
            sets.add(set);
            fragments.clear();
        }
        return sets;
    }

    private static Set<List<Integer>> generateCombinations(int size) {
        if (size < 2) return null;
        Set<List<Integer>> combinations = new HashSet<>();
        int num = 0;
        for (int i = 0; i < size - 1; i++)
            for (int j = i + 1; j < size; j++) {
                num++;
                List<Integer> lt = new ArrayList<>();
                lt.add(i);
                lt.add(j);
                combinations.add(lt);
            }
        return combinations;
    }

    private static Set<ClonePair> parsePairFile(List<String> pairFile) {
        Set<ClonePair> pairs = new HashSet<>();
        List<String> pairStr = new ArrayList<>();
        for (int index = 0; index < pairFile.size(); index++) {
            String line = pairFile.get(index);
            if (!line.startsWith("<clone ")) continue;
            pairStr.add(line);
            pairStr.add(pairFile.get(++index));
            pairStr.add(pairFile.get(++index));
            line = pairFile.get(++index);
            if (!line.startsWith("</clone")) continue;
            pairs.add(new ClonePair(pairStr));
            pairStr.clear();
        }
        return pairs;
    }

}
