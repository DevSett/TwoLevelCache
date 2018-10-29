package cache;

import cache.TwoLevelCache;
import cache.enums.CacheType;
import lombok.extern.log4j.Log4j;

import java.util.Scanner;

@Log4j
public class App {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("put 3 params: memSize, fileSize and strategyType");
            return;
        }
        TwoLevelCache twoLevelCache = null;
        try {
            twoLevelCache = new TwoLevelCache(Integer.valueOf(args[0]), Integer.valueOf(args[1]), CacheType.parse(args[2]));
        } catch (Exception e) {
            log.error("Error put params");
            return;
        }

        System.out.println("HELP: for put write - \"put <key> <value>\"\nfor remove write - \"remove <key>\"\nfor get write - \"get <value>\"\nfor stop write \"EXIT\"");

        Scanner scanner = new Scanner(System.in);
        String string = null;

        while (!(string = scanner.nextLine()).equalsIgnoreCase("EXIT")) {
            String[] split = string.split(" ");

            if (split.length < 2) continue;

            if (split[0].equalsIgnoreCase("get")) {
                System.out.println(twoLevelCache.get(split[1]));
            }

            if (split[0].equalsIgnoreCase("remove")){
                twoLevelCache.remove(split[1]);
            }

            if (split.length != 3) continue;

            if (split[0].equalsIgnoreCase("put")){
                twoLevelCache.put(split[1],split[2]);
            }

        }
    }
}
