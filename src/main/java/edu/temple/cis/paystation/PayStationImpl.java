/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */

package edu.temple.cis.paystation;
import java.util.HashMap;


public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;
    private int moneyCollected;
    public int nickelCount = 0, dimeCount = 0, quarterCount = 0;
    
    //Maps for cancel()
    HashMap<Integer, Integer> coinsInserted = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> previousCoinMap = new HashMap<Integer, Integer>();

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5: 
                nickelCount++;
                break;
            case 10:
                dimeCount++;
                break;
            case 25:
                quarterCount++;
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        populateMap();
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        //Sets global money collected
        moneyCollected = insertedSoFar;
        //Copy the map over to a temp hashmap and clear the main map
        cutMap();
        //Resets global variables
        reset();
        return r;
    }

    @Override
    public HashMap<Integer, Integer> cancel() {

        //Copy the map over to a temp hashmap and clear the main map
        cutMap();
        //Resets global variables
        reset();
        return previousCoinMap;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
        nickelCount = dimeCount = quarterCount = 0;
    }

    @Override
    public int getInsertedSoFar(){
        return insertedSoFar;
    }

    @Override
    public int empty()
    {
        //Resets global variables
        moneyCollected = insertedSoFar;
        reset();
        return moneyCollected;
    }

    @Override
    public void populateMap()
    {
        // to clear previously created map
        coinsInserted.clear();

        if(nickelCount!=0) {
        coinsInserted.put(5, nickelCount);
        }

        if(dimeCount!=0) {
        coinsInserted.put(10,dimeCount);
        }

        if(quarterCount!=0) {
        coinsInserted.put(25,quarterCount);
        }
    }

    @Override
    public void cutMap()
    {
        previousCoinMap.putAll(coinsInserted);
        coinsInserted.clear();
    }

    @Override
    public HashMap<Integer, Integer> getOriginalMap() {
        return coinsInserted;
    }
}
