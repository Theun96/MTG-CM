package mtg.managers;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import mtg.fxmlControllers.IController;

public class CardManager {

    private static final String CARD_COLLECTION_PATH = "./files/all_cards.clist";
    private final File cardListFile = new File(CARD_COLLECTION_PATH);

    private static List<Card> cards;
    private final IController controller;

    public CardManager(IController controller) {
        this.controller = controller;
    }

    public List<Card> getCards(){
        return cards;
    }
    
    public void CallAllCardsFromAPI() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Calling API for All Cards");
                    cards = CardAPI.getAllCards();
                    System.out.println("Done getting All Cards");
                    controller.refresh();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        })).start();
    }

    public void WriteAllCardsToFile() {
        if (cards.size() <= 0) {
            throw new NullPointerException("There are no cards in local list!");
        }

        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting to write cards to file");
                    FileOutputStream fos = new FileOutputStream(cardListFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(cards);
                    System.out.println("Done Writing cards to file");
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        })).start();
    }

    public void ReadAllCardsFromFile() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting to read cards from file");
                    FileInputStream fis = new FileInputStream(cardListFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    cards = (List<Card>) ois.readObject();
                    System.out.println("Done Reading cards from file");
                    controller.refresh();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e);
                }
            }
        })).start();
    }
}