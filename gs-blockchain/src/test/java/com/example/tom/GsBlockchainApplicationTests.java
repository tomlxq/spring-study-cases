package com.example.tom;

import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GsBlockchainApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static int difficulty = 5;

    @Test
    public void testBlockChain() {


        Block genesisBlock = new Block("Hi im the first block", "0");
        System.out.println(genesisBlock);

        Block secondBlock = new Block("Yo im the second block", genesisBlock.hash);
        System.out.println(secondBlock);

        Block thirdBlock = new Block("Hey im the third block", secondBlock.hash);
        System.out.println(thirdBlock);


        //add our blocks to the blockChain ArrayList:
        blockChain.add(new Block("Hi im the first block", "0"));
        blockChain.get(0).mineBlock(difficulty);
        blockChain.add(new Block("Yo im the second block", blockChain.get(blockChain.size() - 1).hash));
        blockChain.get(1).mineBlock(difficulty);
        blockChain.add(new Block("Hey im the third block", blockChain.get(blockChain.size() - 1).hash));
        blockChain.get(2).mineBlock(difficulty);


        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(blockchainJson);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
